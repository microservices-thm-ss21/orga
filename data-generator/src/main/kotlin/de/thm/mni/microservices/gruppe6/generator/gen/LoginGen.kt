package de.thm.mni.microservices.gruppe6.generator.gen

import de.thm.mni.microservices.gruppe6.generator.ServiceAddress
import de.thm.mni.microservices.gruppe6.generator.Utils
import de.thm.mni.microservices.gruppe6.generator.classes.userService.User
import kotlinx.coroutines.Job
import org.slf4j.Logger
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Mono

@Service
class LoginGen(private val utils: Utils): Generator<String> {

    final val users: MutableList<User> = mutableListOf()

    final val loginGeneratorFlux: Flux<String>
    private val webClient = WebClient.create(ServiceAddress.LOGIN.toString())
    private lateinit var thread: Job
    private lateinit var sink: FluxSink<String>

    init {
        loginGeneratorFlux = Flux.create { sink = it }
    }

    fun loginUser(user: User, logger: Logger?): Mono<String> {
        return webClient.get()
            .header("Authorization", "Basic ${user.name}:${user.password}")
            .exchangeToMono {
                    Mono.just(
                        it.headers()
                            .header("authorization")[0].substring("Bearer ".length))}
            .map {
                sink.next(it)
                logger?.info(user.id.toString())
                it
            }
    }


    override fun genSingleRandom(logger: Logger?): Mono<String> {
        if(users.isEmpty()) {
            logger?.error("Logging in requires at least one user")
            return Mono.empty()
        }
        return loginUser(users.random(), logger)
    }

    override fun stop() {
        thread.cancel()
    }

    override fun start(speed: Long, noRandom: Boolean) {
        if(this::thread.isInitialized && thread.isActive) thread.cancel()
        thread = utils.start(this, speed, noRandom)
    }


}