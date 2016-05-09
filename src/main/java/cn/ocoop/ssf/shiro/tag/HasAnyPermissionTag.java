package cn.ocoop.ssf.shiro.tag;

import org.apache.shiro.subject.Subject;

/**
 * Created by liolay on 15-8-27.
 */
public class HasAnyPermissionTag extends PermissionTag {
    // Delimeter that separates role names in tag attribute
    private static final String PERMISSION_NAMES_DELIMETER = ",";

    protected boolean showTagBody(String permissions) {
        boolean hasAnyPermission = false;
        Subject subject = getSubject();

        if (subject != null) {
            // Iterate through roles and check to see if the user has one of the roles
            for (String permission : permissions.split(PERMISSION_NAMES_DELIMETER)) {
                if (subject.isPermitted(permission.trim())) {
                    hasAnyPermission = true;
                    break;
                }
            }
        }

        return hasAnyPermission;
    }
}
