package com.url.shorten.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "ACCESS_TIME")
@Setter
@Getter
public class AccessTime {
    @Id
    @Column(name= "ACCESS_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long accessId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "ID")
    public Url alias;
    @Column(name="ACCESS_TIME")
    Date accessTime;

}
