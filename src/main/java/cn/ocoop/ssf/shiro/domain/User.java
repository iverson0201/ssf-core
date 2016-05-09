package cn.ocoop.ssf.shiro.domain;

import java.io.Serializable;

/**
 * Created by liolay on 15-7-27.
 */
public class User implements Serializable {
    private String id;
    private String username;
    private String password;
    private String salt;
    private String locked;
    private String userId;

    public User() {
    }

    public User(String id, String username, String password, String salt, String locked) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.locked = locked;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", locked='" + locked + '\'' +
                '}';
    }
}
