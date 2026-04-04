package com.xius.TariffBuilder.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "cs_tariff_pack_ap_reg_status")
public class TariffEntity {

    @Id
    @Column(name = "TARIFF_PACKAGE_ID")
    private Long tariffPackageId;

    @Column(name = "NETWORK_ID")
    private Long networkId;

    @Column(name = "TARIFF_PACKAGE_NAME")
    private String tariffPackageName;

    @Column(name = "STATUS")
    private String status;
}