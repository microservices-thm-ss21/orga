package de.thm.mni.microservices.gruppe6.generator

import de.thm.mni.microservices.gruppe6.generator.gen.Generator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

@Service
class Utils {

    fun randomDate(): LocalDate {
        val startSeconds = Instant.now().minus(Duration.ofDays(100 * 365)).epochSecond
        val endSeconds = Instant.now().minus(Duration.ofDays(10 * 365)).epochSecond
        val random: Long = ThreadLocalRandom
            .current()
            .nextLong(startSeconds, endSeconds)
        return LocalDate.ofInstant(Instant.ofEpochSecond(random), ZoneId.systemDefault())
    }

    fun randomRole(): String {
        return listOf("USER", "ADMIN", "REPORTER").random()
    }

    fun start(generator: Generator<out Any>, speed: Long, noRandom: Boolean): Job {
        return GlobalScope.launch {
            while (true) {
                if (!noRandom) delay(Random.nextLong((speed * 0.67).toLong(), (speed * 1.34).toLong()))
                else delay(speed)
                generator.genSingleRandom(null).subscribe()
            }
        }
    }
}