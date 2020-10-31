package com.mj.ddingdong.main;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class PasswordForm {

    @NotBlank
    @Pattern(regexp = "((?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,50})", message = "영문자, 숫자, 특수기호를 각각 1개 이상 조합하여 8자 이상 50자 이내로 입력하세요.")
    private String password;


    @NotBlank
    private String confirmPassword;
}
