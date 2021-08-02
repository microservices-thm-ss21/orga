package de.thm.mni.microservices.gruppe6.generator.classes.issueService


import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class Issue(
    var id: UUID? = null,
    var projectId: UUID,
    var message: String,
    var assignedUserId: UUID? = null,
    var creatorId: UUID,
    var deadline: LocalDate? = null,
    var createTime: LocalDateTime,
    var updateTime: LocalDateTime? = null,
    var status: String = IssueStatus.OPEN.name
) {
    constructor(issueDTO: IssueDTO, creatorId: UUID) : this(
        null,
        issueDTO.projectId!!,
        issueDTO.message!!,
        issueDTO.assignedUserId,
        creatorId,
        issueDTO.deadline,
        LocalDateTime.now(),
        null
    )

}
