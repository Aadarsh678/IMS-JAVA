package com.IMS.IssueManagementSystem.Repository;

import com.IMS.IssueManagementSystem.Model.User;
import com.IMS.IssueManagementSystem.Model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByContact(String contact);

    User findById(long id);

    User findByEmail(String email);
    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.name = :roleName")
    int countUsersByRole(@Param("roleName") String roleName);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name IN :roleNames")
    List<User> findAllByRoleNames(@Param("roleNames") List<String> roleNames);





}

