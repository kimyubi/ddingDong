package com.mj.ddingdong.circle.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CircleForm {

    @NotBlank
    @Length(min = 2, max = 20)
    @Pattern(regexp =  "^[ㄱ-ㅎ가-힣a-z0-9_-]{2,20}$")
    private String path;

    @NotBlank
    @Length(max = 50)
    private String title;

    @NotBlank
    @Length(max = 100)
    private String shortIntroduce;

    @NotBlank
    private String detailDescription;

    @NotBlank
    private String field;

    private String ownUrl;
}
