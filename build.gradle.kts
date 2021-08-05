import org.ajoberstar.grgit.*
import org.gradle.internal.os.OperatingSystem;

plugins {
    id("org.ajoberstar.grgit") version "4.1.0"
}

val repos = mapOf(
    Pair("orga", Pair("../orga", "git@git.thm.de:microservicesss21/orga.git")),
    Pair("service-lib", Pair("../service-lib", "git@git.thm.de:microservicesss21/service-lib.git")),
    Pair("issue-service", Pair("../issue-service", "git@git.thm.de:microservicesss21/issue-service.git")),
    Pair("project-service", Pair("../project-service", "git@git.thm.de:microservicesss21/project-service.git")),
    Pair("user-service", Pair("../user-service", "git@git.thm.de:microservicesss21/user-service.git")),
    Pair("news-service", Pair("../news-service", "git@git.thm.de:microservicesss21/news-service.git")),
    Pair("gateway", Pair("../gateway", "git@git.thm.de:microservicesss21/gateway.git")),
    Pair("stresstest-gatling", Pair("../stresstest-gatling", "git@git.thm.de:microservicesss21/gatling-service.git"))
)

repositories {
    jcenter()
}

tasks.register("buildAll") {
    dependsOn("publishServiceLibMaven", "buildIssueService", "buildProjectService", "buildUserService", "buildNewsService", "buildGateway")
}

tasks.register("buildGateway", GradleBuild::class) {
    this.shouldRunAfter("publishServiceLibMaven")
    description = "Build Gateway"
    buildFile = File("../gateway/build.gradle.kts")
    tasks = listOf("clean", "build")
}

tasks.register("buildIssueService", GradleBuild::class) {
    this.shouldRunAfter("publishServiceLibMaven")
    description = "Build IssueService"
    buildFile = File("../issue-service/build.gradle.kts")
    tasks = listOf("clean", "build")
}

tasks.register("buildProjectService", GradleBuild::class) {
    this.shouldRunAfter("publishServiceLibMaven")
    description = "Build ProjectService"
    buildFile = File("../project-service/build.gradle.kts")
    tasks = listOf("clean", "build")
}

tasks.register("buildUserService", GradleBuild::class) {
    this.shouldRunAfter("publishServiceLibMaven")
    description = "Build UserService"
    buildFile = File("../user-service/build.gradle.kts")
    tasks = listOf("clean", "build")
}

tasks.register("buildNewsService", GradleBuild::class) {
    this.shouldRunAfter("publishServiceLibMaven")
    description = "Build NewsService"
    buildFile = File("../news-service/build.gradle.kts")
    tasks = listOf("clean", "build")
}

tasks.register("publishServiceLib", GradleBuild::class) {
    description = "Shortcut to publish service-lib"
    buildFile = File("../service-lib/lib/build.gradle.kts")
    tasks = listOf("clean", "publish")
}

tasks.register("publishServiceLibMaven", Exec::class) {
    if (OperatingSystem.current().isWindows) {
        workingDir = File("../service-lib")
        commandLine = listOf("cmd", "/c", "mvnw.cmd", "clean", "install")
    } else {
        workingDir = File("../service-lib")
        commandLine = listOf("./mvnw", "clean", "install")
    }
}

task("checkoutMaster") {
    description = "checkouts to master branch"
    doFirst {
        repos.forEach {
            if (file(it.value.first).exists()) {
                val project = Grgit.open { dir = "$rootDir/${it.value.first}"; }
                project.checkout(mapOf(Pair("branch","master")))
            }
        }
    }
}

task("pullAll") {
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

tasks.register("deployAll") {
    dependsOn("deployNews", "deployProject", "deployUser", "deployIssue", "deployGateway")
}

tasks.register("deployGateway") {
    dependsOn("buildGateway")
    doLast {
        exec {
            commandLine = listOf("docker-compose", "stop", "gateway")
        }
        exec {
            commandLine = listOf("docker-compose", "build", "gateway")
        }
        exec {
            commandLine = listOf("docker-compose", "start", "gateway")
        }
    }
}

tasks.register("deployNews") {
    dependsOn("buildNewsService")
    doLast {
        exec {
            commandLine = listOf("docker-compose", "stop", "news")
        }
        exec {
            commandLine = listOf("docker-compose", "build", "news")
        }
        exec {
            commandLine = listOf("docker-compose", "start", "news")
        }
    }
}

tasks.register("deployProject") {
    dependsOn("buildProjectService")
    doLast {
        exec {
            commandLine = listOf("docker-compose", "stop", "project")
        }
        exec {
            commandLine = listOf("docker-compose", "build", "project")
        }
        exec {
            commandLine = listOf("docker-compose", "start", "project")
        }
    }
}

tasks.register("deployIssue") {
    dependsOn("buildIssueService")
    doLast {

        exec {
            commandLine = listOf("docker-compose", "stop", "issue")
        }
        exec {
            commandLine = listOf("docker-compose", "build", "issue")
        }
        exec {
            commandLine = listOf("docker-compose", "start", "issue")
        }
    }
}

tasks.register("deployUser") {
    dependsOn("buildUserService")
    doLast {

        exec {
            commandLine = listOf("docker-compose", "stop", "user")
        }
        exec {
            commandLine = listOf("docker-compose", "build", "user")
        }
        exec {
            commandLine = listOf("docker-compose", "start", "user")
        }
    }
}

