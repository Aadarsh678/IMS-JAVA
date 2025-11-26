package com.IMS.IssueManagementSystem.Model;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ensure the text is not empty and limit the maximum length of the comment
    @Column(length = 2000)
    @NotEmpty(message = "Comment text must not be empty")
    @Size(max = 2000, message = "Comment cannot exceed 2000 characters")
    private String text;

    // Ensure the associated post is not null (i.e., a comment must be linked to a post)
    @ManyToOne
    @JoinColumn(name = "post_id")
    @NotNull(message = "Comment must be associated with a post")
    private Post post;

    // Ensure the user who created the comment is not null
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "User who created the comment must not be null")
    private User createdBy;

    // Default value for the active status
    private boolean isActive = true;

    // Automatic timestamp for when the comment was created
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updatedDate;

    @PrePersist
    @PreUpdate
    public void setUpdatedDate() {
        updatedDate = LocalDateTime.now();
    }
}
