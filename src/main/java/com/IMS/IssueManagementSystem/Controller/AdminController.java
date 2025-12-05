package com.IMS.IssueManagementSystem.Controller;

import com.IMS.IssueManagementSystem.Model.enums.PostType;
import com.IMS.IssueManagementSystem.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.IMS.IssueManagementSystem.DTO.PostDtos;
import com.IMS.IssueManagementSystem.DTO.UserDtos;
import com.IMS.IssueManagementSystem.Model.UserPrincipal;
import com.IMS.IssueManagementSystem.Service.AdminService;
import com.IMS.IssueManagementSystem.Service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @PutMapping("/post/approve")
    public PostDtos.Response approvePost(@RequestBody PostDtos.ChangePostStatusRequest request) {
        return adminService.approvePost(request);
    }

    @PutMapping("/post/reject")
    public PostDtos.Response rejectPost(@RequestBody PostDtos.ChangePostStatusRequest request) {
        return adminService.rejectPost(request);
    }

    @PutMapping("/user/promote")
    public UserDtos.PromoteResponse promoteToAdmin(@RequestBody UserDtos.ChangeUserRoleRequest request) {
        return userService.promoteUserToAdmin(request);
    }

    @PutMapping("/user/demote")
    public UserDtos.PromoteResponse demoteToAdmin(@RequestBody UserDtos.ChangeUserRoleRequest request,@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long currentuserId = userPrincipal.getUserId();
        return userService.demoteAdminToUser(request,currentuserId);
    }

    @GetMapping("/ims/dashboard")
    public PostDtos.PostStatisticsDto getPostStatistics() {
        return adminService.getPostStatistics();
    }

    @GetMapping("/users/all")
    public List<UserDtos.UserListResponse> getAllAdminsAndUsers() {
        return adminService.getAllAdminsAndUsers();
    }

    @GetMapping("/post/submitted/all")
    public List<PostDtos.PostResponse> getSubmittedPosts(
            @RequestParam(required = false) PostType postType
    ) {
        return postService.getSubmittedPosts(postType);
    }

    // Get all rejected posts
    @GetMapping("/post/rejected/all")
    public List<PostDtos.PostResponse> getRejectedPosts(
            @RequestParam(required = false) PostType postType
    ) {
        return postService.getRejectedPosts(postType);
    }

    @GetMapping("/post/submitted/user/{userId}")
    public List<PostDtos.PostResponse> getSubmittedPostsByUser(
            @PathVariable Long userId,
            @RequestParam(required = false) PostType postType) {
        return postService.getSubmittedPostsByUser(userId, postType);
    }
}
