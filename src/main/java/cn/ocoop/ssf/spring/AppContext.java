package cn.ocoop.ssf.spring;

import cn.ocoop.ssf.shiro.filter.authc.FormAuthenticationFilterAdapter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by liolay on 15-7-27.
 */
@Component
public class AppContext implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static <T> T getBean(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }

    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static Object getLoginInfo() {
        Session session = SecurityUtils.getSubject().getSession(false);
        if (session == null) return null;

        return session.getAttribute(FormAuthenticationFilterAdapter.getLoginInfoKey());
    }

    public static PrincipalCollection getPrincipals() {
        return SecurityUtils.getSubject().getPrincipals();
    }
}
