package com.learn.kotlin

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Service

@PropertySource("classpath:api.properties")
@Configuration
class ConfigProperty {

    @Value("\${api-url}")
    lateinit var url: String

    @Value("\${api-key}")
    lateinit var key: String
}