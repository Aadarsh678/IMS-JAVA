package com.IMS.IssueManagementSystem.Controller;

import com.IMS.IssueManagementSystem.DTO.PostDtos;
import com.IMS.IssueManagementSystem.DTO.UserDtos;
import com.IMS.IssueManagementSystem.Model.User;
import com.IMS.IssueManagementSystem.Model.UserPrincipal;
import com.IMS.IssueManagementSystem.Model.enums.PostStatus;
import com.IMS.IssueManagementSystem.Service.AdminService;
import com.IMS.IssueManagementSystem.Service.RoleService;
import com.IMS.IssueManagementSystem.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @PutMapping("/approve-post/{postId}")
    public PostDtos.Response approvePost(@PathVariable Long postId) {
        return adminService.approvePost(postId);
    }

    @PutMapping("/reject-post/{postId}")
    public PostDtos.Response rejectPost(@PathVariable Long postId) {
        return adminService.rejectPost(postId);
    }

    @PutMapping("/promote-to-admin/{userId}")
    public UserDtos.PromoteResponse promoteToAdmin(@PathVariable Long userId) {
        return userService.promoteUserToAdmin(userId);
    }

    @PutMapping("/demote-to-user/{userId}")
    public UserDtos.PromoteResponse demoteToAdmin(@PathVariable Long userId,@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long currentuserId = userPrincipal.getUserId();
        return userService.demoteAdminToUser(userId,currentuserId);
    }



}
