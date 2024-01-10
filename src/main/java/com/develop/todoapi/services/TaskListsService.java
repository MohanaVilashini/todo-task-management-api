package com.develop.todoapi.services;

import ch.qos.logback.classic.Logger;
import com.develop.todoapi.models.responses.TaskListResponse;
import com.develop.todoapi.exceptionhandle.ToDoAppException;
import com.develop.todoapi.models.TaskLists;
import com.develop.todoapi.models.requests.CreateTaskListRequest;
import com.develop.todoapi.models.requests.UpdateTaskListRequest;
import com.develop.todoapi.repositories.TaskListsRepository;
import com.develop.todoapi.validations.tasklists.CreateTaskListsValidator;
import com.develop.todoapi.validations.tasklists.GetTaskListsValidator;
import com.develop.todoapi.validations.tasklists.UpdateTaskListsValidator;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static com.develop.todoapi.exceptionhandle.ToDoAppErrorMessages.DUPLICATE_ENTRY_FOUND;
import static com.develop.todoapi.exceptionhandle.ToDoAppErrorMessages.TASK_LIST_NOT_FOUND_MESSAGE;
import static com.develop.todoapi.exceptionhandle.ToDoAppErrors.TO_DO_APP_ERROR_002;
import static com.develop.todoapi.exceptionhandle.ToDoAppErrors.TO_DO_APP_ERROR_004;

@Service
public class TaskListsService {

    private static final Logger log = (Logger) LoggerFactory.getLogger(TaskListsService.class);

    @Autowired
    private CreateTaskListsValidator createTaskListsValidator;

    @Autowired
    private UpdateTaskListsValidator updateTaskListsValidator;

    @Autowired
    private GetTaskListsValidator getTaskListsValidator;

    @Autowired
    private TaskListsRepository taskListsRepository;

    public TaskListResponse createTaskList(CreateTaskListRequest createTaskListRequest) throws ToDoAppException {
        log.info("Processing request for task list creation.");
        createTaskListsValidator.validate(createTaskListRequest);
        TaskLists taskLists = createTaskListInDB(createTaskListRequest);
        return TaskListResponse.builder()
                .uuid(taskLists.getUuid())
                .name(taskLists.getName())
                .description(taskLists.getDescription())
                .build();
    }

    private TaskLists createTaskListInDB(CreateTaskListRequest createTaskListRequest) throws ToDoAppException {
        checkExistence(createTaskListRequest.getName());
        TaskLists taskList = TaskLists.builder()
                .name(createTaskListRequest.getName())
                .description(createTaskListRequest.getDescription())
                .uuid(UUID.randomUUID())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        TaskLists taskLists = null;
        try {
            taskLists = taskListsRepository.save(taskList);
            log.info("Task list has been created. Returning the newly created object.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return taskLists;
    }

    private void checkExistence(String name) throws ToDoAppException {
        Optional<TaskLists> taskListsOptional = taskListsRepository.findByName(name);
        if (taskListsOptional.isPresent() && !taskListsOptional.get().getIsDeleted()) {
            log.info("Task list with name : " + name + " already exist. Sending error response.");
            throw new ToDoAppException(DUPLICATE_ENTRY_FOUND + name, TO_DO_APP_ERROR_004);
        }
    }

    public TaskListResponse updateTaskList(UpdateTaskListRequest updateTaskListRequest) throws ToDoAppException {
        log.info("Processing request for task list update.");
        updateTaskListsValidator.validate(updateTaskListRequest);
        TaskLists taskLists = updateTaskListInDB(updateTaskListRequest);
        return TaskListResponse.builder()
                .uuid(taskLists.getUuid())
                .name(taskLists.getName())
                .description(taskLists.getDescription())
                .build();
    }

    private TaskLists updateTaskListInDB(UpdateTaskListRequest updateTaskListRequest) throws ToDoAppException {
        TaskLists oldTaskList = checkExistence(UUID.fromString(updateTaskListRequest.getUuid()));
        TaskLists newTaskList = TaskLists.builder()
                .name(oldTaskList.getName())
                .description(updateTaskListRequest.getDescription())
                .uuid(oldTaskList.getUuid())
                .createdAt(oldTaskList.getCreatedAt())
                .updatedAt(new Date())
                .isDeleted(oldTaskList.getIsDeleted())
                .build();
        try {
            newTaskList = taskListsRepository.save(newTaskList);
            log.info("Task list has been updated. Returning the updated object.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return newTaskList;
    }

    private TaskLists checkExistence(UUID uuid) throws ToDoAppException {
        Optional<TaskLists> taskListsOptional = taskListsRepository.findById(uuid);
        if (taskListsOptional.isEmpty() || taskListsOptional.get().getIsDeleted()) {
            log.info("Task list with uuid : " + uuid + " either does not exist or deleted. Sending error response.");
            throw new ToDoAppException(TASK_LIST_NOT_FOUND_MESSAGE, TO_DO_APP_ERROR_002);
        }
        return taskListsOptional.get();
    }

    public TaskListResponse getTaskList(String taskListUuid) throws ToDoAppException {
        log.info("Processing request for task list fetch.");
        getTaskListsValidator.validate(taskListUuid);
        TaskLists taskLists = fetchTaskList(taskListUuid);
        return TaskListResponse.builder()
                .uuid(taskLists.getUuid())
                .name(taskLists.getName())
                .description(taskLists.getDescription())
                .build();
    }

    private TaskLists fetchTaskList(String taskListUuid) throws ToDoAppException {
        Optional<TaskLists> taskListsOptional = Optional.empty();
        try {
            taskListsOptional = taskListsRepository.findById(UUID.fromString(taskListUuid));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (taskListsOptional.isEmpty() || taskListsOptional.get().getIsDeleted()) {
            log.info("Task list with uuid : " + taskListUuid + " either does not exist or deleted. Sending error response.");
            throw new ToDoAppException(TASK_LIST_NOT_FOUND_MESSAGE, TO_DO_APP_ERROR_002);
        }
        return taskListsOptional.get();
    }

    public void deleteTaskList(String taskListUuid) throws ToDoAppException {
        log.info("Processing request for task list delete.");
        getTaskListsValidator.validate(taskListUuid);
        TaskLists taskLists = checkExistence(UUID.fromString(taskListUuid));
        deleteTaskListInDB(taskLists);
    }

    private void deleteTaskListInDB(TaskLists taskLists) {
        try {
            taskLists.setIsDeleted(true);
            taskLists.setUpdatedAt(new Date());
            taskListsRepository.save(taskLists);
            log.info("Task list has been deleted.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
