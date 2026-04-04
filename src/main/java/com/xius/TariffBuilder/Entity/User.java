package com.xius.TariffBuilder.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "UMS_MT_USER")
public class User {

    @Id
    @Column(name = "LOGIN_ID")
    private String loginId;

    @Column(name = "PASSWORD_NAME")
    private String password;

    @ManyToOne
    @JoinColumn(name = "NETWORK_ID")
    private Network network;
}