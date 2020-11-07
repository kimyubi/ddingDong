package com.mj.ddingdong.recruit.domain;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.circle.domain.Circle;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of="id")
@AllArgsConstructor @NoArgsConstructor
public class Recruit {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Circle circle;

    @ManyToMany
    private Set<Account> managers;

    //@Column(nullable = false)
    private String title;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String applicationForm;

    private LocalDateTime createRecruitTime;

    private LocalDateTime startRecruitTime;

    private LocalDateTime endRecruitTime;

    @OneToMany(mappedBy = "recruit")
    @OrderBy("enrolledTime")
    private Set<Enrollment> enrollments;
}
