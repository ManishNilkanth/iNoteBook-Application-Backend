package com.Study.inotebook.Payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class NoteRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "content is required")
    private String content;

    @NotNull(message = "Field isFavorite is required")
    private Boolean isFavorite;

    @NotNull(message = "Field isArchived is required")
    private Boolean isArchived;

    @NotBlank(message = "userId is required")
    private Long userId;

    @NotBlank(message = "notebook is required")
    private Long notebookId;
}
