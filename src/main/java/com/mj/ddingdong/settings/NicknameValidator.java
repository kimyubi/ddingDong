package com.mj.ddingdong.settings;

import com.mj.ddingdong.account.repository.AccountRepository;
import com.mj.ddingdong.settings.form.NicknameForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class NicknameValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(NicknameForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        NicknameForm nicknameForm = (NicknameForm) o;
        if(accountRepository.existsByNickname(nicknameForm.getNickname())){
            errors.rejectValue("nickname","wrong.nickname","이미 사용중인 닉네임입니다.");
        }

        if(nicknameForm.getNickname().indexOf(" ") != -1){
            errors.rejectValue("nickname","wrong.nickname","공백은 사용할 수 없습니다.");
        }


    }
}
