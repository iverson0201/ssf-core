package cn.ocoop.ssf.shiro.filter.authz;

import org.apache.shiro.web.filter.authz.PortFilter;
import org.apache.shiro.web.filter.authz.SslFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 未实现
 * Created by liolay on 15-7-27.
 */
public class NonSslFilter  extends PortFilter {


    public static final int DEFAULT_HTTPS_PORT = 9000;
    public static final String HTTPS_SCHEME = "http";

    public NonSslFilter() {
        setPort(DEFAULT_HTTPS_PORT);
    }

    @Override
    protected String getScheme(String requestScheme, int port) {
        if (port == DEFAULT_HTTP_PORT) {
            return PortFilter.HTTP_SCHEME;
        } else {
            return HTTPS_SCHEME;
        }
    }

    /**
     * Retains the parent method's port-matching behavior but additionally guarantees that the
     *{@code ServletRequest.}{@link javax.servlet.ServletRequest#isSecure() isSecure()}.  If the port does not match or
     * the request is not secure, access is denied.
     *
     * @param request     the incoming {@code ServletRequest}
     * @param response    the outgoing {@code ServletResponse} - ignored in this implementation
     * @param mappedValue the filter-specific config value mapped to this filter in the URL rules mappings - ignored by this implementation.
     * @return {@code true} if the request is received on an expected SSL port and the
     * {@code request.}{@link javax.servlet.ServletRequest#isSecure() isSecure()}, {@code false} otherwise.
     * @throws Exception if the call to {@code super.isAccessAllowed} throws an exception.
     * @since 1.2
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return isAccessAllowedParent(request, response, mappedValue) && !request.isSecure();
    }

    protected boolean isAccessAllowedParent(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        int requiredPort = toPort(mappedValue);
        int requestPort = request.getServerPort();
        return requiredPort == requestPort;
    }

    protected String getSchemes(String requestScheme, int port) {
        if (port == DEFAULT_HTTP_PORT) {
            return HTTP_SCHEME;
        } else if (port == SslFilter.DEFAULT_HTTPS_PORT) {
            return SslFilter.HTTPS_SCHEME;
        } else {
            return requestScheme;
        }
    }

    /**
     * Redirects the request to the same exact incoming URL, but with the port listed in the filter's configuration.
     *
     * @param request     the incoming <code>ServletRequest</code>
     * @param response    the outgoing <code>ServletResponse</code>
     * @param mappedValue the config specified for the filter in the matching request's filter chain.
     * @return {@code false} always to force a redirect.
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {

        //just redirect to the specified port:
        int port = toPort(mappedValue);

        String scheme = getScheme(request.getScheme(), port);

        StringBuilder sb = new StringBuilder();
        sb.append(scheme).append("://");
        sb.append(request.getServerName());
        if (port != DEFAULT_HTTP_PORT && port != SslFilter.DEFAULT_HTTPS_PORT) {
            sb.append(":");
            sb.append(port);
        }
        if (request instanceof HttpServletRequest) {
            sb.append(WebUtils.toHttp(request).getRequestURI());
            String query = WebUtils.toHttp(request).getQueryString();
            if (query != null) {
                sb.append("?").append(query);
            }
        }

        WebUtils.issueRedirect(request, response, sb.toString());

        return false;
    }
}
