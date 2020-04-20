package com.learn.kotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication
class KotlinApplication: SpringBootServletInitializer()

fun main(args: Array<String>) {
	runApplication<KotlinApplication>(*args)
}
