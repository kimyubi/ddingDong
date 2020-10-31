package com.mj.ddingdong.account.domain;

import com.mj.ddingdong.tag.domain.DepartmentTag;
import com.mj.ddingdong.tag.domain.FieldTag;
import com.mj.ddingdong.tag.domain.IntroduceTag;
import lombok.*;

import javax.persistence.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
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
    private String nickname;

    @Column(nullable = false, unique = true)
    private String signUpId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    private LocalDateTime signUpAt;

    private String shortIntroduce;

    private boolean circlesCreateNotification;

    private boolean circlesRestartNotification;

    private boolean signUpAsManager;

    private boolean recognizedManager;

    private int hagbeon;

    public String getEncodedNickname() {
        return URLEncoder.encode(this.nickname, StandardCharsets.UTF_8);
    }

    @ManyToMany
    private Set<IntroduceTag> introduceTags = new HashSet<>();        // TODO 소개 태그

    @ManyToMany
    private Set<FieldTag> FieldTags = new HashSet<>();         // TODO 동아리 분야 태그

    @ManyToMany
    private Set<DepartmentTag> DepartmentTags = new HashSet<>();       // TODO 학부, 전공 태그


}
