package com.example.mvvm.data.source

import com.example.mvvm.data.source.api.MyApi
import com.example.mvvm.data.source.api.model.request.NewAttachmentRequest
import com.example.mvvm.data.source.api.model.request.NewProjectRequest
import com.example.mvvm.data.source.api.model.request.NewReportRequest
import com.example.mvvm.data.source.api.model.response.ProfileResponse
import com.example.mvvm.datacore.BaseDataSource
import com.example.mvvm.datacore.DataResult
import com.example.mvvm.domain.Document
import com.example.mvvm.domain.Project
import com.example.mvvm.domain.ProjectResponse
import com.example.mvvm.domain.ResearcherReport
import javax.inject.Inject

interface ProjectDataSource {
    suspend fun getAllProject(): DataResult<List<ProjectResponse>>
    suspend fun addProject(project: Project): DataResult<ProjectResponse>
    suspend fun addResearcherToProject(projectId: Long, researcherEmail: String): DataResult<ProfileResponse>
    suspend fun addReport(report: ResearcherReport, projectId: Long): DataResult<Boolean>
    suspend fun setApprove(projectId: Long): DataResult<Boolean>
    suspend fun setUnderReview(projectId: Long): DataResult<Boolean>
    suspend fun setPauseOrResume(projectId: Long): DataResult<Boolean>
    suspend fun setCancel(projectId: Long): DataResult<Boolean>

    suspend fun addAttachmentToProject(projectId: Long, name: String, url: String): DataResult<Document>
    suspend fun addAttachmentToReport(reportId: Long, name: String, url: String): DataResult<Document>
}

class ProjectDataSourceImpl
    @Inject
    constructor(private val api: MyApi) : ProjectDataSource, BaseDataSource() {

        override suspend fun getAllProject() = returnResult {
            api.getAllProject()
        }

        override suspend fun addProject(project: Project) = returnResult {
            val projectRequest = NewProjectRequest(
                name = project.title ?: "Unnamed Research",
                description = project.description ?: "No description",
                supervisorId = project.supervisor?.id ?: 0,
            )
            api.addProject(projectRequest)
        }

        override suspend fun addResearcherToProject(projectId: Long, researcherEmail: String) = returnResult {
            api.addProject(projectId, researcherEmail)
        }

        override suspend fun addReport(report: ResearcherReport, projectId: Long) = returnIfSuccess {
            val reportRequest = NewReportRequest(
                report.content ?: "No content",
                report.file?.firstOrNull()?.url,
                projectId,
            )

            api.addReport(reportRequest, projectId)
        }

        override suspend fun setApprove(projectId: Long) = returnIfSuccess {
            api.setApprove(projectId)
        }

        override suspend fun setUnderReview(projectId: Long) = returnIfSuccess {
            api.setUnderReview(projectId)
        }

        override suspend fun setPauseOrResume(projectId: Long) = returnIfSuccess {
            api.setPauseOrResume(projectId)
        }

        override suspend fun setCancel(projectId: Long) = returnIfSuccess {
            api.setCancel(projectId)
        }

        override suspend fun addAttachmentToProject(projectId: Long, name: String, url: String) = returnResult {
            api.addAttachmentToProject(projectId, NewAttachmentRequest(name, url))
        }

        override suspend fun addAttachmentToReport(reportId: Long, name: String, url: String) = returnResult {
            api.addAttachmentToReport(reportId, NewAttachmentRequest(name, url))
        }
    }
