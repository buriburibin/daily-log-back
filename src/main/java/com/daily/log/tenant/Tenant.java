package com.daily.log.tenant;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "tenant")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_seq", nullable = false)
    private Long tenantSeq;

    @Column(name = "tenant_name", nullable = false)
    private String tenantName;

    @Column(name = "tenant_url", nullable = false)
    private String tenantUrl;

    @Column(name = "tenant_host", nullable = false)
    private String tenantHost;

    @Column(name = "reg_date")
    private Date regDate;
}