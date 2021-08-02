package de.thm.mni.microservices.gruppe6.generator.classes.userService

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class User(
    var id: UUID? = null,
    var username: String,
    var password: String,
    var name: String,
    var lastName: String,
    var email: String,
    var dateOfBirth: LocalDate,
    var createTime: LocalDateTime,
    var globalRole: String,
    var lastLogin: LocalDateTime?
) {
    constructor(userDTO: UserDTO) : this(
        null,
        userDTO.username!!,
        userDTO.password!!,
        userDTO.name!!,
        userDTO.lastName!!,
        userDTO.email!!,
        userDTO.dateOfBirth!!,
        LocalDateTime.now(),
        userDTO.globalRole!!.name,
        null
    )
}

