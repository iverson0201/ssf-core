package cn.ocoop.ssf.shiro.authz;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 从来源realm获取权限信息
 * Created by liolay on 16-4-1.
 */
public class SingleModularRealmAuthorizer extends ModularRealmAuthorizer {
    public static final String LOGIN_TOKEN_TYPE = "loginTokenType";

    public boolean hasRole(PrincipalCollection principals, String roleIdentifier) {
        assertRealmsConfigured();
        for (Realm realm : getRealms()) {
            if (!(realm instanceof Authorizer) || !realm.supports((AuthenticationToken) SecurityUtils.getSubject().getSession().getAttribute(LOGIN_TOKEN_TYPE)))
                continue;
            if (((Authorizer) realm).hasRole(principals, roleIdentifier)) {
                return true;
            }
        }
        return false;
    }
}
