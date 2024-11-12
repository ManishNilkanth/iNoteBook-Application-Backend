package com.Study.inotebook.Payload;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@Builder
public class NoteResponse {

    private Long id;

    private String title;

    private String content;

    private Boolean isFavorite;

    private Boolean isArchived;

    private Long notebookId;

    private LocalDateTime createdDate;

    private LocalDateTime lastUpdatedDate;

}
