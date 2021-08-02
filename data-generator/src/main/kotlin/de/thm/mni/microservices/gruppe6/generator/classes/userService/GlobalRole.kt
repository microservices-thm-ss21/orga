package de.thm.mni.microservices.gruppe6.generator.classes.userService


enum class GlobalRole {
    USER,
    ADMIN,
    SUPPORT;

    fun getAuthority(): String = this.name
}