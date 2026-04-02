package com.xius.TarrifBuilder.UserRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xius.TarrifBuilder.Entity.Network;

public interface NetworkRepository extends JpaRepository<Network, Long> {

    Optional<Network> findByNetworkDisplay(String networkDisplay);
}