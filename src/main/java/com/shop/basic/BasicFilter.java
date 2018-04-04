package com.shop.basic;

import net.sf.json.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Name;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * 基础过滤器
 *
 * @autherAdmin Deram Zhao
 * @creat 2018/4/3
 */
@WebFilter(filterName = "BasicFilter",urlPatterns = "/*")
public class BasicFilter implements Filter{
    private static  final Logger LOGGER= LoggerFactory.getLogger(BasicFilter.class);
    public  static String user_id ="";
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("过滤器初始化");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rep = (HttpServletResponse) response;

        System.out.println("过滤器执行");
        user_id = req.getHeader("user_id");
        chain.doFilter(request,response);
        /*String token = null; 这里已经可以实现拦截未登录用户的请求跳转了 可以放心的做其他工作了
        if (token==null)
            ((HttpServletResponse) response).sendRedirect("http://localhost:8080/web/catalog/login");
        return;*/

    }

    @Override
    public void destroy() {
        System.out.println("过滤器销毁");
    }
}
