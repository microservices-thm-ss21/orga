import org.ajoberstar.grgit.*

plugins {
    java
    kotlin("jvm") version "1.4.32"
    id("org.ajoberstar.grgit") version "4.1.0"
}

val repos = mapOf(
    Pair("service-lib", Pair("../service-lib", "git@git.thm.de:microservicesss21/service-lib.git")),
    Pair("issue-service", Pair("../issue-service", "git@git.thm.de:microservicesss21/issue-service.git")),
    Pair("project-service", Pair("../project-service", "git@git.thm.de:microservicesss21/project-service.git")),
    Pair("user-service", Pair("../user-service", "git@git.thm.de:microservicesss21/user-service.git")),
    Pair("news-service", Pair("../news-service", "git@git.thm.de:microservicesss21/news-service.git"))
)

repositories {
    jcenter()
}

tasks.register("buildAll") {
    dependsOn("buildIssueService", "buildProjectService", "buildUserService", "buildNewsService", "buildServiceLib")
}

tasks.register("buildIssueService", GradleBuild::class) {
    description = "Build IssueService"
    buildFile = File("../issue-service/build.gradle.kts")
    tasks = listOf("build")
}

tasks.register("buildProjectService", GradleBuild::class) {
    description = "Build ProjectService"
    buildFile = File("../project-service/build.gradle.kts")
    tasks = listOf("build")
}

tasks.register("buildUserService", GradleBuild::class) {
    description = "Build UserService"
    buildFile = File("../user-service/build.gradle.kts")
    tasks = listOf("build")
}

tasks.register("buildNewsService", GradleBuild::class) {
    description = "Build NewsService"
    buildFile = File("../news-service/build.gradle.kts")
    tasks = listOf("build")
}

tasks.register("buildServiceLib", GradleBuild::class) {
    description = "Shortcut to publish service-lib"
    buildFile = File("../service-lib/lib/build.gradle.kts")
    tasks = listOf("build")
}

task("gitPull") {
    description = "Pulls git."
    doFirst {
        repos.forEach {
            if (file(it.value.first).exists()) {
                val project = Grgit.open { dir = "$rootDir/${it.value.first}"; }
                project.pull()
            }
        }
    }
}

task("gitClone") {
    description = "Clones git."
    doLast {
        repos.forEach {
            if (!file(it.value.first).exists()) {
                val project = Grgit.clone {dir = "$rootDir/${it.value.first}"; uri = it.value.second;}
            }
        }
    }
}
