package de.thm.mni.microservices.gruppe6.generator

enum class Service(val host: String, val port: Int, val uri: String) {

    ISSUE("localhost", 8081, "api/issues"),
    PROJECT("localhost", 8082, "api/projects"),
    USER("localhost", 8083, "api/users"),
    NEWS("localhost", 8084, "api/news"),

}