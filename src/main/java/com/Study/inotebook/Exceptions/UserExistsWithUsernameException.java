package com.Study.inotebook.Exceptions;

public class UserExistsWithUsernameException extends RuntimeException {
    public UserExistsWithUsernameException(String message) {
        super(message);
    }
}
