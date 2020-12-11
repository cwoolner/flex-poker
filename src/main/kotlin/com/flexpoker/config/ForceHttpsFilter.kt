package com.flexpoker.config

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ForceHttpsFilter : Filter {

    companion object {
        const val X_FORWARDED_PROTO = "x-forwarded-proto"
    }

    override fun destroy() {
        // nothing to do
    }

    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        val request = servletRequest as HttpServletRequest
        val response = servletResponse as HttpServletResponse
        val servletPath = if (request.servletPath == null) "" else request.servletPath

        if (request.getHeader(X_FORWARDED_PROTO) != null
            && request.getHeader(X_FORWARDED_PROTO).indexOf("https") != 0
            && request.serverName !== "localhost") {
            response.sendRedirect("https://" + request.serverName + servletPath)
            return
        }
        filterChain.doFilter(request, response)
    }

    override fun init(arg0: FilterConfig) {
        // nothing to do
    }

}