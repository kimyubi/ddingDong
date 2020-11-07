package com.mj.ddingdong.recruit.domain;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.circle.domain.Circle;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @EqualsAndHashCode(of="id")
@AllArgsConstructor @NoArgsConstructor
public class Enrollment {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Recruit recruit;

    @ManyToOne
    private Account account;

    private LocalDateTime enrolledTime;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String applicationForm;


}
