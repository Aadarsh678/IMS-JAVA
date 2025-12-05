package com.IMS.IssueManagementSystem.Model;
import java.time.LocalDateTime;

import com.IMS.IssueManagementSystem.Model.enums.PostStatus;
import com.IMS.IssueManagementSystem.Model.enums.PostType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Post {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @NotNull(message = "Post type must not be null")
    @Enumerated(EnumType.STRING)
    private PostType postType;
    @NotNull(message = "Post status must not be null")
    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.DRAFT;
    private boolean isActive = true;
    @ManyToOne
    @JoinColumn(name = "created_by_id")
    @NotNull(message = "Creator (user) must not be null")
    private User createdBy;

    // createdAt timestamp is not null.
    private LocalDateTime createdAt = LocalDateTime.now();

    // Validate the updatedAt timestamp for consistency.
    private LocalDateTime updatedAt;

    // Automatically set the updatedAt timestamp on insert and update.
    @PrePersist
    @PreUpdate
    public void setUpdateTimestamp() {
        updatedAt = LocalDateTime.now();
    }

}

