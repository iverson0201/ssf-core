package cn.ocoop.ssf.shiro.realm;

import cn.ocoop.ssf.shiro.domain.User;
import cn.ocoop.ssf.shiro.iface.SubjectService;
import cn.ocoop.ssf.shiro.iface.TokenExtendFace;
import cn.ocoop.ssf.shiro.util.SimpleByteSource;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liolay on 15-7-27.
 */
public class SubjectRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(SubjectRealm.class);

    private SubjectService subjectService;

    public void setSubjectService(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(subjectService.findRoles(username));
        authorizationInfo.setStringPermissions(subjectService.findPermissions(username));

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        User user = subjectService.findByUsername(username);

        if (user == null) {
            throw new UnknownAccountException();//没找到帐号
        }

        if (Boolean.TRUE.equals(user.getLocked())) {
            throw new LockedAccountException(); //帐号锁定
        }

        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUsername(), //用户名
                user.getPassword(), //密码
                new SimpleByteSource(user.getSalt()),
                getName()  //realm name
        );
        return authenticationInfo;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return super.supports(token) && !TokenExtendFace.class.isAssignableFrom(token.getClass());
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
       super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

}
