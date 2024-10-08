package com.example.mvvm.data

import com.example.mvvm.data.source.api.model.request.CouncilRequest
import com.example.mvvm.domain.Document
import com.example.mvvm.domain.Project
import com.example.mvvm.domain.Researcher
import com.example.mvvm.domain.ResearcherReport
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    suspend fun getInChargeResearcher(userId: Long): Flow<Project?>
    suspend fun getInChargeSupervisor(userId: Long): Flow<List<Project>>
    suspend fun getAllProject(): Flow<List<Project>>
    suspend fun getProjectHistorySupervisor(userId: Long): Flow<List<Project>>
    suspend fun getProjectHistoryResearcher(userId: Long): Flow<List<Project>>

    suspend fun addResearcherReport(report: ResearcherReport, projectId: Long)
    suspend fun addProject(project: Project): Flow<Project>

    suspend fun addResearcherToProject(projectId: Long, researcherEmail: String): Flow<List<Researcher>>

    suspend fun addReport(report: ResearcherReport, projectId: Long): Flow<Boolean>
    suspend fun setApprove(projectId: Long): Flow<Boolean>
    suspend fun setUnderReview(projectId: Long): Flow<Boolean>
    suspend fun setPauseOrResume(projectId: Long): Flow<Boolean>
    suspend fun setCancel(projectId: Long): Flow<Boolean>

    suspend fun addAttachmentToProject(projectId: Long, name: String, url: String): Flow<Boolean>
    suspend fun addAttachmentToReport(reportId: Long, name: String, url: String): Flow<Boolean>

    suspend fun setCouncil(projectId: Long, council: CouncilRequest): Flow<Boolean>
    suspend fun setProjectTime(projectId: Long, date: String): Flow<Boolean>

    suspend fun setProjectScore(projectId: Long, score: Double): Flow<Boolean>
}
