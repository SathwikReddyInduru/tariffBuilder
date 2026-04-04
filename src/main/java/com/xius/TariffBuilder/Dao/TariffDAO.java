package com.xius.TariffBuilder.Dao;

public class TariffDao {

    private Long networkId;
    private String tariffPackageName;
    private String status;
    private Long tariffPackageId;

    public TariffDao(Long networkId, String tariffPackageName, String status, Long tariffPackageId) {
        this.networkId = networkId;
        this.tariffPackageName = tariffPackageName;
        this.status = status;
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

    public Long getTariffPackageId() {
        return tariffPackageId;
    }

    public void setTariffPackageId(Long tariffPackageId) {
        this.tariffPackageId = tariffPackageId;
    }

    @Override
    public String toString() {
        return "TariffDAO [networkId=" + networkId + ", tariffPackageName=" + tariffPackageName + ", status=" + status
                + ", tariffPackageId=" + tariffPackageId + "]";
    }
}