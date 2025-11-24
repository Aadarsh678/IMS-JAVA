package com.IMS.IssueManagementSystem.Model;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(length = 2000)
    @NotEmpty(message = "Comment must not be empty")
    private String text;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private boolean isActive = true;

    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updatedDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;

}
