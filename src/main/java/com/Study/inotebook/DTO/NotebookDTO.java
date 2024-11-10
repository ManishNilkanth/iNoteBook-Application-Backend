package com.Study.inotebook.DTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NotebookDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    private Boolean isArchived;

    private Long userId;
}
