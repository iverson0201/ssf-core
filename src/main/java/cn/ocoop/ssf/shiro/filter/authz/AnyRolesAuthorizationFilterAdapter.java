package cn.ocoop.ssf.shiro.filter.authz;

import cn.ocoop.ssf.util.AjaxRequestUtil;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * Created by liolay on 15-7-28.
 */
public class AnyRolesAuthorizationFilterAdapter extends RolesAuthorizationFilter {

    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {

        Subject subject = getSubject(request, response);
        String[] rolesArray = (String[]) mappedValue;

        if (rolesArray == null || rolesArray.length == 0) {
            //no roles specified, so nothing to check - allow access.
            return true;
        }

        return Stream.of(rolesArray).anyMatch(subject::hasRole);
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {

        Subject subject = getSubject(request, response);
        // If the subject isn't identified, redirect to login URL
        if (subject.getPrincipal() == null) {
            if (!AjaxRequestUtil.isAjaxRequest(WebUtils.toHttp(request))) {
                saveRequestAndRedirectToLogin(request, response);
            }

            AjaxRequestUtil.tipLoginInvalid(response);

        } else {
            // If subject is known but not authorized, redirect to the unauthorized URL if there is one
            // If no unauthorized URL is specified, just return an unauthorized HTTP status code
            String unauthorizedUrl = getUnauthorizedUrl();
            //SHIRO-142 - ensure that redirect _or_ error code occurs - both cannot happen due to response commit:
            if (!AjaxRequestUtil.isAjaxRequest(WebUtils.toHttp(request))) {
                if (StringUtils.hasText(unauthorizedUrl)) {
                    WebUtils.issueRedirect(request, response, unauthorizedUrl);
                } else {
                    WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } else {
                AjaxRequestUtil.tipPermsInvalid(response);
            }

        }
        return false;
    }

}
