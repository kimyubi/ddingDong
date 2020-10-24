package com.mj.ddingdong.account.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String SignUpId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    private String profileImage;

    private boolean emailVerified;

    private String emailVerifyToken;

    private LocalDateTime signUpAt;

    private String shortIntroduce;

    private LocalDateTime emailVerifyTokenGeneratedAt;

    private boolean circlesCreateNotification;

    private boolean circlesRestartNotification;

    private boolean signUpAsManager;

    private boolean recognizedManager;

//    @OneToMany
//    private Set<IntroduceTag> introduceTags;       // TODO 소개 태그

//    @OneToMany
//    private Set<FieldTag> FieldTags;       // TODO 동아리 분야 태그


}
