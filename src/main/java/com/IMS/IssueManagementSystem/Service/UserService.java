package com.IMS.IssueManagementSystem.Service;

import com.IMS.IssueManagementSystem.DTO.UserDtos;
import com.IMS.IssueManagementSystem.Model.User;
import com.IMS.IssueManagementSystem.Model.Role;
import com.IMS.IssueManagementSystem.Repository.RoleRepo;
import com.IMS.IssueManagementSystem.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepo userRepo;

    @Autowired
    private final RoleRepo roleRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;



    public User registerUser(User user) {
        if (userRepo.findByEmail(user.getEmail()) != null) {
        throw new RuntimeException("Email already exists!");
        }

        // Check if username already exists
        if (userRepo.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists!");
        }

        // Check if contact exists (optional)
        if (user.getContact() != null && !user.getContact().isEmpty()) {
            User existingContact = userRepo.findByContact(user.getContact());
            if (existingContact != null) {
                throw new RuntimeException("Contact number already exists!");
            }
        }
        List<Role> roles = new ArrayList<>();
        for(Role role : user.getRoles()) {
            Role existingRole = roleRepo.findByName(role.getName());

            if(existingRole == null) {
                throw new RuntimeException("Role not found: " + role.getName());
            }
            roles.add(existingRole);
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(roles));
        userRepo.save(user);
        return user;
    }

    public User loginUser(String email, String password) {
        authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(email, password)
        );
        return  userRepo.findByEmail(email);
    }


    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void deleteUser(Long id) {
        if (!userRepo.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepo.deleteById(id);
    }


    public User updateUser(Long id, User updatedUser) {
        User existingUser = getUserById(id);

        // Update basic fields
        if (updatedUser.getUsername() != null) {
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(bCryptPasswordEncoder.encode(updatedUser.getPassword()));
        }

        if (updatedUser.getRoles() != null && !updatedUser.getRoles().isEmpty()) {
            List<Role> updatedRoles = new ArrayList<>();

            for (Role role : updatedUser.getRoles()) {
                Role existingRole = roleRepo.findByName(role.getName());

                updatedRoles.add(existingRole);
            }

            existingUser.setRoles((Set<Role>) updatedRoles);
        }

        // Save and return updated user
        return userRepo.save(existingUser);
    }

    public UserDtos.PromoteResponse promoteUserToAdmin(UserDtos.ChangeUserRoleRequest request) {
        User user = getUserById(request.getId());
        if(user == null) {
            throw new RuntimeException("User not found with id: " + request.getId());
        }
        Role adminRole = roleRepo.findByName("ADMIN");
        if (adminRole == null) {
            throw new RuntimeException("Role 'ADMIN' not found in the database");
        }
        if (!user.getRoles().contains(adminRole)) {
            user.getRoles().add(adminRole);
            user.setUpdatedDate(LocalDateTime.now());
            userRepo.save(user);

            return new UserDtos.PromoteResponse(
                    200,
                    "User promoted to ADMIN successfully",
                    "SUCCESS",
                    LocalDateTime.now()
            );
        } else {
            throw new RuntimeException("User is already an admin");
        }
    }


    public UserDtos.PromoteResponse demoteAdminToUser(UserDtos.ChangeUserRoleRequest request,Long currentuserId){
        User user = getUserById(request.getId());
        User currentUser = getUserById(currentuserId);
        if (currentUser.getId().equals(request.getId())) {
            throw new RuntimeException("Admin cannot demote themselves");
        }
        Role adminRole = roleRepo.findByName("ADMIN");
        if (adminRole == null) {
            throw new RuntimeException("Role 'ADMIN' not found in the database");
        }
        if (user.getRoles().contains(adminRole)) {
            user.getRoles().remove(adminRole);
            user.setUpdatedDate(LocalDateTime.now());
            userRepo.save(user);
            return new UserDtos.PromoteResponse(
                    200,
                    "User demoted Successfully.",
                    "SUCCESS",
                    LocalDateTime.now()
            );

        }else {
            throw new RuntimeException("User is not an admin");
        }}


}