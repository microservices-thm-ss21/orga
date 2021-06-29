package de.thm.mni.microservices.gruppe6.generator.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class User(
    val id: UUID? = UUID.randomUUID(),
    val username: String,
    val name: String,
    val lastName: String,
    val email: String,
    val dateOfBirth: LocalDate,
    val createTime: LocalDateTime,
    val globalRole: String,
    val lastLogin: LocalDateTime?
) {
    constructor(userDTO: UserDTO): this(
        null
        ,userDTO.username!!
        ,userDTO.name!!
        ,userDTO.lastName!!
        ,userDTO.email!!
        ,userDTO.dateOfBirth!!
        ,LocalDateTime.now()
        ,userDTO.globalRole!!
        ,null
    )

}

