package com.xius.TariffBuilder.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CS_TARIFF_PACK_AP_REG_STATUS")
public class TariffStatus {

    @Id
    @Column(name = "TARIFF_PACKAGE_ID")
    private Long tariffPackageId;

    @Column(name = "NETWORK_ID")
    private Long networkId;

    @Column(name = "TARIFF_PACKAGE_NAME")
    private String tariffPackageName;

    @Column(name = "STATUS")
    private String status;

    public Long getTariffPackageId() {
        return tariffPackageId;
    }

    public void setTariffPackageId(Long tariffPackageId) {
        this.tariffPackageId = tariffPackageId;
    }

    public Long getNetworkId() {
        return networkId;
    }

    public void setNetworkId(Long networkId) {
        this.networkId = networkId;
    }

    public String getTariffPackageName() {
        return tariffPackageName;
    }

    public void setTariffPackageName(String tariffPackageName) {
        this.tariffPackageName = tariffPackageName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
