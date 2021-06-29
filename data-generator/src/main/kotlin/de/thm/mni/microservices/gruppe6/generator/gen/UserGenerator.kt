package de.thm.mni.microservices.gruppe6.generator.gen

import de.thm.mni.microservices.gruppe6.generator.model.UserDTO
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random


@Service
class UserGenerator {

    val userGeneratorFlux: Flux<UserDTO>
    private var running = true
    private val faker = Faker()

    init {
        userGeneratorFlux = Flux.create {
            runBlocking {
                launch {
                    while (running) {
                        delay(Random.nextLong(5000, 10000))
                        val user = UserDTO()
                        user.name = faker.name.firstName()
                        user.lastName = faker.name.lastName()
                        user.dateOfBirth = randomDate()
                        user.username = faker.swordArtOnline.gameName()
                        user.globalRole = listOf("USER", "ADMIN", "REPORTER").random()
                        it.next(UserDTO())
                    }
                }
            }
        }
    }

    fun stop() {
        this.running = false
    }

    fun start() {
        this.running = true
    }

    private fun randomDate(): LocalDate {
        val startSeconds = Instant.now().minus(Duration.ofDays(100 * 365)).epochSecond
        val endSeconds = Instant.now().minus(Duration.ofDays(10 * 365)).epochSecond
        val random: Long = ThreadLocalRandom
            .current()
            .nextLong(startSeconds, endSeconds)
        return LocalDate.from(Instant.ofEpochSecond(random))
    }

}
