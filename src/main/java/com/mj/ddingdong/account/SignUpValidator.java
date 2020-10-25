package com.mj.ddingdong.account;

import com.mj.ddingdong.account.form.SignUpForm;
import com.mj.ddingdong.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpValidator implements Validator {

    private final AccountRepository accountRepository;
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        SignUpForm signUpForm = (SignUpForm) o;
        if(accountRepository.existsBySignUpId(signUpForm.getSignUpId())){
            errors.rejectValue("signUpId","wrong.signUpId","해당 아이디는 사용할 수 없습니다.");
        }

        if(accountRepository.existsByNickname(signUpForm.getNickname())){
            errors.rejectValue("nickname","wrong.nickname","이미 사용중인 닉네임입니다.");
        }

    }
}
