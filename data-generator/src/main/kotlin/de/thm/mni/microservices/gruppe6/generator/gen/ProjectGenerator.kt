package de.thm.mni.microservices.gruppe6.generator.gen

import de.thm.mni.microservices.gruppe6.generator.Commands
import de.thm.mni.microservices.gruppe6.generator.Utils
import org.slf4j.Logger
import de.thm.mni.microservices.gruppe6.lib.classes.projectService.MemberDTO
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import de.thm.mni.microservices.gruppe6.lib.classes.projectService.Project
import de.thm.mni.microservices.gruppe6.lib.classes.projectService.ProjectDTO
import de.thm.mni.microservices.gruppe6.lib.classes.userService.User
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Mono
import java.util.*
import kotlin.random.Random

@Service
class ProjectGenerator(private val utils: Utils): Generator<Project> {

    final val projectGeneratorFlux: Flux<Project>
    final val memberGeneratorFlux: Flux<Pair<UUID, MemberDTO>>
    final val users: MutableList<User> = mutableListOf()

    private val webClient = WebClient.create("http://localhost:8082/api/projects")
    val faker = Faker()
    private lateinit var thread: Job
    private lateinit var projectSink: FluxSink<Project>
    private lateinit var memberSink: FluxSink<Pair<UUID, MemberDTO>>

    init {
        projectGeneratorFlux = Flux.create { projectSink = it }
        memberGeneratorFlux = Flux.create{ memberSink = it}
    }

    fun userToMemberDTO(user: User): MemberDTO {
        val memberDTO = MemberDTO()
        memberDTO.projectRole = utils.randomRole()
        memberDTO.userId = user.id
        return  memberDTO
    }

    override fun genSingleRandom(logger: Logger?): Mono<Project> {
        if(users.isEmpty()) {
            logger?.error("Generating Project requires at least one user")
            return Mono.empty()
        }
        val projectDTO = ProjectDTO()
        projectDTO.creatorId = users.random().id
        projectDTO.name = faker.company.buzzwords()
        val fromIndex = Random.nextInt(users.size)
        projectDTO.members = users.subList(
            fromIndex,
            Random.nextInt(fromIndex, users.size)
        ).map {userToMemberDTO(it) }

        return webClient.post().bodyValue(projectDTO).exchangeToMono {
            it.bodyToMono(Project::class.java)
                .map { project ->
                    projectDTO.members!!.forEach {
                            memberDTO ->  memberSink.next(Pair(project.id!!, memberDTO)) }
                    projectSink.next(project)
                    logger?.info(project.id.toString())
                    project
                }
        }
    }

    override fun stop() {
        thread.cancel()
    }

    @DelicateCoroutinesApi
    override fun start(speed: Long, noRandom: Boolean) {
        if(this::thread.isInitialized && thread.isActive) thread.cancel()
        thread = GlobalScope.launch {
            while (true) {
                if(!noRandom) delay(Random.nextLong((speed*0.67).toLong(), (speed*1.34).toLong()))
                else delay(speed)
                genSingleRandom(null).subscribe()
            }
        }
    }

}
