package com.Study.inotebook.Payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@Builder
public class NotebookResponse {

    private Long id;

    private Long userId;

    private String title;

    private String description;

    private Boolean isArchived;

    private LocalDateTime createdDate;

    private LocalDateTime lastUpdatedDate;
}
