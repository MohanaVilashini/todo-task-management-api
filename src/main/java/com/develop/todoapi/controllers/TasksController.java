package com.develop.todoapi.controllers;

import ch.qos.logback.classic.Logger;
import com.develop.todoapi.constants.ToDoAPIConstants;
import com.develop.todoapi.exceptionhandle.ToDoAppException;
import com.develop.todoapi.models.requests.CreateTaskRequest;
import com.develop.todoapi.models.requests.UpdateTaskRequest;
import com.develop.todoapi.models.responses.TaskResponse;
import com.develop.todoapi.services.TasksService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TasksController {

    private static final Logger log = (Logger) LoggerFactory.getLogger(TasksController.class);

    @Autowired
    private TasksService tasksService;

    @PostMapping(ToDoAPIConstants.API + "/" + ToDoAPIConstants.VERSION_1 + "/" + ToDoAPIConstants.TASKS_ENDPOINT)
    public TaskResponse createTask(@RequestBody(required = true) CreateTaskRequest createTaskRequest) throws ToDoAppException {
        try {
            log.info("Incoming request for task creation.");
            return tasksService.createTask(createTaskRequest);
        } finally {
            log.info("Processing for task creation request finished.");
        }
    }

    @PutMapping(ToDoAPIConstants.API + "/" + ToDoAPIConstants.VERSION_1 + "/" + ToDoAPIConstants.TASKS_ENDPOINT)
    public TaskResponse updateTask(@RequestBody(required = true) UpdateTaskRequest updateTaskRequest) throws ToDoAppException {
        try {
            log.info("Incoming request for task update.");
            return tasksService.updateTask(updateTaskRequest);
        } finally {
            log.info("Processing for task update request finished.");
        }
    }

    @GetMapping(ToDoAPIConstants.API + "/" + ToDoAPIConstants.VERSION_1 + "/" + ToDoAPIConstants.TASKS_ENDPOINT + "/" + "{task_uuid}")
    public TaskResponse fetchTask(@PathVariable(name = "task_uuid", required = true) String taskUuid) throws ToDoAppException {
        try {
            log.info("Incoming request for task fetch.");
            return tasksService.getTask(taskUuid);
        } finally {
            log.info("Processing for task fetch request finished.");
        }
    }

    @DeleteMapping(ToDoAPIConstants.API + "/" + ToDoAPIConstants.VERSION_1 + "/" + ToDoAPIConstants.TASKS_ENDPOINT + "/" + "{task_uuid}")
    public void deleteTask(@PathVariable(name = "task_uuid", required = true) String taskUuid) throws ToDoAppException {
        try {
            log.info("Incoming request for task delete.");
            tasksService.deleteTask(taskUuid);
        } finally {
            log.info("Processing for task delete request finished.");
        }
    }

    @GetMapping(ToDoAPIConstants.API + "/" + ToDoAPIConstants.VERSION_1 + "/" + ToDoAPIConstants.TASKS_ENDPOINT + "/" + ToDoAPIConstants.TASKLIST_ENDPOINT + "/" + "{tasklist_uuid}")
    public List<TaskResponse> fetchTasks(@PathVariable(name = "tasklist_uuid", required = true) String taskListUuid) throws ToDoAppException {
        try {
            log.info("Incoming request for tasks fetch.");
            return tasksService.getTasks(taskListUuid);
        } finally {
            log.info("Processing for task fetch request finished.");
        }
    }
}
