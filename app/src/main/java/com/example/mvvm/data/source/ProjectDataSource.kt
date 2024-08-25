package com.example.mvvm.data.source

import com.example.mvvm.data.source.api.MyApi
import com.example.mvvm.datacore.BaseDataSource
import com.example.mvvm.datacore.DataResult
import com.example.mvvm.domain.Project
import com.example.mvvm.domain.ProjectState
import javax.inject.Inject

interface ProjectDataSource {
    suspend fun getInChargeResearcher(userId: Long): DataResult<Project?>
    suspend fun getInChargeSupervisor(userId: Long): DataResult<List<Project>>
    suspend fun getAllProject(): DataResult<List<Project>>
}

class ProjectDataSourceImpl
    @Inject
    constructor(val api: MyApi) : ProjectDataSource, BaseDataSource() {
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
                    return DataResult.Error(Exception("No project found"))
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

                val supervisorProjects = allProjects.filter { it.state !in listOf(ProjectState.NEW, ProjectState.COMPLETED) }
                    .filter { it1 -> it1.supervisor?.id == userId }
                if (supervisorProjects.isEmpty()) {
                    return DataResult.Error(Exception("No project found"))
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
    }
