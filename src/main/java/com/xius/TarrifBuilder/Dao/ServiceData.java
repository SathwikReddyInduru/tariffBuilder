package com.xius.TarrifBuilder.Dao;

import lombok.Data;

@Data
public class ServiceData {
	
	private String servicePackageId;
    private String servicePackageName;
    private Integer networkId;
    private String tariffPlanType;
    private String serviceTypes;

	   
	}

