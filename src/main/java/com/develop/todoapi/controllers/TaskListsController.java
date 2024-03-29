package com.develop.todoapi.controllers;

import ch.qos.logback.classic.Logger;
import com.develop.todoapi.constants.ToDoAPIConstants;
import com.develop.todoapi.exceptionhandle.ToDoAppException;
import com.develop.todoapi.models.requests.CreateTaskListRequest;
import com.develop.todoapi.models.requests.UpdateTaskListRequest;
import com.develop.todoapi.models.responses.TaskListResponse;
import com.develop.todoapi.services.TaskListsService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskListsController {

    private static final Logger log = (Logger) LoggerFactory.getLogger(TaskListsController.class);

    @Autowired
    private TaskListsService taskListsService;

    @PostMapping(ToDoAPIConstants.API + "/" + ToDoAPIConstants.VERSION_1 + "/" + ToDoAPIConstants.TASKLISTS_ENDPOINT)
    public TaskListResponse createTaskList(@RequestBody(required = true) CreateTaskListRequest createTaskListRequest) throws ToDoAppException {
        try {
            log.info("Incoming request for task list creation.");
            return taskListsService.createTaskList(createTaskListRequest);
        } finally {
            log.info("Processing for task list creation request finished.");
        }
    }

    @PutMapping(ToDoAPIConstants.API + "/" + ToDoAPIConstants.VERSION_1 + "/" + ToDoAPIConstants.TASKLISTS_ENDPOINT)
    public TaskListResponse updateTaskList(@RequestBody(required = true) UpdateTaskListRequest updateTaskListRequest) throws ToDoAppException {
        try {
            log.info("Incoming request for task list update.");
            return taskListsService.updateTaskList(updateTaskListRequest);
        } finally {
            log.info("Processing for task list update request finished.");
        }
    }

    @GetMapping(ToDoAPIConstants.API + "/" + ToDoAPIConstants.VERSION_1 + "/" + ToDoAPIConstants.TASKLISTS_ENDPOINT + "/" + "{task_list_uuid}")
    public TaskListResponse getTaskList(@PathVariable(name = "task_list_uuid", required = true) String taskListUuid) throws ToDoAppException {
        try {
            log.info("Incoming request for task list fetch.");
            return taskListsService.getTaskList(taskListUuid);
        } finally {
            log.info("Processing for task list get request finished.");
        }
    }

    @DeleteMapping(ToDoAPIConstants.API + "/" + ToDoAPIConstants.VERSION_1 + "/" + ToDoAPIConstants.TASKLISTS_ENDPOINT + "/" + "{task_list_uuid}")
    public void deleteTaskList(@PathVariable(name = "task_list_uuid", required = true) String taskListUuid) throws ToDoAppException {
        try {
            log.info("Incoming request for task list delete.");
            taskListsService.deleteTaskList(taskListUuid);
        } finally {
            log.info("Processing for task list delete request finished.");
        }
    }
}
