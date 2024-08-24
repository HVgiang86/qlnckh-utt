package com.example.mvvm.data.repository

import com.example.mvvm.data.ProjectRepository
import com.example.mvvm.datacore.BaseRepository
import com.example.mvvm.domain.Project
import com.example.mvvm.domain.ResearcherReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber

class ProjectRepositoryImpl : ProjectRepository, BaseRepository() {

    override suspend fun getInChargeResearcher(userId: Long): Flow<Project?> {
        Timber.d("get in charge")
        return flowOf(null)
    }

    override suspend fun getInChargeSupervisor(userId: Long): Flow<List<Project>> {
        return flowOf(emptyList())
    }

    override suspend fun getAllProject(): Flow<List<Project>> {
        return flowOf(emptyList())
    }

    override suspend fun getProjectHistorySupervisor(userId: Long): Flow<List<Project>> {
        return flowOf(emptyList())
    }

    override suspend fun getProjectHistoryResearcher(userId: Long): Flow<List<Project>> {
        return flowOf(emptyList())
    }

    override suspend fun addResearcherReport(report: ResearcherReport, projectId: Int) {
    }

    override suspend fun addProject(project: Project) {
    }
}
