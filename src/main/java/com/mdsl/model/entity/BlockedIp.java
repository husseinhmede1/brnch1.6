package com.mdsl.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "MD_BLOCKED_IPS")
@Getter
@Setter
public class BlockedIp {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blocked_ips_seq_gen")
    @SequenceGenerator(name = "blocked_ips_seq_gen",sequenceName = "BLOCKED_IPS_SEQ",allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "IP_ADDRESS", nullable = false, unique = true)
    private String ipAddress;

    @Column(name = "BLOCKED_AT")
    private LocalDateTime blockedAt;
}