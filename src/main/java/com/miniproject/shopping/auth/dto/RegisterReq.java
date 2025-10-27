package com.miniproject.auth.auth.dto;

import com.miniproject.auth.common.entity.Role;
import com.miniproject.auth.common.entity.User;
import lombok.Data;

@Data
public class RegisterReq {
    private String username;
    private String password;
    private String email;

    public User toEntity(Role role){
        return new User(this.username, this.password, this.email, role);
    }
}
