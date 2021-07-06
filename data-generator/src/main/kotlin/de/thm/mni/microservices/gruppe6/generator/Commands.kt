package de.thm.mni.microservices.gruppe6.generator

import de.thm.mni.microservices.gruppe6.generator.gen.UserGenerator
import de.thm.mni.microservices.gruppe6.generator.model.User
import io.github.serpro69.kfaker.Faker
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod

@ShellComponent
class Commands(private val userGen: UserGenerator) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val users: MutableList<User> = mutableListOf()

    init {
        userGen.userGeneratorFlux.subscribe(users::add)
    }

    @ShellMethod("Generate random user.", group = "User")
    fun createUser() {
        userGen.createRandomUser().doOnNext {
            logger.info(it.id.toString())
        }.subscribe(users::add)
    }

    @ShellMethod("Generate random users.", group = "User")
    fun genUsers() {
        userGen.start()
    }

    @ShellMethod("Stop generating random users.", group = "User")
    fun stopUsers() {
        userGen.stop()
    }
}
