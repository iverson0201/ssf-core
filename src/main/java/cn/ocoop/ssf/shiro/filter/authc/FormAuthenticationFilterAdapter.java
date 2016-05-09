package cn.ocoop.ssf.shiro.filter.authc;

import cn.ocoop.ssf.util.AjaxRequestUtil;
import cn.ocoop.ssf.shiro.iface.SubjectService;
import cn.ocoop.ssf.spring.AppContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by liolay on 15-7-28.
 */
public class FormAuthenticationFilterAdapter extends FormAuthenticationFilter {
    public static final String DEFAULT_PROFILE_URL = "/profile";
    public static final String DEFAULT_LOGININFO_KEY = "user";
    public static final String DEFAULT_ERROR_KEY_EXCEPTION_NAME = "shiroLoginFailureException";

    private static final Logger log = LoggerFactory.getLogger(FormAuthenticationFilterAdapter.class);
    private static String profileUrl = DEFAULT_PROFILE_URL;
    private static String loginInfoKey = DEFAULT_LOGININFO_KEY;
    private String failureExceptionAttribute = DEFAULT_ERROR_KEY_EXCEPTION_NAME;

    public static String getLoginInfoKey() {
        return loginInfoKey;
    }

    public void setLoginInfoKey(String loginInfoKey) {
        this.loginInfoKey = loginInfoKey;
    }

    public String getFailureExceptionAttribute() {
        return failureExceptionAttribute;
    }

    public void setFailureExceptionAttribute(String failureExceptionAttribute) {
        this.failureExceptionAttribute = failureExceptionAttribute;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginRequest(request, response) && SecurityUtils.getSubject().isAuthenticated()) return false;

        return super.isAccessAllowed(request, response, mappedValue);
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {

            if (SecurityUtils.getSubject().isAuthenticated()) {
                WebUtils.issueRedirect(request, response, profileUrl);
                return false;
            }

            if (isLoginSubmission(request, response)) {
                if (log.isTraceEnabled()) {
                    log.trace("Login submission detected.  Attempting to execute login.");
                }
                return executeLogin(request, response);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Login page view.");
                }
                //allow them to see the login page ;)
                return true;
            }
        } else {
            if (log.isTraceEnabled()) {
                log.trace("Attempting to access a path which requires authentication.  Forwarding to the " +
                        "Authentication url [" + getLoginUrl() + "]");
            }

            if (!AjaxRequestUtil.isAjaxRequest(WebUtils.toHttp(request))) {
                saveRequestAndRedirectToLogin(request, response);
                return false;
            }

            AjaxRequestUtil.tipLoginInvalid(response);
            return false;

        }
    }

    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response) throws Exception {

        resolveLoginInfo(subject);

        issueSuccessRedirect(request, response);
        //we handled the success redirect directly, prevent the chain from continuing:
        return false;
    }

    protected void resolveLoginInfo(Subject subject) {
        String principal = (String) subject.getPrincipals().getPrimaryPrincipal();
        SubjectService subjectService = AppContext.getBean(SubjectService.class);
        Object loginInfo = subjectService.findLoginInfoByUsername(principal);
        subject.getSession().setAttribute(loginInfoKey, loginInfo);
    }

    protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
        String className = ae.getClass().getName();
        request.setAttribute(getFailureExceptionAttribute(), ae);
        request.setAttribute(getFailureKeyAttribute(), className);
    }


}
