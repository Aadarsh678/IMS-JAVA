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

    // Ensure title is not empty and has a minimum and maximum length.
    @NotEmpty(message = "Title must not be empty")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;

    // Ensure description is not empty, and restrict its maximum length.
    @NotEmpty(message = "Description must not be empty")
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotNull(message = "Post type must not be null")
    @Enumerated(EnumType.STRING)
    private PostType postType;

    // EPostStatus is not null when creating the post.
    @NotNull(message = "Post status must not be null")
    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.DRAFT;

    // isActive flag is a boolean value and is set to true by default.
    private boolean isActive = true;

    // Validate that the User who created the post is not null.
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

