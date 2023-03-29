package com.daily.log.team;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_seq", nullable = false)
    private Long teamSeq;

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @Column(name = "team_tenant_seq", nullable = false)
    private Long teamTenantSeq;

    @Column(name = "reg_date")
    private Date regDate;
}