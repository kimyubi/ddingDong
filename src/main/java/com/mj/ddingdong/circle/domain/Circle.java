package com.mj.ddingdong.circle.domain;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.recruit.domain.Recruit;
import lombok.*;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity @EqualsAndHashCode(of = "id")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Circle {

    @Id @GeneratedValue
    private Long id;

    @ManyToMany
    private Set<Account> managers = new HashSet<>();

    @ManyToMany
    private Set<Account> members = new HashSet<>();

    @Column(unique = true, nullable = false)
    private String path;

    @Column(nullable = false)
    private String title;

    @Basic(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private String shortIntroduce;

    @Lob @Basic(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private String detailDescription;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String bannerImage;

    @Column(nullable = false)
    private String field;

    private boolean useBanner;

    private String ownUrl;

    @Column(nullable = true)
    private boolean recruiting;

    @OneToMany(mappedBy = "circle", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Activity> activities;

    @OneToMany(mappedBy = "circle", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Recruit> recruits;

    private LocalDateTime createdTime;


    public String getEncodedPath(String path) throws UnsupportedEncodingException {
        return URLEncoder.encode(path, "utf-8");
    }

    public String[] getOwnUrlArray() throws UnsupportedEncodingException {
        return this.ownUrl.replace(" ", "").split(",");
    }

    public boolean isMember(Account account){
        for(Account member : this.getMembers()){
            if(member.getId().equals(account.getId())){
                return true;
            }
        }
        return false;
    }

    public boolean isRecruiting(){
        for(Recruit recruit : this.recruits){
            if(recruit.getStartRecruitTime().isBefore(LocalDateTime.now()) && recruit.getEndRecruitTime().isAfter(LocalDateTime.now())){
                return true;
            }
        }
        return false;
    }

    public String getBannerImage() {
        return this.bannerImage != null ? bannerImage : "/images/banner.jpg";
    }
}
