package de.thm.mni.microservices.gruppe6.generator.classes.projectService


enum class ProjectRole {
    USER,
    ADMIN,
    SUPPORT;

    fun getAuthority(): String = this.name
}