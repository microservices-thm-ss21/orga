package de.thm.mni.microservices.gruppe6.generator.gen

import de.thm.mni.microservices.gruppe6.generator.model.User
import de.thm.mni.microservices.gruppe6.generator.model.UserDTO
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

@Service
class UserGenerator {

    final val userGeneratorFlux: Flux<User>
    private val webClient = WebClient.create("http://localhost:8083/api/users")
    val faker = Faker()
    private lateinit var thread: Job
    private lateinit var sink: FluxSink<User>

    init {
        userGeneratorFlux = Flux.create { sink = it }
    }

    fun createRandomUser(): Mono<User> {
        val userDTO = UserDTO()
        userDTO.name = faker.name.firstName()
        userDTO.lastName = faker.name.lastName()
        userDTO.dateOfBirth = randomDate()
        userDTO.email = "${faker.rickAndMorty.characters()}@gmail.com"
        userDTO.username = faker.swordArtOnline.gameName()
        userDTO.globalRole = listOf("USER", "ADMIN", "REPORTER").random()
        return webClient.post().bodyValue(userDTO).exchangeToMono {
            it.bodyToMono(User::class.java)
        }
    }

    fun stop() {
        thread.cancel()
    }

    @DelicateCoroutinesApi
    fun start() {
        thread = GlobalScope.launch {
            while (true) {
                delay(Random.nextLong(5000, 10000))
                createRandomUser().subscribe(sink::next)
            }
        }
    }

    private fun randomDate(): LocalDate {
        val startSeconds = Instant.now().minus(Duration.ofDays(100 * 365)).epochSecond
        val endSeconds = Instant.now().minus(Duration.ofDays(10 * 365)).epochSecond
        val random: Long = ThreadLocalRandom
            .current()
            .nextLong(startSeconds, endSeconds)
        return LocalDate.ofInstant(Instant.ofEpochSecond(random), ZoneId.systemDefault())
    }

}
