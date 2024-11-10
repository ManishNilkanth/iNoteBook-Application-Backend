package com.Study.inotebook.Exceptions;

public class NotebookNotFoundException extends RuntimeException {
    public NotebookNotFoundException(String message) {
        super(message);
    }
}
