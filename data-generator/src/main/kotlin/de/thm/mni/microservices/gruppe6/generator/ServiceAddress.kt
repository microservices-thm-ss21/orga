package de.thm.mni.microservices.gruppe6.generator

enum class ServiceAddress(val host: String, val port: Int, val uri: String) {

    GATEWAY("localhost", 8069, ""),
    ISSUE("localhost", 8081, "api/issues"),
    PROJECT("localhost", 8082, "api/projects"),
    USER("localhost", 8083, "api/users"),
    NEWS("localhost", 8084, "api/news"),;

    override fun toString(): String {
        return "http://$host:$port/$uri"
    }


}