package de.thm.mni.microservices.gruppe6.generator

import de.thm.mni.microservices.gruppe6.generator.gen.Generator
import de.thm.mni.microservices.gruppe6.generator.gen.LoginGen
import de.thm.mni.microservices.gruppe6.generator.gen.ProjectGenerator
import de.thm.mni.microservices.gruppe6.generator.gen.UserGenerator
import de.thm.mni.microservices.gruppe6.generator.classes.projectService.Project
import de.thm.mni.microservices.gruppe6.generator.classes.projectService.ProjectRole
import de.thm.mni.microservices.gruppe6.generator.classes.userService.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@ShellComponent
class Commands(private val userGen: UserGenerator, private val projectGen: ProjectGenerator, private val loginGen: LoginGen) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    private val users: MutableList<User> = mutableListOf()
    private val projects: MutableList<Project> = mutableListOf()
    // ProjectID -> ProjectRole -> List of userIDs associated with this role
    private val members: MutableMap<UUID, MutableMap<ProjectRole, MutableList<UUID>>> = mutableMapOf()
    private val logins: MutableList<Pair<UUID,String>> = mutableListOf()

    private val generators: MutableMap<String, Generator<out Any>> = mutableMapOf(
        Pair("login", loginGen),
        Pair("user",userGen),
        Pair("project",projectGen)
    )

    init {
        userGen.userGeneratorFlux.subscribe{
            users.add(it)
            loginGen.users.add(it)
        }

        loginGen.loginGeneratorFlux.subscribe{
            projectGen.logins.add(it)
            userGen.logins.add(it)
            logins.add(it)
        }
        projectGen.projectGeneratorFlux.subscribe(projects::add)

        projectGen.memberGeneratorFlux.subscribe{
            // get Project-Map
            members.getOrPut(it.first) { mutableMapOf() }
                //get role of projects
                .getOrPut(it.second) { mutableListOf() }
                .add(it.third)
        }
    }

    @ShellMethod("Creates default objects to make sure everything is ready to work", group = "General")
    fun setup(){
        userGen.setup()
        loginGen.genSingleRandom(logger, true).flatMap {
            projectGen.genSingleRandom(logger, true)
        }
        .subscribe()
    }


    @ShellMethod("Starts all or specific generator", group = "General")
    fun gen(@ShellOption(defaultValue = "all") type: String, @ShellOption(defaultValue = "5000") speed: Long, noRandom: Boolean){
        when {
            type == "all" -> generators.values.forEach{
                it.start(speed, noRandom, logger)
            }
            generators.containsKey(type) -> generators[type]?.start(speed, noRandom, logger)
            else -> logger.error("Unrecognized type: $type")
        }
    }

    @ShellMethod("Stops all or specific generator", group = "General")
    fun stop(@ShellOption(defaultValue = "all") type: String){
        when {
            type == "all" -> generators.values.forEach{
                it.stop()
            }
            generators.containsKey(type) -> generators[type]?.stop()
            else -> logger.error("Unrecognized type: $type")
        }
    }

    @ShellMethod("Creates a single Object", group = "General")
    fun create(type: String){
        when {
            type == "all" -> generators.values.forEach{
                it.genSingleRandom(logger, true).subscribe()
            }
            generators.containsKey(type) -> generators[type]?.genSingleRandom(logger, true)?.subscribe()
            else -> logger.error("Unrecognized type: $type")
        }
    }
}
