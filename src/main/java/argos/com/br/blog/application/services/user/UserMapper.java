package argos.com.br.blog.application.services.user;

import argos.com.br.blog.application.dto.auth.UserResponse;
import argos.com.br.blog.domain.model.User;

public class UserMapper {
    public static UserResponse toResponse(User u){
        return new UserResponse(u.getId(), u.getName(), u.getEmail());
    }
}
