package cn.ocoop.ssf.shiro.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liolay on 15-8-19.
 */
public class PathMatchingFilterChainResolver extends org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver {
    private static transient final Logger log = LoggerFactory.getLogger(PathMatchingFilterChainResolver.class);

    public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {
        DefaultFilterChainManager filterChainManager = (DefaultFilterChainManager) getFilterChainManager();
        if (!filterChainManager.hasChains()) {
            return null;
        }

        String requestURI = getPathWithinApplication(request);

        //the 'chain names' in this implementation are actually path patterns defined by the user.  We just use them
        //as the chain name for the FilterChainManager's requirements
        List<String> chainNames = new ArrayList<String>();

        for (String pathPattern : filterChainManager.getChainNames()) {

            // If the path does match, then pass on to the subclass implementation for specific checks:
            if (pathMatches(pathPattern, requestURI)) {
                if (log.isTraceEnabled()) {
                    log.trace("Matched path pattern [" + pathPattern + "] for requestURI [" + requestURI + "].  " +
                            "Utilizing corresponding filter chain...");
                }
                chainNames.add(pathPattern);
            }
        }

        if (chainNames.size() == 0) return null;

        return filterChainManager.proxy(originalChain, chainNames);
    }

}
