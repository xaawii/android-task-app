package com.example.taskapp.task.mappers

import com.example.taskapp.task.domain.models.TaskModel
import com.example.taskapp.task.presentation.model.TaskUIModel

class TaskUIModelMapper {

    fun fromDomainToUI(taskModel: TaskModel): TaskUIModel {
        return TaskUIModel(
            id = taskModel.id,
            title = taskModel.title,
            description = taskModel.description,
            createDate = taskModel.createDate,
            updateDate = taskModel.updateDate,
            dueDate = taskModel.dueDate,
            status = taskModel.status,
            userId = taskModel.userId
        )
    }

    fun fromUItoDomain(taskUIModel: TaskUIModel): TaskModel {
        return TaskModel(
            id = taskUIModel.id,
            title = taskUIModel.title,
            description = taskUIModel.description,
            createDate = taskUIModel.createDate,
            updateDate = taskUIModel.updateDate,
            dueDate = taskUIModel.dueDate,
            status = taskUIModel.status,
            userId = taskUIModel.userId
        )
    }

    fun fromDomainListToUIList(taskModelList: List<TaskModel>): List<TaskUIModel> {
        return taskModelList.map { fromDomainToUI(it) }
    }

    fun fromUIListToDomainList(taskUIModelList: List<TaskUIModel>): List<TaskModel> {
        return taskUIModelList.map { fromUItoDomain(it) }
    }


}