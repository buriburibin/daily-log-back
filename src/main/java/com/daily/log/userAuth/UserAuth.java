package com.daily.log.userAuth;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_auth")
public class UserAuth {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userAuthSeq;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "auth_num")
    private String authNum;

    @Column(name = "reg_date")
    private Date regDate;
}