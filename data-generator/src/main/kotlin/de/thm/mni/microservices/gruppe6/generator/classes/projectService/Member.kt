package de.thm.mni.microservices.gruppe6.generator.classes.projectService

import java.util.*

data class Member(
    var id: UUID?,
    var projectId: UUID,
    var userId: UUID,
    var projectRole: String
) {
    constructor(projectId: UUID, userId: UUID, projectRole: ProjectRole) : this(
        null,
        projectId,
        userId,
        projectRole.name
    )
}
