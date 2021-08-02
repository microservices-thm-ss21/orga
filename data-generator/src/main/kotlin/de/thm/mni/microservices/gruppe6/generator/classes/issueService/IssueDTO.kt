package de.thm.mni.microservices.gruppe6.generator.classes.issueService

import java.time.LocalDate
import java.util.*

/**
 * DTO = Data Transport Object
 */
data class IssueDTO(
    var message: String? = null,
    var assignedUserId: UUID? = null,
    var projectId: UUID? = null,
    var deadline: LocalDate? = null,
    var status: IssueStatus? = null
)
