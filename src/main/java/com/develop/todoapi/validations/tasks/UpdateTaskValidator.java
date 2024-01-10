package com.develop.todoapi.validations.tasks;

import ch.qos.logback.classic.Logger;
import com.develop.todoapi.exceptionhandle.ToDoAppErrorMessages;
import com.develop.todoapi.exceptionhandle.ToDoAppErrors;
import com.develop.todoapi.exceptionhandle.ToDoAppException;
import com.develop.todoapi.models.TaskStatus;
import com.develop.todoapi.models.requests.UpdateTaskRequest;
import com.develop.todoapi.validations.Validator;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import static com.develop.todoapi.constants.ToDoAPIConstants.MAXIMUM_LENGTH_FOR_DESCRIPTION;

@Component
public class UpdateTaskValidator implements Validator {

    private static final Logger log = (Logger) LoggerFactory.getLogger(UpdateTaskValidator.class);


    @Override
    public <T> void validate(T object) throws ToDoAppException {
        UpdateTaskRequest updateTaskRequest = (UpdateTaskRequest) object;
        validateMandatory(updateTaskRequest);
        validateUuid(updateTaskRequest.getUuid());
        validateLengths(updateTaskRequest.getDescription());
        validateStatus(updateTaskRequest.getStatus());
        log.info("Received request object for task update has been validated.");
    }

    private void validateStatus(String status) throws ToDoAppException {
        if (Objects.isNull(status)) {
            return;
        }
        TaskStatus taskStatus = Arrays.stream(TaskStatus.values())
                .filter(statusValue -> statusValue.name().equals(status))
                .findFirst()
                .orElse(null);
        if (Objects.isNull(taskStatus)) {
            throw new ToDoAppException(ToDoAppErrorMessages.TASK_STATUS_VALUE_INVALID_MESSAGE, ToDoAppErrors.TO_DO_APP_ERROR_001);
        }
    }

    private void validateLengths(String description) throws ToDoAppException {
        if (Objects.nonNull(description) && description.length() > MAXIMUM_LENGTH_FOR_DESCRIPTION) {
            throw new ToDoAppException(ToDoAppErrorMessages.DESCRIPTION_MAX_LENGTH_MESSAGE, ToDoAppErrors.TO_DO_APP_ERROR_001);
        }
    }

    private void validateUuid(String uuid) throws ToDoAppException {
        try {
            UUID isValidUUID = UUID.fromString(uuid);
        } catch (IllegalArgumentException  ex) {
            throw new ToDoAppException(ToDoAppErrorMessages.UUID_INVALID_MESSAGE, ToDoAppErrors.TO_DO_APP_ERROR_001);
        }
    }

    private void validateMandatory(UpdateTaskRequest updateTaskRequest) throws ToDoAppException {
        if (Objects.isNull(updateTaskRequest.getUuid()) || updateTaskRequest.getUuid().isBlank() || updateTaskRequest.getUuid().isEmpty()) {
            throw new ToDoAppException(ToDoAppErrorMessages.UUID_MANDATORY_FIELD_MESSAGE, ToDoAppErrors.TO_DO_APP_ERROR_001);
        }

        if (Objects.isNull(updateTaskRequest.getDescription()) && Objects.isNull(updateTaskRequest.getStatus())) {

            throw new ToDoAppException(ToDoAppErrorMessages.MISSING_FIELD_FOR_UPDATE, ToDoAppErrors.TO_DO_APP_ERROR_001);
        }
    }
}
