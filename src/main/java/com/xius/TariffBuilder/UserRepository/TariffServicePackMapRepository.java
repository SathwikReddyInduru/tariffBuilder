package com.xius.TariffBuilder.UserRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xius.TariffBuilder.Entity.TariffServicePackMap;
import com.xius.TariffBuilder.Entity.TariffServicePackMapId;


public interface TariffServicePackMapRepository
        extends JpaRepository<TariffServicePackMap,
                              TariffServicePackMapId> {
}