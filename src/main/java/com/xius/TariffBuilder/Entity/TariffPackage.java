package com.xius.TariffBuilder.Entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="CS_RAT_TARIFF_PACKAGE")
@Data
@NoArgsConstructor
public class TariffPackage {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tariff_pack_seq")

    @SequenceGenerator(
            name="tariff_pack_seq",
            sequenceName="SEQ_TARIFF_PACK_ID",
            allocationSize=1)

    @Column(name="TARIFF_PACKAGE_ID")
    private Long tariffPackageId;


    @Column(name="TARIFF_PACKAGE_DESC")
    private String tariffPackageDesc;


    @Column(name="NETWORK_ID")
    private Long networkId;


    @Column(name="END_DATE")
    private LocalDate endDate;


    @Column(name="CHARGE_ID")
    private String chargeId;


    @Column(name="PACKAGE_TYPE")
    private String packageType;


    @Column(name="SUBSCRIBER_CATEGORY_ID")
    private Long subscriberCategoryId;


    @Column(name="IS_CORPORATE_YN")
    private String isCorporateYn;


    @Column(name="TARIFF_PACK_CATEGORY")
    private String tariffPackCategory;


    @Column(name="PUBLICITY_ID")
    private String publicityId;


    @Column(name="DESCRIPTION")
    private String description;
}