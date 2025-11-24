package com.IMS.IssueManagementSystem.Service;

import com.IMS.IssueManagementSystem.DTO.PostDtos;
import com.IMS.IssueManagementSystem.DTO.UserDtos;
import com.IMS.IssueManagementSystem.Model.Post;
import com.IMS.IssueManagementSystem.Model.enums.PostStatus;
import com.IMS.IssueManagementSystem.Repository.PostRepo;
import com.IMS.IssueManagementSystem.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AdminService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    private PostDtos.PostResponse convertToPostResponse(Post post) {
        PostDtos.UserResponse userResponse = new PostDtos.UserResponse(
                post.getCreatedBy().getId(),
                post.getCreatedBy().getUsername(),
                post.getCreatedBy().getEmail(),
                post.getCreatedBy().getContact()
        );

        return new PostDtos.PostResponse(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getStatus().toString(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                userResponse
        );
    }

    public PostDtos.Response approvePost(Long postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if(post.getStatus() == PostStatus.APPROVED) {
            throw new RuntimeException("Post is already approved");
        }
        if(post.getStatus() == PostStatus.CLOSED) {
            throw new RuntimeException("Post is already closed");
        }

        post.setStatus(PostStatus.APPROVED);
        post.setUpdatedAt(LocalDateTime.now());

        Post updatedPost = postRepo.save(post);
        PostDtos.PostResponse postResponse = convertToPostResponse(updatedPost);

        return new PostDtos.Response(200, "Post approved successfully", "SUCCESS", postResponse);
    }

    public PostDtos.Response rejectPost(Long postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        if(post.getStatus() == PostStatus.REJECTED) {
            throw new RuntimeException("Post is already rejected");
        }
        if(post.getStatus() == PostStatus.CLOSED) {
            throw new RuntimeException("Post is already closed");
        }
        post.setStatus(PostStatus.REJECTED);
        post.setUpdatedAt(LocalDateTime.now());
        Post updatedPost = postRepo.save(post);
        PostDtos.PostResponse postResponse = convertToPostResponse(updatedPost);
        return new PostDtos.Response(200, "Post rejected successfully", "SUCCESS", postResponse);
    }


}
