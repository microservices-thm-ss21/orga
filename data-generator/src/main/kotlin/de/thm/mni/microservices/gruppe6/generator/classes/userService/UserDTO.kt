package de.thm.mni.microservices.gruppe6.generator.classes.userService

import java.time.LocalDate

/**
 * DTO = Data Transport Object
 */
class UserDTO {
    var username: String? = null
    var password: String? = null
    var name: String? = null
    var lastName: String? = null
    var email: String? = null
    var dateOfBirth: LocalDate? = null
    var globalRole: GlobalRole? = null
}
