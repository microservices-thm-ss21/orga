import org.ajoberstar.grgit.*

plugins {
    java
    kotlin("jvm") version "1.4.32"
    id("org.ajoberstar.grgit") version "4.1.0"
}

val repos = mapOf(
    Pair("issue-service", Pair("../issue-service", "git@git.thm.de:microservicesss21/issue-service.git")),
    Pair("project-service", Pair("../project-service", "git@git.thm.de:microservicesss21/project-service.git")),
    Pair("user-service", Pair("../user-service", "git@git.thm.de:microservicesss21/user-service.git")),
    Pair("news-service", Pair("../news-service", "git@git.thm.de:microservicesss21/news-service.git")),
    Pair("service-service", Pair("../service-lib", "git@git.thm.de:microservicesss21/service-lib.git"))
)

repositories {
    jcenter()
}

task("gitPull") {
    description = "Pulls git."
    doLast {
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
