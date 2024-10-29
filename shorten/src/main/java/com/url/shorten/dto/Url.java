package com.url.shorten.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="URL")
public class Url {
    @Id
    @Column(name= "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long Id;
    @Column(name = "ORIGINAL_URL")
    private String originalUrl;
    @Column(name="SHORT_URL")
    private String alias;
    @Column(name="EXPIRE_TIME")
    long   expireTime;
    @Column(name="ACCESS_COUNT")
    private int  accessCount;
    @Column(name= "ACCESS_TIME")

    @OneToMany(mappedBy = "alias",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<AccessTime> accessTime= new ArrayList<>();
}
