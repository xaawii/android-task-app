package com.example.taskapp.task.mappers

import com.example.taskapp.task.data.remote.dto.TaskRequest
import com.example.taskapp.task.data.remote.dto.TaskResponse
import com.example.taskapp.task.domain.models.TaskModel
import javax.inject.Inject

class TaskDtoMapper @Inject constructor() {
    fun fromResponseToDomain(taskResponse: TaskResponse): TaskModel {
        return TaskModel(
            taskResponse.id,
            taskResponse.title,
            taskResponse.description,
            taskResponse.createDate,
            taskResponse.updateDate,
            taskResponse.dueDate,
            taskResponse.status,
            taskResponse.userId
        )
    }

    fun fromDomainToRequest(taskModel: TaskModel): TaskRequest {
        return TaskRequest(
            title = taskModel.title,
            description = taskModel.description,
            dueDate = taskModel.dueDate,
            status = taskModel.status
        )
    }

    fun fromResponseListToDomainList(taskResponseList: List<TaskResponse>): List<TaskModel> {
        return taskResponseList.map { fromResponseToDomain(it) }
    }

    fun fromDomainLisToRequestLis(taskModelList: List<TaskModel>): List<TaskRequest> {
        return taskModelList.map { fromDomainToRequest(it) }
    }


}