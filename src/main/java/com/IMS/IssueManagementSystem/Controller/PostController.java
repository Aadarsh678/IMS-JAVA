package com.IMS.IssueManagementSystem.Controller;

import com.IMS.IssueManagementSystem.DTO.PostDtos;
import com.IMS.IssueManagementSystem.Model.User;
import com.IMS.IssueManagementSystem.Model.UserPrincipal;
import com.IMS.IssueManagementSystem.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // Create a post
    @PostMapping("/create-post")
    public PostDtos.Response createPost(@RequestBody PostDtos.CreateRequest request,@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getUserId();
        return postService.createPost(request, userId);
    }

    // Update a post
    @PutMapping("/update-post/{postId}")
    public PostDtos.Response updatePost(@PathVariable Long postId, @RequestBody PostDtos.UpdateRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        long userId = userPrincipal.getUserId();
        return postService.updatePost(postId, userId, request.getTitle(), request.getDescription());
    }

    @PutMapping("/close-post/{postId}")
    public PostDtos.Response closePost(@PathVariable Long postId,@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getUserId();
        return postService.closePost(userId,postId);
    }

    @PutMapping("/submit-post/{postId}")
    public PostDtos.Response submitPost(@PathVariable Long postId,@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getUserId();
        return postService.submitApproval(userId,postId);
    }

    // Delete a post
    @DeleteMapping("/delete-post/{postId}")
    public PostDtos.Response deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        long  userId = userPrincipal.getUserId();
        return postService.deletePost(postId, userId);
    }

    // Get all posts for the logged-in user
    @GetMapping("/my-posts")
    public List<PostDtos.PostResponse> getAllUserPosts(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        long userId = userPrincipal.getUserId();
        return postService.getAllUserPosts(userId);
    }

    // Get approved posts for a specific user (if viewing their own, return all; if viewing others', return only approved)
    @GetMapping("/approved/{targetUserId}")
    public List<PostDtos.PostResponse> getApprovedPosts(@PathVariable Long targetUserId) {
        return postService.getApprovedPostsByUser(targetUserId);
    }

    // Get all approved posts (public view)
    @GetMapping("/feed/approved")
    public List<PostDtos.PostResponse> getApprovedPosts() {
        return postService.getApprovedPosts();
    }
}
