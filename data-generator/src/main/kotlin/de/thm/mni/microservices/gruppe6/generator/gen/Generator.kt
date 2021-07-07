package de.thm.mni.microservices.gruppe6.generator.gen

import reactor.core.publisher.Mono
import org.slf4j.Logger

interface Generator<T> {

    fun genSingleRandom(logger: Logger?): Mono<T>
    fun start(speed: Long, noRandom: Boolean)
    fun stop()
}