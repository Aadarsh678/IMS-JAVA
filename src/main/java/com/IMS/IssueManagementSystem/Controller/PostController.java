package com.IMS.IssueManagementSystem.Controller;

import com.IMS.IssueManagementSystem.DTO.PostDtos;
import com.IMS.IssueManagementSystem.Model.User;
import com.IMS.IssueManagementSystem.Model.UserPrincipal;
import com.IMS.IssueManagementSystem.Model.enums.PostStatus;
import com.IMS.IssueManagementSystem.Model.enums.PostType;
import com.IMS.IssueManagementSystem.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {

    @Autowired
    private PostService postService;

    // Create a post
    @PostMapping
    public PostDtos.Response createPost(@RequestBody PostDtos.CreateRequest request,@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getUserId();
        return postService.createPost(request, userId);
    }

    // Update a post
    @PutMapping("/update-post/{id}")
    public PostDtos.Response updatePost(@PathVariable Long id, @RequestBody PostDtos.UpdateRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        long userId = userPrincipal.getUserId();
        return postService.updatePost(id, userId, String.valueOf(request.getPostType()),request.getTitle(), request.getDescription());
    }

    @PutMapping("/close")
    public PostDtos.Response closePost(@RequestBody PostDtos.ChangePostStatusRequest request,@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getUserId();
        return postService.closePost(userId,request);
    }

    @PutMapping("/submit")
    public PostDtos.Response submitPost(@RequestBody PostDtos.ChangePostStatusRequest request,@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getUserId();
        return postService.submitApproval(userId,request);
    }

    // Delete a post
    @DeleteMapping("/{id}")
    public PostDtos.Response deletePost(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        long  userId = userPrincipal.getUserId();
        return postService.deletePost(id, userId);
    }

    // Get all posts for the logged-in user
    @GetMapping("/user")
    public List<PostDtos.PostResponse> getAllUserPosts(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                       @RequestParam(required = false) PostStatus postStatus,
                                                       @RequestParam(required = false) PostType postType) {
        long userId = userPrincipal.getUserId();
        return postService.getAllUserPosts(userId,postStatus,postType);
    }

    // Get approved posts for a specific user (if viewing their own, return all; if viewing others', return only approved)
    @GetMapping("/approved/{targetUserId}")
    public List<PostDtos.PostResponse> getApprovedPosts(@PathVariable Long targetUserId) {
        return postService.getApprovedPostsByUser(targetUserId);
    }

    // Get all approved posts (public view)
    @GetMapping("/approved")
    public List<PostDtos.PostResponse> getApprovedPosts(
            @RequestParam(required = false) PostType postType
    ) {
        return postService.getApprovedPosts(postType);
    }

    @GetMapping("/{id}")
    public PostDtos.Response getPostById(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getUserId();
        return postService.findPostById(id, userId);
    }
}
