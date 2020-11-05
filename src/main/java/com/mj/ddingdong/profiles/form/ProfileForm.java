package com.mj.ddingdong.profiles.form;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class ProfileForm {

    @Length(max = 30)
    private String shortIntroduce;

    private int hagbeon;

    private String profileImage;
}
