package com.mj.ddingdong.settings.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class NicknameForm {

    @NotNull
    @Length(max = 20)
    private String nickname;
}
