package com.develop.todoapi.validations.tasks;

import com.develop.todoapi.exceptionhandle.ToDoAppErrorMessages;
import com.develop.todoapi.exceptionhandle.ToDoAppErrors;
import com.develop.todoapi.exceptionhandle.ToDoAppException;
import com.develop.todoapi.models.requests.CreateTaskRequest;
import com.develop.todoapi.validations.Validator;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

import static com.develop.todoapi.constants.ToDoAPIConstants.MAXIMUM_LENGTH_FOR_DESCRIPTION;
import static com.develop.todoapi.constants.ToDoAPIConstants.MAXIMUM_LENGTH_FOR_NAME;

@Component
public class CreateTaskValidator implements Validator {


    @Override
    public <T> void validate(T object) throws ToDoAppException {
        CreateTaskRequest createTaskRequest = (CreateTaskRequest) object;
        validateMandatory(createTaskRequest);
        validateLengths(createTaskRequest);
        validateUuid(createTaskRequest.getTaskListUuid());
    }

    private void validateUuid(String taskListUuid) throws ToDoAppException {
        try {
            UUID isValidUUID = UUID.fromString(taskListUuid);
        } catch (IllegalArgumentException  ex) {
            throw new ToDoAppException(ToDoAppErrorMessages.UUID_INVALID_MESSAGE, ToDoAppErrors.TO_DO_APP_ERROR_001);
        }
    }

    private void validateLengths(CreateTaskRequest createTaskRequest) throws ToDoAppException {
        if (createTaskRequest.getName().length() > MAXIMUM_LENGTH_FOR_NAME) {
            throw new ToDoAppException(ToDoAppErrorMessages.NAME_MAX_LENGTH_MESSAGE, ToDoAppErrors.TO_DO_APP_ERROR_001);
        }

        if (Objects.nonNull(createTaskRequest.getDescription()) && createTaskRequest.getDescription().length() > MAXIMUM_LENGTH_FOR_DESCRIPTION) {
            throw new ToDoAppException(ToDoAppErrorMessages.DESCRIPTION_MAX_LENGTH_MESSAGE, ToDoAppErrors.TO_DO_APP_ERROR_001);
        }
    }

    private void validateMandatory(CreateTaskRequest createTaskRequest) throws ToDoAppException {
        if (Objects.isNull(createTaskRequest.getName()) || createTaskRequest.getName().isBlank() || createTaskRequest.getName().isEmpty()) {
            throw new ToDoAppException(ToDoAppErrorMessages.NAME_MANDATORY_FIELD_MESSAGE, ToDoAppErrors.TO_DO_APP_ERROR_001);
        }

        if (Objects.isNull(createTaskRequest.getDescription())) {
            throw new ToDoAppException(ToDoAppErrorMessages.DESCRIPTION_MANDATORY_FIELD_MESSAGE, ToDoAppErrors.TO_DO_APP_ERROR_001);
        }

        if (Objects.isNull(createTaskRequest.getTaskListUuid()) || createTaskRequest.getTaskListUuid().isBlank() || createTaskRequest.getTaskListUuid().isEmpty()) {
            throw new ToDoAppException(ToDoAppErrorMessages.UUID_MANDATORY_FIELD_MESSAGE, ToDoAppErrors.TO_DO_APP_ERROR_001);
        }
    }
}
