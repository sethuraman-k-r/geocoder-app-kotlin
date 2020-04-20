package com.learn.kotlin

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.web.client.RestTemplate



@Service
class DataService (@Autowired configProperty: ConfigProperty) {

    val config = configProperty

    fun getPlaceData(place: String): String? {
        var apiUrl = "${config.url}?q=$place&key=${config.key}&limit=1"
        val restTemplate = RestTemplate()
        val result = restTemplate.getForObject(apiUrl, String::class.java)
        return result
    }

}