package de.thm.mni.microservices.gruppe6.generator.gen

import de.thm.mni.microservices.gruppe6.generator.ServiceAddress
import de.thm.mni.microservices.gruppe6.generator.Utils
import org.slf4j.Logger
//import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import de.thm.mni.microservices.gruppe6.generator.classes.projectService.Project
import de.thm.mni.microservices.gruppe6.generator.classes.projectService.ProjectRole
import org.springframework.http.HttpStatus
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Mono
import java.util.*

@Service
class ProjectGenerator(private val utils: Utils): Generator<Project> {

    final val projectGeneratorFlux: Flux<Project>
    final val memberGeneratorFlux: Flux<Triple<UUID, ProjectRole, UUID>>
    final val logins: MutableList<Pair<UUID, String>> = mutableListOf()

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

    override fun genSingleRandom(logger: Logger, verbose: Boolean): Mono<Project> {
        if(logins.isEmpty()) {
            logger?.error("Generating Project requires at least one logged in user")
            return Mono.empty()
        }

        val projectName = "Random" //faker.company.name()
        val creatorTup = logins.random()

        return webClient.post()
            .uri("$baseUrl/$projectName")
            .header("Authorization", "Bearer ${creatorTup.second}")
            .exchangeToMono {
                if(it.statusCode() != HttpStatus.OK) {
                    logger.error("Error Code ${it.statusCode()} on project creation")
                    Mono.empty()
                }
                else
                    it.bodyToMono(Project::class.java)
            }
            .map {
                memberSink.next(Triple(it.id!!, ProjectRole.ADMIN, creatorTup.first))
                projectSink.next(it)
                if(verbose)logger.info(it.id.toString())
                it
                }
    }

    override fun stop() {
        thread.cancel()
    }

    override fun start(speed: Long, noRandom: Boolean, logger: Logger) {
        if(this::thread.isInitialized && thread.isActive) thread.cancel()
        thread = utils.start(this, speed, noRandom, logger)
    }

}
