package com.xius.TariffBuilder.Dao;

public class TariffDAO {
	 
    private Long networkId;
    private String tariffPackageName;
 
    public TariffDAO(Long networkId, String tariffPackageName) {
        this.networkId = networkId;
        this.tariffPackageName = tariffPackageName;
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
 
    @Override
    public String toString() {
        return "TariffDAO [networkId=" + networkId + ", tariffPackageName=" + tariffPackageName + "]";
    }
}