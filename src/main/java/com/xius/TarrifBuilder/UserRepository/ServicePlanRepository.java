package com.xius.TarrifBuilder.UserRepository;

import com.xius.TarrifBuilder.Entity.ServicePlanPackMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServicePlanRepository extends JpaRepository<ServicePlanPackMap, String> {

	@Query(value = "SELECT * FROM CS_SERVICE_PLAN_PACK_MAP " +
			"WHERE NETWORK_ID = 16 " +
			"AND TARIFF_PLAN_TYPE = 'TP' " +
			"AND SERVICE_TYPES = :types", nativeQuery = true)
	List<ServicePlanPackMap> getPlansByExactType(@Param("types") String types);
}