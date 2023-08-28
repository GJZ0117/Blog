package com.gjz.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {

    private Long id;

    private String nickname;

    private String username;

    private String password;

    private String avatar;

    private String email;
}
