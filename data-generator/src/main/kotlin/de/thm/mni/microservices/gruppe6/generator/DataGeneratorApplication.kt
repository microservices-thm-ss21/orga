package de.thm.mni.microservices.gruppe6.generator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
open class DataGeneratorApplication

fun main(args: Array<String>) {
    runApplication<DataGeneratorApplication>(*args)
}


