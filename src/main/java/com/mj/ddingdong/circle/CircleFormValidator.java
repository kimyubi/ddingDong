package com.mj.ddingdong.circle;

import com.mj.ddingdong.circle.form.CircleForm;
import com.mj.ddingdong.circle.repository.CircleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class CircleFormValidator implements Validator {

    private final CircleRepository circleRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(CircleForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CircleForm circleForm = (CircleForm) o;

        if(circleForm.getPath().indexOf(" ") != -1){
            errors.rejectValue("path","wrong.path","공백은 사용할 수 없습니다.");
        }

        if(circleRepository.existsByPath(circleForm.getPath())){
            errors.rejectValue("path","wrong.path","사용중인 URL은 사용할 수 없습니다.");
        }


    }
}
