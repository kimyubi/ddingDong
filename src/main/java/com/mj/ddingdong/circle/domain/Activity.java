package com.mj.ddingdong.circle.domain;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Activity {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int viewCount = 0;

    @Column(nullable = false)
    private String content;

    private LocalDateTime writedTime;

    @ManyToOne
    private Circle circle;
}
