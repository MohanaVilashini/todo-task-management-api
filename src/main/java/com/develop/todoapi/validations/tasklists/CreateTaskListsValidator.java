package com.develop.todoapi.validations.tasklists;

import ch.qos.logback.classic.Logger;
import com.develop.todoapi.exceptionhandle.ToDoAppErrorMessages;
import com.develop.todoapi.exceptionhandle.ToDoAppErrors;
import com.develop.todoapi.models.requests.CreateTaskListRequest;
import com.develop.todoapi.exceptionhandle.ToDoAppException;
import com.develop.todoapi.validations.Validator;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.develop.todoapi.constants.ToDoAPIConstants.MAXIMUM_LENGTH_FOR_DESCRIPTION;
import static com.develop.todoapi.constants.ToDoAPIConstants.MAXIMUM_LENGTH_FOR_NAME;

@Component
public class CreateTaskListsValidator implements Validator {

    private static final Logger log = (Logger) LoggerFactory.getLogger(CreateTaskListsValidator.class);

    @Override
    public <T> void validate(T object) throws ToDoAppException {
        CreateTaskListRequest createTaskListRequest = (CreateTaskListRequest) object;
        validateMandatory(createTaskListRequest.getName());
        validateLengths(createTaskListRequest);
        log.info("Received request object for task list creation has been validated.");
    }

    private void validateLengths(CreateTaskListRequest createTaskListRequest) throws ToDoAppException{
        if (createTaskListRequest.getName().length() > MAXIMUM_LENGTH_FOR_NAME) {
            throw new ToDoAppException(ToDoAppErrorMessages.NAME_MAX_LENGTH_MESSAGE, ToDoAppErrors.TO_DO_APP_ERROR_001);
        }

        if (Objects.nonNull(createTaskListRequest.getDescription()) && createTaskListRequest.getDescription().length() > MAXIMUM_LENGTH_FOR_DESCRIPTION) {
            throw new ToDoAppException(ToDoAppErrorMessages.DESCRIPTION_MAX_LENGTH_MESSAGE, ToDoAppErrors.TO_DO_APP_ERROR_001);
        }
    }

    private void validateMandatory(String name) throws ToDoAppException {
        if (Objects.isNull(name) || name.isBlank() || name.isEmpty()) {
            throw new ToDoAppException(ToDoAppErrorMessages.NAME_MANDATORY_FIELD_MESSAGE, ToDoAppErrors.TO_DO_APP_ERROR_001);
        }
    }
}
