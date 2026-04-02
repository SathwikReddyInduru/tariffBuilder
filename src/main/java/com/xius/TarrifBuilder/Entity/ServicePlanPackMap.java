package com.xius.TarrifBuilder.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "CS_SERVICE_PLAN_PACK_MAP")
public class ServicePlanPackMap {
	

	    @Id
	    @Column(name = "SERVICE_PACKAGE_ID")
	    private String servicePackageId;

	    @Column(name = "SERVICE_PACKAGE_NAME")
	    private String servicePackageName;

	    @Column(name = "NETWORK_ID")
	    private Integer networkId;

	    @Column(name = "TARIFF_PLAN_TYPE")
	    private String tariffPlanType;

	    // Change to String if DB is VARCHAR
	    @Column(name = "SERVICE_TYPES")
	    private String serviceTypes;

	   

	    // Getters and Setters
	}
