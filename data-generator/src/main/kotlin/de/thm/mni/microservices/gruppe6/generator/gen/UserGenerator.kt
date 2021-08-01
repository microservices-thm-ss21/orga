package de.thm.mni.microservices.gruppe6.generator.gen

import de.thm.mni.microservices.gruppe6.generator.ServiceAddress
import de.thm.mni.microservices.gruppe6.generator.Utils
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import de.thm.mni.microservices.gruppe6.lib.classes.userService.User
import de.thm.mni.microservices.gruppe6.lib.classes.userService.UserDTO
import org.slf4j.Logger
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Mono

@Service
class UserGenerator(private val utils: Utils): Generator<User> {

    final val userGeneratorFlux: Flux<User>
    private val webClient = WebClient.create(ServiceAddress.USER.toString())
    val faker = Faker()
    private lateinit var thread: Job
    private lateinit var sink: FluxSink<User>

    init {
        userGeneratorFlux = Flux.create { sink = it }
    }

    override fun genSingleRandom(logger: Logger?): Mono<User> {
        val userDTO = UserDTO()
        userDTO.name = faker.name.firstName()
        userDTO.password = faker.device.serial()
        userDTO.lastName = faker.name.lastName()
        userDTO.dateOfBirth = utils.randomDate()
        userDTO.email = "${faker.rickAndMorty.characters()}@gmail.com"
        userDTO.username = faker.swordArtOnline.gameName()
        userDTO.globalRole = utils.randomGlobalRole()
        return webClient.post().bodyValue(userDTO).exchangeToMono {
            it.bodyToMono(User::class.java)
                .map { user->
                sink.next(user)
                logger?.info(user.id.toString())
                user
            }
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
