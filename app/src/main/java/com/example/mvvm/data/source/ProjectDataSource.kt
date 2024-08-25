package com.example.mvvm.data.source

import com.example.mvvm.data.source.api.MyApi
import com.example.mvvm.data.source.api.model.request.NewAttachmentRequest
import com.example.mvvm.data.source.api.model.request.NewProjectRequest
import com.example.mvvm.data.source.api.model.request.NewReportRequest
import com.example.mvvm.datacore.BaseDataSource
import com.example.mvvm.datacore.DataResult
import com.example.mvvm.domain.Document
import com.example.mvvm.domain.Project
import com.example.mvvm.domain.ProjectState
import com.example.mvvm.domain.Researcher
import com.example.mvvm.domain.ResearcherReport
import javax.inject.Inject

interface ProjectDataSource {
    suspend fun getInChargeResearcher(userId: Long): DataResult<Project?>
    suspend fun getInChargeSupervisor(userId: Long): DataResult<List<Project>>
    suspend fun getAllProject(): DataResult<List<Project>>
    suspend fun addProject(project: Project): DataResult<Project>
    suspend fun addResearcherToProject(projectId: Long, researcherEmail: String): DataResult<Researcher>
    suspend fun addReport(report: ResearcherReport, projectId: Long): DataResult<Boolean>
    suspend fun setApprove(projectId: Long): DataResult<Project>
    suspend fun setUnderReview(projectId: Long): DataResult<Project>
    suspend fun setPauseOrResume(projectId: Long): DataResult<Project>
    suspend fun setCancel(projectId: Long): DataResult<Project>

    suspend fun addAttachmentToProject(projectId: Long, name: String, url: String): DataResult<Document>
    suspend fun addAttachmentToReport(reportId: Long, name: String, url: String): DataResult<Document>
}

class ProjectDataSourceImpl
@Inject constructor(val api: MyApi) : ProjectDataSource, BaseDataSource() {
    override suspend fun getInChargeResearcher(userId: Long): DataResult<Project?> {
        println("[DEBUG] userId: $userId")
        val response = api.getAllProject()
        if (response.isSuccessful) {
            val allProjects = response.body()?.map { it.toProject() }
            println("[DEBUG] allProjects: $allProjects")

            if (allProjects.isNullOrEmpty()) {
                return DataResult.Error(Exception("No project found"))
            }

            val researcherProjects = allProjects.filter { it.state !in listOf(ProjectState.NEW, ProjectState.COMPLETED) }
                .find { it1 -> it1.researcher?.map { project -> project.id }?.contains(userId) == true }
            if (researcherProjects == null) {
                return DataResult.Error(Exception("No In-Charge project found"))
            }

            return DataResult.Success(researcherProjects)
        }
        return DataResult.Error(Exception("No project found"))
    }

    override suspend fun getInChargeSupervisor(userId: Long): DataResult<List<Project>> {
        println("[DEBUG] userId: $userId")
        val response = api.getAllProject()
        if (response.isSuccessful) {
            val allProjects = response.body()?.map { it.toProject() }

            if (allProjects.isNullOrEmpty()) {
                return DataResult.Error(Exception("No project found"))
            }

            println("[DEBUG] allProjects: $allProjects")

            val supervisorProjects =
                allProjects.filter { it.state !in listOf(ProjectState.NEW, ProjectState.COMPLETED) }.filter { it1 -> it1.supervisor?.id == userId }
            if (supervisorProjects.isEmpty()) {
                return DataResult.Error(Exception("No In-charge project found"))
            }

            return DataResult.Success(supervisorProjects)
        }
        return DataResult.Error(Exception("No project found"))
    }

    override suspend fun getAllProject(): DataResult<List<Project>> {
        val response = api.getAllProject()
        if (response.isSuccessful) {
            val allProjects = response.body()?.map { it.toProject() }

            println("[DEBUG] allProjects: $allProjects")

            if (allProjects.isNullOrEmpty()) {
                return DataResult.Error(Exception("No project found"))
            }

            return DataResult.Success(allProjects)
        }
        return DataResult.Error(Exception("No project found"))
    }

    override suspend fun addProject(project: Project): DataResult<Project> {
        val projectRequest = NewProjectRequest(
            name = project.title ?: "Unnamed Research", description = project.description ?: "No description", supervisorId = project.supervisor?.id ?: 0
        )
        val response = api.addProject(projectRequest)

        if (response.isSuccessful) {
            println("[DEBUG] addProject: ${response.body()}")
            val r = response.body()

            if (r != null) {
                return DataResult.Success(r.successfully.toProject())
            }

            return DataResult.Error(Exception("Create Fail"))
        }
        return DataResult.Error(Exception("No project found"))
    }

    override suspend fun addResearcherToProject(projectId: Long, researcherEmail: String): DataResult<Researcher> {
        val response = api.addProject(projectId, researcherEmail)
        if (response.isSuccessful) {
            val r = response.body()
            if (r != null) {
                val researcher = r.successfully.getResearcher()
                return DataResult.Success(researcher)
            }
            return DataResult.Error(Exception("Add Researcher to project Fail"))
        }
        return DataResult.Error(Exception("No project found"))
    }

    override suspend fun addReport(report: ResearcherReport, projectId: Long): DataResult<Boolean> {
        val reportRequest = NewReportRequest(
            report.content ?: "No content", report.file?.firstOrNull()?.url, projectId
        )

        val response = api.addReport(reportRequest, projectId)
        if (response.isSuccessful) {
            println("[DEBUG] addReport: ${response.body()}")
            return DataResult.Success(true)
        }
        return DataResult.Error(Exception("Cannot add report"))
    }

    override suspend fun setApprove(projectId: Long): DataResult<Project> {
        val response = api.setApprove(projectId)
        if (response.isSuccessful) {
            val r = response.body()
            if (r != null) {
                return DataResult.Success(r.successfully.toProject())
            }
            return DataResult.Error(Exception("Set Approve Fail"))
        }
        return DataResult.Error(Exception("No project found"))
    }

    override suspend fun setUnderReview(projectId: Long): DataResult<Project> {
        val response = api.setUnderReview(projectId)

        if (response.isSuccessful) {
            val r = response.body()
            if (r != null) {
                return DataResult.Success(r.successfully.toProject())
            }
            return DataResult.Error(Exception("Set Under Review Fail"))
        }
        return DataResult.Error(Exception("No project found"))
    }

    override suspend fun setPauseOrResume(projectId: Long): DataResult<Project> {
        val response = api.setPauseOrResume(projectId)
        if (response.isSuccessful) {
            val r = response.body()
            if (r != null) {
                return DataResult.Success(r.successfully.toProject())
            }
            return DataResult.Error(Exception("Set Pause Or Resume Fail"))
        }
        return DataResult.Error(Exception("No project found"))
    }

    override suspend fun setCancel(projectId: Long): DataResult<Project> {
        val response = api.setCancel(projectId)

        if (response.isSuccessful) {
            val r = response.body()
            if (r != null) {
                return DataResult.Success(r.successfully.toProject())
            }
            return DataResult.Error(Exception("Set Cancel Fail"))
        }
        return DataResult.Error(Exception("No project found"))
    }

    override suspend fun addAttachmentToProject(projectId: Long, name: String, url: String): DataResult<Document> {
        val response = api.addAttachmentToProject(projectId, NewAttachmentRequest(name, url))
        if (response.isSuccessful) {
            val r = response.body()
            if (r != null) {
                return DataResult.Success(r.successfully)
            }
            return DataResult.Error(Exception("Add Attachment Fail"))
        }
        return DataResult.Error(Exception("No project found"))
    }

    override suspend fun addAttachmentToReport(reportId: Long, name: String, url: String): DataResult<Document> {
        val response = api.addAttachmentToReport(reportId, NewAttachmentRequest(name, url))
        if (response.isSuccessful) {
            val r = response.body()
            if (r != null) {
                return DataResult.Success(r.successfully)
            }
            return DataResult.Error(Exception("Add Attachment Fail"))
        }
        return DataResult.Error(Exception("No project found"))
    }
}
