package com.mj.ddingdong.main;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class UpdatePasswordValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(PasswordForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PasswordForm passwordForm = (PasswordForm) o;
        if(!passwordForm.getPassword().equals(passwordForm.getConfirmPassword())){
            errors.rejectValue("confirmPassword","wrong.confirmPassword","입력하신 비밀번호와 일치하지 않습니다.");
        }
        if(passwordForm.getPassword().indexOf(" ") != -1){
            errors.rejectValue("password","wrong.password","공백은 사용할 수 없습니다.");
        }




    }
}
