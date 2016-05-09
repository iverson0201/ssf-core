package cn.ocoop.ssf.shiro.domain;

/**
 * LoginResult
 * Created by liolay on 16-3-25.
 */
public class LoginResult {
    private String code, msg;
    private Object data;

    LoginResult() {
    }

    LoginResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    LoginResult(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static LoginResult build(String code, String msg) {
        return new LoginResult(code, msg);
    }

    public static LoginResult build(String code, String msg, Object object) {
        return new LoginResult(code, msg, object);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
