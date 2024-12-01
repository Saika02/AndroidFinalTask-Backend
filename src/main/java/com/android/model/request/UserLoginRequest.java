package com.android.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest  implements Serializable {
    private String username;
    private String password;
}
