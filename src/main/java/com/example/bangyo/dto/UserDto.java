package com.example.bangyo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String email;    // 필수
    private String password; // 필수

    private String nickname; // 선택
}
