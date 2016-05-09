package cn.ocoop.ssf.shiro.iface;

import cn.ocoop.ssf.shiro.ShiroFilterFactoryBean;
import cn.ocoop.ssf.shiro.domain.User;
import cn.ocoop.ssf.spring.AppContext;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;

import java.util.Map;
import java.util.Set;

/**
 * Created by liolay on 15-7-27.
 */
public abstract class SubjectService {

    private static DefaultFilterChainManager filterChainManager;

    public static DefaultFilterChainManager getFilterChainManager() {
        return filterChainManager;
    }

    public void setFilterChainManager(DefaultFilterChainManager filterChainManager) {
        this.filterChainManager = filterChainManager;
    }

    public abstract Set<String> findRoles(String username);

    public abstract Set<String> findPermissions(String username);

    public abstract User findByUsername(String username);

    public Map<String, String> getFilterChainDefinitionMap() {
        return AppContext.getBean(ShiroFilterFactoryBean.class).getFilterChainDefinitionMap();
    }

    public Object findLoginInfoByUsername(String username) {
        return findByUsername(username);
    }
}
