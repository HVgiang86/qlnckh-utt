package com.example.mvvm.data.repository

import com.example.mvvm.data.ProjectRepository
import com.example.mvvm.data.source.ProjectDataSource
import com.example.mvvm.datacore.BaseRepository
import com.example.mvvm.domain.Project
import com.example.mvvm.domain.Researcher
import com.example.mvvm.domain.ResearcherReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ProjectRepositoryImpl
@Inject constructor(val remote: ProjectDataSource) : ProjectRepository, BaseRepository() {

    override suspend fun getInChargeResearcher(userId: Long) = runFlow {
        remote.getInChargeResearcher(userId)
    }

    override suspend fun getInChargeSupervisor(userId: Long) = runFlow {
        remote.getInChargeSupervisor(userId)
    }

    override suspend fun getAllProject() = runFlow {
        remote.getAllProject()
    }

    override suspend fun getProjectHistorySupervisor(userId: Long): Flow<List<Project>> {
        return flowOf(emptyList())
    }

    override suspend fun getProjectHistoryResearcher(userId: Long): Flow<List<Project>> {
        return flowOf(emptyList())
    }

    override suspend fun addResearcherReport(report: ResearcherReport, projectId: Long) {

    }

    override suspend fun addProject(project: Project) = runFlow {
        remote.addProject(project)
    }

    override suspend fun addResearcherToProject(projectId: Long, researcherEmail: String): Flow<Researcher> = runFlow {
        remote.addResearcherToProject(projectId, researcherEmail)
    }

    override suspend fun addReport(report: ResearcherReport, projectId: Long) = runFlow {
        remote.addReport(report, projectId)
    }

    override suspend fun setApprove(projectId: Long) = runFlow {
        remote.setApprove(projectId)
    }

    override suspend fun setUnderReview(projectId: Long) = runFlow {
        remote.setUnderReview(projectId)
    }

    override suspend fun setPauseOrResume(projectId: Long) = runFlow {
        remote.setPauseOrResume(projectId)
    }

    override suspend fun setCancel(projectId: Long) = runFlow {
        remote.setCancel(projectId)
    }

    override suspend fun addAttachmentToProject(projectId: Long, name: String, url: String) = runFlow {
        remote.addAttachmentToProject(projectId, name, url)
    }

    override suspend fun addAttachmentToReport(reportId: Long, name: String, url: String) = runFlow {
        remote.addAttachmentToReport(reportId, name, url)
    }
}
