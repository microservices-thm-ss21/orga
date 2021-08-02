package de.thm.mni.microservices.gruppe6.generator.classes.projectService

import java.time.LocalDateTime
import java.util.*

data class Project(
    var id: UUID?,
    var name: String,
    var creatorId: UUID?,
    var createTime: LocalDateTime,
) {
    constructor(projectName: String, creatorId: UUID) : this(
        null,
        projectName,
        creatorId,
        LocalDateTime.now()
    )
}

