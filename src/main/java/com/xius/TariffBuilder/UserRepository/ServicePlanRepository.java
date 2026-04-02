package com.xius.TariffBuilder.UserRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xius.TariffBuilder.Entity.ServicePlanPackMap;

public interface ServicePlanRepository extends JpaRepository<ServicePlanPackMap, String> {

	@Query(value = "SELECT * FROM CS_SERVICE_PLAN_PACK_MAP " +
			"WHERE NETWORK_ID = 16 " +
			"AND TARIFF_PLAN_TYPE = 'TP' " +
			"AND SERVICE_TYPES = :types", nativeQuery = true)
	List<ServicePlanPackMap> getPlansByExactType(@Param("types") String types);
	//TODO why cant we add network id as argument in controller

	@Query(value = "SELECT * FROM CS_SERVICE_PLAN_PACK_MAP " +
	        "WHERE NETWORK_ID = 16 " +
	        "AND TARIFF_PLAN_TYPE = 'ATP' " +
	        "AND SERVICE_TYPES = :types",
	        nativeQuery = true)
	List<ServicePlanPackMap> getAtpPlansByExactType(@Param("types") String types);
}