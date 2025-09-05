package argos.com.br.crud_basico.application.services.user;

import argos.com.br.crud_basico.application.dto.auth.UserResponse;
import argos.com.br.crud_basico.domain.model.User;

public class UserMapper {
    public static UserResponse toResponse(User u){
        return new UserResponse(u.getId(), u.getName(), u.getEmail());
    }
}
