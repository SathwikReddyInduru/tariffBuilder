package com.xius.TariffBuilder.UserRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xius.TariffBuilder.Entity.TariffStatus;

@Repository
public interface TariffStatusRepository
        extends JpaRepository<TariffStatus, Long> {

}