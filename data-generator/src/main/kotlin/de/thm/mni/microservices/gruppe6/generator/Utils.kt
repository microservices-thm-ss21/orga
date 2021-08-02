package de.thm.mni.microservices.gruppe6.generator

import de.thm.mni.microservices.gruppe6.generator.gen.Generator
import de.thm.mni.microservices.gruppe6.generator.classes.projectService.ProjectRole
import de.thm.mni.microservices.gruppe6.generator.classes.userService.GlobalRole
import kotlinx.coroutines.*
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
        val startSeconds = Instant.now().minus(Duration.ofDays((100 * 365).toLong())).epochSecond
        val endSeconds = Instant.now().minus(Duration.ofDays((10 * 365).toLong())).epochSecond
        val random: Long = ThreadLocalRandom
            .current()
            .nextLong(startSeconds, endSeconds)
        return LocalDate.ofInstant(Instant.ofEpochSecond(random), ZoneId.systemDefault())
    }

    fun randomGlobalRole(): GlobalRole {
        return GlobalRole.values().random()
    }

    fun randomProjectRole(): ProjectRole {
        return ProjectRole.values().random()
    }

    fun <E> randomSubList(list: List<E>, maxSize: Int = list.size): List<E> {
        val fromIndex = Random.nextInt(list.size)
        // Only allow a max size defined in config. If bigger than list size, take max size instead
        val lastIndex = Random.nextInt(fromIndex, list.size.coerceAtMost(fromIndex + maxSize))
        return list.subList(fromIndex, lastIndex)
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