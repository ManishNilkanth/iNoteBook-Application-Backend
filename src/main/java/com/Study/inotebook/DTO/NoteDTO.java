package com.Study.inotebook.DTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NoteDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "content is required")
    private String content;

    private Boolean isFavorite;

    private Boolean isArchived;

    @NotBlank(message = "userId is required")
    private Long userId;

    @NotBlank(message = "notebook is required")
    private Long notebookId;
}
