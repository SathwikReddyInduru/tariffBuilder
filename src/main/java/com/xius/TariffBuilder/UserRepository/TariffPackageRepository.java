package com.xius.TariffBuilder.UserRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xius.TariffBuilder.Entity.TariffPackage;

public interface TariffPackageRepository
                extends JpaRepository<TariffPackage, Long> {

        boolean existsByNetworkIdAndTariffPackageDescIgnoreCase(
                        Long networkId,
                        String name);

        boolean existsByNetworkIdAndPublicityIdIgnoreCase(
                        Long networkId,
                        String publicityId);
}
