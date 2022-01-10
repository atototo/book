package com.bs.search.dto;

import com.bs.search.domain.UserEntity;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUserDto implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUserDto(UserEntity user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
