package cn.ocoop.ssf.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by liolay on 15-7-28.
 */
public class AjaxRequestUtil {

    private static final int SC_UNLOGIN = 418;

    private AjaxRequestUtil() {
    }

    public static void tipLoginInvalid(ServletResponse response) throws IOException {
        sendError(response, SC_UNLOGIN, "登录超时或未登录，请重新登录！");
    }

    public static void response(ServletResponse response, int status, Object object) throws IOException {
        HttpServletResponse resp = WebUtils.toHttp(response);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setStatus(status);
        PrintWriter writer = resp.getWriter();
        writer.write(JSON.toJSONString(object));
        writer.flush();
    }

    public static void tipPermsInvalid(ServletResponse response) throws IOException {
        sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "没有该资源的访问权限！");
    }

    private static void sendError(ServletResponse response, int status, String msg) throws IOException {

        HttpServletResponse resp = WebUtils.toHttp(response);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setStatus(status);

        Map<String, Object> errorMsg = Maps.newHashMap();
        errorMsg.put("sysErrorMsg", msg);

        PrintWriter writer = resp.getWriter();
        writer.write(JSON.toJSONString(Maps.newHashMap(errorMsg)));
        writer.flush();
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        return (header != null && "XMLHttpRequest".equals(header)) || request.getRequestURI().indexOf("tpl") != -1 || request.getRequestURI().indexOf("/html/") != -1;
    }
}
