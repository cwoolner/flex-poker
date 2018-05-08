package com.flexpoker.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForceHttpsFilter implements Filter {

    public static final String X_FORWARDED_PROTO = "x-forwarded-proto";

    @Override
    public void destroy() {
        // nothing to do
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        var request = (HttpServletRequest) servletRequest;
        var response = (HttpServletResponse) servletResponse;

        if (request.getHeader(X_FORWARDED_PROTO) != null //
                && request.getHeader(X_FORWARDED_PROTO).indexOf("https") != 0 //
                && request.getServerName() != "localhost") {
            response.sendRedirect("https://" + request.getServerName()
                    + (request.getServletPath() == null ? "" : request.getServletPath()));
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // nothing to do
    }

}
