package com.mj.ddingdong.circle.domain;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.recruit.domain.Recruit;
import lombok.*;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
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

    @Column(unique = true, nullable = false)
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

    @OneToMany(mappedBy = "circle")
    private Set<Activity> activities;

    @OneToMany(mappedBy = "circle")
    private Set<Recruit> recruits;

    public String getEncodedPath(String path) throws UnsupportedEncodingException {
        return URLEncoder.encode(path, "utf-8");
    }

    public String[] getOwnUrl() throws UnsupportedEncodingException {
        return this.ownUrl.replace(" ","").split(",");
    }

    public boolean isMember(Account account){
        for(Account member : this.getMembers()){
            if(member.getId().equals(account.getId())){
                return true;
            }
        }
        return false;
    }


}
