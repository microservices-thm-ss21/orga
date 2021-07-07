package de.thm.mni.microservices.gruppe6.generator

import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.concurrent.ThreadLocalRandom

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
}