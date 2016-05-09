package cn.ocoop.ssf.shiro.util;

import cn.ocoop.ssf.shiro.domain.User;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by liolay on 15-7-27.
 */
@Component
public class PasswordHelper {

    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    @Value("${password.algorithmName}")
    private String algorithmName = "md5";
    @Value("${password.hashIterations}")
    private int hashIterations = 2;

    public static void main(String[] args) {
        PasswordHelper p = new PasswordHelper();
        User user = new User();
        user.setUsername("15300000000");
        user.setPassword("hello");
        p.encryptPassword(user);
        System.out.println(user);

    }

    public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    public void encryptPassword(User user) {

        user.setSalt(user.getUsername());

        String newPassword = new SimpleHash(
                algorithmName,
                user.getPassword(),
                new SimpleByteSource(user.getSalt()),
                hashIterations).toHex();

        user.setPassword(newPassword);
    }

    public String encryptPassword(String salt, String password) {

        return new SimpleHash(
                algorithmName,
                password,
                new SimpleByteSource(salt),
                hashIterations).toHex();
    }
}
