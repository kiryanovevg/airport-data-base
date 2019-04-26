package com.kiryanov.database.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.view.InternalResourceViewResolver
import org.springframework.web.servlet.view.JstlView


@Configuration
class ViewResolverConfig {

    /*@Bean
    fun internalResourceViewResolver(): ViewResolver {
        val bean = InternalResourceViewResolver()
        bean.setViewClass(JstlView::class.java)
        bean.setPrefix("/webapp/html/")
        bean.setSuffix(".html")
        return bean
    }*/
}