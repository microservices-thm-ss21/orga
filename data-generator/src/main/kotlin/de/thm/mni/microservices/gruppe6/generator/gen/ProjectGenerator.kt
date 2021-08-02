package de.thm.mni.microservices.gruppe6.generator.gen

import de.thm.mni.microservices.gruppe6.generator.ServiceAddress
import de.thm.mni.microservices.gruppe6.generator.Utils
import org.slf4j.Logger
//import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import de.thm.mni.microservices.gruppe6.lib.classes.projectService.Project
import de.thm.mni.microservices.gruppe6.lib.classes.projectService.ProjectRole
import de.thm.mni.microservices.gruppe6.lib.classes.userService.User
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Mono
import java.util.*

@Service
class ProjectGenerator(private val utils: Utils, private val loginGen: LoginGen): Generator<Project> {

    final val projectGeneratorFlux: Flux<Project>
    final val memberGeneratorFlux: Flux<Triple<UUID, ProjectRole, UUID>>
    final val users: MutableList<User> = mutableListOf()

    private val baseUrl = ServiceAddress.PROJECT.toString()
    private val webClient = WebClient.create(baseUrl)
    //val faker = Faker()
    private lateinit var thread: Job
    private lateinit var projectSink: FluxSink<Project>
    private lateinit var memberSink: FluxSink<Triple<UUID, ProjectRole, UUID>>

    init {
        projectGeneratorFlux = Flux.create { projectSink = it }
        memberGeneratorFlux = Flux.create{ memberSink = it}
    }

    override fun genSingleRandom(logger: Logger?): Mono<Project> {
        if(users.isEmpty()) {
            logger?.error("Generating Project requires at least one user")
            return Mono.empty()
        }

        val projectName = "Random" //faker.company.name()
        val creator = users.random()
        val creatorJWT = loginGen.loginUser(creator, logger)

        return webClient.post()
            .uri("$baseUrl/$projectName")
            .header("Authorization", "Bearer $creatorJWT")
            .exchangeToMono {
                it.bodyToMono(Project::class.java)
            }
            .map {
                memberSink.next(Triple(it.id!!, ProjectRole.ADMIN, creator.id!!))
                projectSink.next(it)
                logger?.info(it.id.toString())
                it
                }
    }

    override fun stop() {
        thread.cancel()
    }

    override fun start(speed: Long, noRandom: Boolean) {
        if(this::thread.isInitialized && thread.isActive) thread.cancel()
        thread = utils.start(this, speed, noRandom)
    }

}
