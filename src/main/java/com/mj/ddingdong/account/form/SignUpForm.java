package com.mj.ddingdong.account.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignUpForm {

    @NotBlank
    @Length(min = 2, max = 20, message = "2자 이상 20자 이내로 입력하세요.")
    private String name;

    @NotBlank
    @Length(max = 20, message = "20자 이내로 입력하세요.")
    private String nickname;

    @NotBlank
    @Length(min = 5, max = 20, message = "5자 이상 20자 이내로 입력하세요.")
    private String signUpId;

    @NotBlank
    @Pattern(regexp = "((?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,50})", message = "영문자, 숫자, 특수기호를 각각 1개 이상 조합하여 8자 이상 50자 이내로 입력하세요.")
    private String password;

    private boolean signUpAsManager;
}
