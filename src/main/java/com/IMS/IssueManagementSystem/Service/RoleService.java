package com.IMS.IssueManagementSystem.Service;

import com.IMS.IssueManagementSystem.Model.Role;
import com.IMS.IssueManagementSystem.Repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepo roleRepo;

    public void saveRole(Role role) {
        roleRepo.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }

    public Role getRoleByName(String name) {
        return roleRepo.findByName(name);
    }
}
