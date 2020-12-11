package com.flexpoker.web.init

import com.flexpoker.config.ForceHttpsFilter
import com.flexpoker.config.WebConfig
import com.flexpoker.config.WebSocketConfig
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer
import javax.servlet.ServletContext
import javax.servlet.ServletRegistration

class WebInitializer : AbstractAnnotationConfigDispatcherServletInitializer() {

    override fun onStartup(container: ServletContext) {
        container.addFilter(ForceHttpsFilter::class.java.simpleName, ForceHttpsFilter::class.java)
            .addMappingForUrlPatterns(null, false, "/*")
        super.onStartup(container)
    }

    override fun getRootConfigClasses(): Array<Class<*>>? {
        return null
    }

    override fun getServletConfigClasses(): Array<Class<*>> {
        return arrayOf(WebConfig::class.java, WebSocketConfig::class.java)
    }

    override fun getServletMappings(): Array<String> {
        return arrayOf("/")
    }

    override fun customizeRegistration(registration: ServletRegistration.Dynamic) {
        registration.setAsyncSupported(true)
    }

}