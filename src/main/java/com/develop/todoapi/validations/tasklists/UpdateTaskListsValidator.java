package com.develop.todoapi.validations.tasklists;

import ch.qos.logback.classic.Logger;
import com.develop.todoapi.exceptionhandle.ToDoAppErrorMessages;
import com.develop.todoapi.exceptionhandle.ToDoAppErrors;
import com.develop.todoapi.exceptionhandle.ToDoAppException;
import com.develop.todoapi.models.requests.UpdateTaskListRequest;
import com.develop.todoapi.validations.Validator;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

import static com.develop.todoapi.constants.ToDoAPIConstants.MAXIMUM_LENGTH_FOR_DESCRIPTION;

@Component
public class UpdateTaskListsValidator implements Validator {

    private static final Logger log = (Logger) LoggerFactory.getLogger(UpdateTaskListsValidator.class);

    @Override
    public <T> void validate(T object) throws ToDoAppException {
        UpdateTaskListRequest updateTaskListRequest = (UpdateTaskListRequest) object;
        validateMandatory(updateTaskListRequest);
        validateUuid(updateTaskListRequest.getUuid());
        validateLengths(updateTaskListRequest.getDescription());
        log.info("Received request object for task list update has been validated.");
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

    private void validateMandatory(UpdateTaskListRequest updateTaskListRequest) throws ToDoAppException {
        if (Objects.isNull(updateTaskListRequest.getUuid()) || updateTaskListRequest.getUuid().isBlank() || updateTaskListRequest.getUuid().isEmpty()) {
            throw new ToDoAppException(ToDoAppErrorMessages.UUID_MANDATORY_FIELD_MESSAGE, ToDoAppErrors.TO_DO_APP_ERROR_001);
        }

        if (Objects.isNull(updateTaskListRequest.getDescription())) {
            throw new ToDoAppException(ToDoAppErrorMessages.DESCRIPTION_MANDATORY_FIELD_MESSAGE, ToDoAppErrors.TO_DO_APP_ERROR_001);
        }
    }

}
