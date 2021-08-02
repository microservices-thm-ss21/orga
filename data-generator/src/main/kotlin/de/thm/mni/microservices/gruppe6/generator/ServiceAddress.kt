package de.thm.mni.microservices.gruppe6.generator

enum class ServiceAddress(val host: String, val port: Int, val uri: String) {

    LOGIN("localhost", 8069, "login"),
    GATEWAY("localhost", 8069, ""),
    ISSUE("localhost", 8069, "api/issues"),
    PROJECT("localhost", 8082, "api/projects"),
    USER("localhost", 8069, "api/users"),
    NEWS("localhost", 8069, "api/news"),;

    override fun toString(): String {
        return "http://$host:$port/$uri"
    }


}