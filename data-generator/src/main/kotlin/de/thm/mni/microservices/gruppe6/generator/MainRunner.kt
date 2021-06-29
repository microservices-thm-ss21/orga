package de.thm.mni.microservices.gruppe6.generator

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import de.thm.mni.microservices.gruppe6.generator.Service.*
import de.thm.mni.microservices.gruppe6.generator.gen.UserGenerator
import de.thm.mni.microservices.gruppe6.generator.model.Issue
import de.thm.mni.microservices.gruppe6.generator.model.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Component
class MainRunner(val userGen: UserGenerator): CommandLineRunner {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val users: MutableList<User> = mutableListOf()

    override fun run(vararg args: String?) {

        val client = WebClient.create("http://${ISSUE.host}:${ISSUE.port}/${ISSUE.uri}")

        client.get().exchangeToFlux {
            it.bodyToFlux(Issue::class.java)
        }.doOnNext {
            logger.info("${it.id}, ${it.message}")
        }.subscribe()
    }
}
