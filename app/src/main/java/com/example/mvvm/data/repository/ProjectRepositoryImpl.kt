package com.example.mvvm.data.repository

import com.example.mvvm.data.ProjectRepository
import com.example.mvvm.data.source.ProjectDataSource
import com.example.mvvm.datacore.BaseRepository
import com.example.mvvm.domain.Project
import com.example.mvvm.domain.ResearcherReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ProjectRepositoryImpl
    @Inject
    constructor(val remote: ProjectDataSource) : ProjectRepository, BaseRepository() {

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

        override suspend fun addProject(project: Project) {
        }
    }
