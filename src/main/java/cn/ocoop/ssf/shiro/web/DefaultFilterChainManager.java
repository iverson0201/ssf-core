package cn.ocoop.ssf.shiro.web;

import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.apache.shiro.web.filter.mgt.SimpleNamedFilterList;

import javax.servlet.FilterChain;
import java.util.List;

/**
 * Created by liolay on 15-8-19.
 */
public class DefaultFilterChainManager extends org.apache.shiro.web.filter.mgt.DefaultFilterChainManager {

    public FilterChain proxy(FilterChain original, List<String> chainNames) {
        NamedFilterList configured = new SimpleNamedFilterList(chainNames.toString());
        for (String chainName : chainNames) {
            configured.addAll(getChain(chainName));
        }
        return configured.proxy(original);
    }

}
