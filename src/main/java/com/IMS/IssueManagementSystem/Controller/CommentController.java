package com.IMS.IssueManagementSystem.Controller;

import com.IMS.IssueManagementSystem.Repository.CommentRepo;
import com.IMS.IssueManagementSystem.Service.CommentService;
import org.springframework.web.bind.annotation.RestController;

import com.IMS.IssueManagementSystem.DTO.CommentDtos;
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
@RequestMapping("/api/Comment")
public class CommentController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/post/{postId}")
    public CommentDtos.Response GetPostComment(@PathVariable Long postId) {
        return commentService.getAllCommentsByPost(postId);
    }

    @PostMapping
    public CommentDtos.Response addComment(
            @RequestBody CommentDtos.CreateCommentRequest createCommentRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = userPrincipal.getUserId();

        return commentService.createComment(createCommentRequest, userId);
    }

    @PutMapping("/{id}")
    public CommentDtos.Response updateComment(@PathVariable Long id,
            @RequestBody CommentDtos.UpdateCommentRequest updateCommentRequest,
                                              @AuthenticationPrincipal UserPrincipal userPrincipal
    ){
        Long userId = userPrincipal.getUserId();
        return commentService.updateComment(id,updateCommentRequest,userId);
    }

    @DeleteMapping("/{id}")
    public CommentDtos.Response deleteComment(@PathVariable Long id,@AuthenticationPrincipal UserPrincipal userPrincipal) {
       Long userId = userPrincipal.getUserId();
        return commentService.deleteComment(id,userId);
    }


}
