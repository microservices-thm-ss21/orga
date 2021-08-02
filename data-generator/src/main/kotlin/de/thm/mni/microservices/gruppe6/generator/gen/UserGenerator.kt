package de.thm.mni.microservices.gruppe6.generator.gen

import de.thm.mni.microservices.gruppe6.generator.ServiceAddress
import de.thm.mni.microservices.gruppe6.generator.Utils
//import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import de.thm.mni.microservices.gruppe6.generator.classes.userService.User
import de.thm.mni.microservices.gruppe6.generator.classes.userService.UserDTO
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Service
class UserGenerator(private val utils: Utils): Generator<User> {

    final val userGeneratorFlux: Flux<User>
    private val webClient = WebClient.create(ServiceAddress.USER.toString())
    final val logins: MutableList<Pair<UUID, String>> = mutableListOf()
    //val faker = Faker()
    private lateinit var thread: Job
    private lateinit var sink: FluxSink<User>

    init {
        userGeneratorFlux = Flux.create { sink = it }
    }

    fun setup(){
        sink.next(User(
            UUID.fromString("a443ffd0-f7a8-44f6-8ad3-87acd1e91042"),
            "Peter_Zwegat",
            "password",
            "Peter",
            "Zwegat",
            "peter.zwegat@mni.thm.de",
            LocalDate.now(),
            LocalDateTime.now(),
            "ADMIN",
            null
        ))
    }

    override fun genSingleRandom(logger: Logger, verbose: Boolean): Mono<User> {
        if(logins.isEmpty()) {
            logger.error("Generating Users requires at least one logged in user")
            return Mono.empty()
        }
        val creatorTup = logins.random()

        val userDTO = UserDTO()
        userDTO.name = "Random" //faker.name.firstName()
        userDTO.password = "Random" //faker.device.serial()
        userDTO.lastName = "Random" //faker.name.lastName()
        userDTO.dateOfBirth = utils.randomDate()
        userDTO.email = "Random" //"${faker.rickAndMorty.characters()}@gmail.com"
        userDTO.username = "Random" //faker.swordArtOnline.gameName()
        userDTO.globalRole = utils.randomGlobalRole()
        return webClient.post().bodyValue(userDTO)
            .header("Authorization", "Bearer ${creatorTup.second}")
            .exchangeToMono {
                if(it.statusCode() != HttpStatus.OK) {
                    logger.error("Error Code ${it.statusCode()} on user creation")
                    Mono.empty()
                }
                else
                    it.bodyToMono(User::class.java)
                        .map { user->
                            sink.next(user)
                            if(verbose)logger.info(user.id.toString())
                            user
                        }
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
