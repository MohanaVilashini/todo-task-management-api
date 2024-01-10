package com.develop.todoapi.validations.tasklists;

import com.develop.todoapi.exceptionhandle.ToDoAppErrorMessages;
import com.develop.todoapi.exceptionhandle.ToDoAppErrors;
import com.develop.todoapi.exceptionhandle.ToDoAppException;
import com.develop.todoapi.validations.Validator;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
public class GetTaskListsValidator implements Validator {


    @Override
    public <T> void validate(T object) throws ToDoAppException {
        String taskListUuid = (String) object;
        validateMandatory(taskListUuid);
        validateUuid(taskListUuid);
    }

    private void validateMandatory(String taskListUuid) throws ToDoAppException {
        if (Objects.isNull(taskListUuid) || taskListUuid.isBlank() || taskListUuid.isEmpty()) {
            throw new ToDoAppException(ToDoAppErrorMessages.UUID_MANDATORY_FIELD_MESSAGE, ToDoAppErrors.TO_DO_APP_ERROR_001);
        }
    }

    private void validateUuid(String uuid) throws ToDoAppException {
        try {
            UUID isValidUUID = UUID.fromString(uuid);
        } catch (IllegalArgumentException  ex) {
            throw new ToDoAppException(ToDoAppErrorMessages.UUID_INVALID_MESSAGE, ToDoAppErrors.TO_DO_APP_ERROR_001);
        }
    }


}
