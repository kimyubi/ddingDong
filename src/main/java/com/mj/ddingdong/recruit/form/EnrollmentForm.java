package com.mj.ddingdong.recruit.form;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class EnrollmentForm {

    @NotBlank
    private String applicationForm;
}
