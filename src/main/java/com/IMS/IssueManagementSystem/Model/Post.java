package com.IMS.IssueManagementSystem.Model;
import java.time.LocalDateTime;

import com.IMS.IssueManagementSystem.Model.enums.PostStatus;

import jakarta.persistence.Column;
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
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Title must not be empty")
    private String title;

    @NotEmpty(message = "Description must not be empty")
    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.DRAFT;

    private boolean isActive = true;


    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt ;

    @PrePersist
    @PreUpdate
    public void setUpdateTimestamp() {
        updatedAt = LocalDateTime.now();
    }
}

