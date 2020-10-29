package com.mj.ddingdong.profiles.validator;

import com.mj.ddingdong.profiles.form.ProfileForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ProfileFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(ProfileForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProfileForm profileForm = (ProfileForm) o;
        if(profileForm.getHagbeon()<10 || profileForm.getHagbeon() > 21){
            errors.rejectValue("hagbeon","wrong.hagbeon","유효하지 않은 학번입니다.");
        }



    }
}
