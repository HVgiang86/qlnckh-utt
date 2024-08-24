package com.example.mvvm.data

import com.example.mvvm.domain.Project
import com.example.mvvm.domain.ResearcherReport
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    suspend fun getInChargeResearcher(userId: Long): Flow<Project?>
    suspend fun getInChargeSupervisor(userId: Long): Flow<List<Project>>
    suspend fun getAllProject(): Flow<List<Project>>
    suspend fun getProjectHistorySupervisor(userId: Long): Flow<List<Project>>
    suspend fun getProjectHistoryResearcher(userId: Long): Flow<List<Project>>

    suspend fun addResearcherReport(report: ResearcherReport, projectId: Int)
    suspend fun addProject(project: Project)
}
