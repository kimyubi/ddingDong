package com.mj.ddingdong.recruit.domain;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.circle.domain.Circle;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
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
    private Set<Account> managers = new HashSet<>();

    @Column(nullable = false)
    private String title;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private String applicationForm;

    private LocalDateTime createRecruitTime;

    private LocalDateTime publishRecruitTime;

    private LocalDateTime startRecruitTime;

    private LocalDateTime endRecruitTime;

    @Column(nullable = false)
    private String shortIntroduce;

    @OneToMany(mappedBy = "recruit")
    @OrderBy("enrolledTime")
    private Set<Enrollment> enrollments;

    private Integer limitPersonnel;

    public boolean isEnrolled(Account account){
        for(Enrollment enrollment : this.enrollments){
            if(enrollment.getAccount().getId().equals(account.getId())){
                return true;
            }
        }
        return false;
    }
}
