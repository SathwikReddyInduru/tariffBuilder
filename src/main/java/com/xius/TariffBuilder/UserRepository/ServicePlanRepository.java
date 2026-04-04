package com.xius.TariffBuilder.UserRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xius.TariffBuilder.Entity.ServicePlanPackMap;

public interface ServicePlanRepository extends JpaRepository<ServicePlanPackMap, String> {

	@Query(value = "SELECT * FROM CS_SERVICE_PLAN_PACK_MAP " +
			"WHERE NETWORK_ID = :networkId " +
			"AND TARIFF_PLAN_TYPE = 'TP' " +
			"AND SERVICE_TYPES = :types", nativeQuery = true)
	List<ServicePlanPackMap> getPlansByExactType(@Param("networkId") Long networkId,
			@Param("types") String types);

	@Query(value = "SELECT * FROM CS_SERVICE_PLAN_PACK_MAP " +
			"WHERE NETWORK_ID = :networkId " +
			"AND TARIFF_PLAN_TYPE = 'ATP' " +
			"AND SERVICE_TYPES = :types", nativeQuery = true)
	List<ServicePlanPackMap> getDAtpPlansByExactType(@Param("networkId") Long networkId, @Param("types") String types);

	@Query(value = "SELECT * FROM CS_SERVICE_PLAN_PACK_MAP " +
			"WHERE NETWORK_ID = :networkId " +
			"AND TARIFF_PLAN_TYPE = 'ATP' " +
			"AND SERVICE_TYPES = :types", nativeQuery = true)
	List<ServicePlanPackMap> getAAtpPlansByExactType(@Param("networkId") Long networkId, @Param("types") String types);
}