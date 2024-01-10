package com.develop.todoapi.validations;

import com.develop.todoapi.exceptionhandle.ToDoAppException;

public interface Validator {
    public <T> void validate(T object) throws ToDoAppException;
}
