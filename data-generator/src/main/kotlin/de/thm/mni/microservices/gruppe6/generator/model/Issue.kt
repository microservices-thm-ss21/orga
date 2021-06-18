package de.thm.mni.microservices.gruppe6.generator.model


import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class Issue(
    var id: UUID? = null,
    var projectId: UUID,
    var message: String,
    var assignedUserId: UUID? = null,
    @JsonFormat(pattern = "dd.MM.yyyy")
    var deadline: LocalDate? = null,
    var createTime: LocalDateTime,
    var updateTime: LocalDateTime? = null
)