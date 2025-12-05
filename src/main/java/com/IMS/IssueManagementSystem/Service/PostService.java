package com.IMS.IssueManagementSystem.Service;

import com.IMS.IssueManagementSystem.DTO.PostDtos;
import com.IMS.IssueManagementSystem.Model.Post;
import com.IMS.IssueManagementSystem.Model.User;
import com.IMS.IssueManagementSystem.Model.enums.PostStatus;
import com.IMS.IssueManagementSystem.Model.enums.PostType;
import com.IMS.IssueManagementSystem.Repository.PostRepo;
import com.IMS.IssueManagementSystem.Repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

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

    public PostDtos.Response createPost(PostDtos.CreateRequest request, Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setCreatedBy(user);
        post.setPostType(request.getPostType());
        post.setStatus(PostStatus.DRAFT);
        post.setActive(true);
        post.setCreatedAt(LocalDateTime.now());

        Post savedPost = postRepo.save(post);
        PostDtos.PostResponse postResponse = convertToPostResponse(savedPost);

        return new PostDtos.Response(200, "Post created successfully", "DRAFT", postResponse);
    }

    public PostDtos.Response updatePost(Long postId, Long userId, String newPostType,String newTitle, String newDescription) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getCreatedBy().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to update this post.");
        }
        post.setPostType(PostType.valueOf(newPostType));
        post.setTitle(newTitle);
        post.setDescription(newDescription);
        post.setUpdatedAt(LocalDateTime.now());

        Post updatedPost = postRepo.save(post);
        PostDtos.PostResponse postResponse = convertToPostResponse(updatedPost);

        return new PostDtos.Response(200, "Post updated successfully", "SUCCESS", postResponse);
    }

    public PostDtos.Response deletePost(Long postId, Long userId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        if (!post.getCreatedBy().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to delete this post.");
        }

        postRepo.delete(post);
        return new PostDtos.Response(200, "Post deleted successfully","SUCCESS", null);
    }

    public PostDtos.Response submitApproval(Long userId,PostDtos.ChangePostStatusRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Post post = postRepo.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        if (!post.getCreatedBy().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to submit this post.");
        }
        post.setStatus(PostStatus.PENDING_APPROVAL);
        post.setUpdatedAt(LocalDateTime.now());
        postRepo.save(post);
        PostDtos.PostResponse postResponse = convertToPostResponse(post);
        return new PostDtos.Response(200, "Post submitted successfully", "SUCCESS", postResponse);
    }

    public PostDtos.Response findPostById(Long postId, Long userId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        if (!post.getCreatedBy().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to view this post.");
        }

        PostDtos.PostResponse postResponse = convertToPostResponse(post);
        return new PostDtos.Response(200, "Post retrieved successfully", "SUCCESS", postResponse);
    }

    public PostDtos.Response closePost(Long userId,PostDtos.ChangePostStatusRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Post post = postRepo.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        if (!post.getCreatedBy().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to close this post.");
        }
        post.setStatus(PostStatus.CLOSED);
        post.setUpdatedAt(LocalDateTime.now());
        postRepo.save(post);
        PostDtos.PostResponse postResponse = convertToPostResponse(post);
        return new PostDtos.Response(200, "Post closed successfully", "SUCCESS", postResponse);
    }

    public List<PostDtos.PostResponse> getAllUserPosts(Long userId, PostStatus postStatus, PostType postType) {

        List<Post> posts;

        if (postStatus != null && postType != null) {
            posts = postRepo.findByCreatedByIdAndStatusAndPostType(userId, postStatus, postType);
        }
        else if (postStatus != null) {
            posts = postRepo.findByCreatedByIdAndStatus(userId, postStatus);
        }
        else if (postType != null) {
            posts = postRepo.findByCreatedByIdAndPostType(userId, postType);
        }
        else {
            posts = postRepo.findByCreatedById(userId);
        }

        return convertToPostResponseList(posts);
    }


    public List<PostDtos.PostResponse> getApprovedPostsByUser(Long targetUserId) {
        List<Post> posts;
        User user = userRepo.findById(targetUserId)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
        posts = postRepo.findByCreatedByIdAndStatus(targetUserId, PostStatus.APPROVED);

        return convertToPostResponseList(posts);
    }

    private List<PostDtos.PostResponse> convertToPostResponseList(List<Post> posts) {
        return posts.stream()
                .map(this::convertToPostResponse)
                .toList();
    }

    public List<PostDtos.PostResponse> getApprovedPosts(PostType postType) {
        if (postType != null) {
            return convertToPostResponseList(postRepo.findByStatusAndPostType(PostStatus.APPROVED, postType));
        }else {
            return convertToPostResponseList(postRepo.findByStatus(PostStatus.APPROVED));
        }

    }

    public List<PostDtos.PostResponse> getSubmittedPosts(PostType postType) {
        if (postType != null) {
            return convertToPostResponseList(postRepo.findByStatusAndPostType(PostStatus.PENDING_APPROVAL, postType));
        } else {
            return convertToPostResponseList(postRepo.findByStatus(PostStatus.PENDING_APPROVAL));
        }
    }

    public List<PostDtos.PostResponse> getRejectedPosts(PostType postType) {
        if (postType != null) {
            return convertToPostResponseList(postRepo.findByStatusAndPostType(PostStatus.REJECTED, postType));
        } else {
            return convertToPostResponseList(postRepo.findByStatus(PostStatus.REJECTED));
        }
    }


    public List<PostDtos.PostResponse> getSubmittedPostsByUser(Long userId, PostType postType) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Post> posts;

        if (postType != null) {
            posts = postRepo.findByCreatedByIdAndStatusAndPostType(userId, PostStatus.PENDING_APPROVAL, postType);
        } else {
            posts = postRepo.findByCreatedByIdAndStatus(userId, PostStatus.PENDING_APPROVAL);
        }

        return convertToPostResponseList(posts);
    }

}
