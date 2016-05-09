package cn.ocoop.ssf.shiro.service;

import com.alibaba.fastjson.JSON;
import cn.ocoop.ssf.shiro.domain.LoginResult;
import cn.ocoop.ssf.spring.AppContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by liolay on 16-3-25.
 */
public abstract class AbstractAuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AbstractAuthenticationService.class);

    public LoginResult login(ServletRequest request, ServletResponse response, AuthenticationToken token) {

        try {
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        } catch (Throwable e) {
            logger.error("登录异常", e);
            return LoginResult.build("500", "系统异常");
        }

    }

    protected LoginResult onLoginSuccess(AuthenticationToken token, Subject subject,
                                         ServletRequest request, ServletResponse response) {
        Session session = subject.getSession(false);

        LoginResult result = LoginResult.build("200", "登录成功", null);
        if (session == null) return result;
        result.setData(JSON.toJSONString(storeSessionId(request, response, session)));
        onLoginSuccess(token);
        return result;
    }

    protected abstract void onLoginSuccess(AuthenticationToken token);

    protected LoginResult onLoginFailure(AuthenticationToken token, AuthenticationException e,
                                         ServletRequest request, ServletResponse response) {

        String errorClassName = e.getClass().getName();
        String errorMsg = "用户名/密码错误";
        if (UnknownAccountException.class.getName().equals(errorClassName)) {
            errorMsg = "用户名不存在";
        } else if (IncorrectCredentialsException.class.getName().equals(errorClassName)) {
            errorMsg = "密码错误";
        } else if (errorClassName != null) {
            errorMsg = e.getMessage();
        }
        return LoginResult.build("400", errorMsg);
    }

    private Cookie storeSessionId(ServletRequest request, ServletResponse response, Session session) {
        DefaultWebSessionManager defaultWebSessionManager = (DefaultWebSessionManager) AppContext.getBean(WebSessionManager.class);
        Cookie template = defaultWebSessionManager.getSessionIdCookie();
        Cookie cookie = new SimpleCookie(template);
        String idString = session.getId().toString();
        cookie.setValue(idString);
        cookie.saveTo(WebUtils.toHttp(request), WebUtils.toHttp(response));
        return cookie;
    }
}

