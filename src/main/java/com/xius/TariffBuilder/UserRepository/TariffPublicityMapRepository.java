package com.xius.TariffBuilder.UserRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xius.TariffBuilder.Entity.TariffPublicityMap;

public interface TariffPublicityMapRepository
                extends JpaRepository<TariffPublicityMap, Long> {
}