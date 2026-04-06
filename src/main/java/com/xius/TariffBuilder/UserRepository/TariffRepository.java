package com.xius.TariffBuilder.UserRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.xius.TariffBuilder.Entity.TariffEntity;

@Repository
public interface TariffRepository extends JpaRepository<TariffEntity, Long> {

	@Query(value = "SELECT NETWORK_ID, TARIFF_PACKAGE_NAME, TARIFF_PACKAGE_ID, STATUS "
			+ "FROM CS_TARIFF_PACK_AP_REG_STATUS ", nativeQuery = true)
	List<Object[]> getTariffPackagesStatic();

	List<TariffEntity> findByStatusIsNull();
}