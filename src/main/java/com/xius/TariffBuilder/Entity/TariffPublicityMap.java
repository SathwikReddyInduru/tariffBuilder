package com.xius.TariffBuilder.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="CS_RAT_TPID_VS_PUBLICITYID")
@Data
@NoArgsConstructor
public class TariffPublicityMap {

    @Id
    @Column(name="TARIFF_PACKAGE_ID")
    private Long tariffPackageId;


    @Column(name="NETWORK_ID")
    private Long networkId;


    @Column(name="TARIFF_PACKAGE_DESC")
    private String tariffPackageDesc;


    @Column(name="PUBLICITY_ID")
    private String publicityId;


    @Column(name="RECORD_INSERTED_BY")
    private String recordInsertedBy;


    @Column(name="REC_INSERTED_DATE")
    private LocalDate recInsertedDate;
}