package com.IMS.IssueManagementSystem.Service;

import com.IMS.IssueManagementSystem.DTO.PostDtos;
import com.IMS.IssueManagementSystem.DTO.UserDtos;
import com.IMS.IssueManagementSystem.Model.Post;
import com.IMS.IssueManagementSystem.Model.Role;
import com.IMS.IssueManagementSystem.Model.User;
import com.IMS.IssueManagementSystem.Model.enums.PostStatus;
import com.IMS.IssueManagementSystem.Model.enums.UserRole;
import com.IMS.IssueManagementSystem.Repository.PostRepo;
import com.IMS.IssueManagementSystem.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

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
                post.getPostType().toString(),
                post.getStatus().toString(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                userResponse
        );
    }

    public PostDtos.Response approvePost(PostDtos.ChangePostStatusRequest request) {
        Post post = postRepo.findById(request.getId())
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

    public PostDtos.Response rejectPost(PostDtos.ChangePostStatusRequest request) {
        Post post = postRepo.findById(request.getId())
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

    public PostDtos.PostStatisticsDto getPostStatistics() {

        int totalActiveUsers = userRepo.countUsersByRole(String.valueOf(UserRole.USER));


        int totalApproved = postRepo.countByStatus(PostStatus.APPROVED);
        int totalRejected = postRepo.countByStatus(PostStatus.REJECTED);
        int totalPending = postRepo.countByStatus(PostStatus.PENDING_APPROVAL);
        int totalClosed = postRepo.countByStatus(PostStatus.CLOSED);

        return new PostDtos.PostStatisticsDto(
                totalActiveUsers,
                totalApproved,
                totalRejected,
                totalPending,
                totalClosed
        );
    }


    public List<UserDtos.UserListResponse> getAllAdminsAndUsers() {
        List<String> roles = List.of("ADMIN", "USER");
        List<User> users = userRepo.findAllByRoleNames(roles);

        // Filter out users who have SUPERADMIN role
        List<User> filteredUsers = users.stream()
                .filter(user -> user.getRoles().stream()
                        .noneMatch(role -> role.getName().equals("SUPERADMIN")))
                .collect(Collectors.toList());

        return filteredUsers.stream()
                .map(user -> new UserDtos.UserListResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRoles().stream().map(Role::getName).toList(),
                        user.getContact()
                ))
                .toList();
    }



}
