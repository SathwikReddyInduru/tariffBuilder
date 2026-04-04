package com.xius.TariffBuilder.UserRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xius.TariffBuilder.Entity.PeriodicChargeInfo;

public interface PeriodicChargeRepository
        extends JpaRepository<PeriodicChargeInfo,String> {

    boolean existsByChargeIdAndNetworkId(
            String chargeId,
            Long networkId);
}