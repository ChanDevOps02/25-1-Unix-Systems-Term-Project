package com.example.UnixPractice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class UserDto {
    private String username;
    private String password;
    private String confirmPassword;

}
