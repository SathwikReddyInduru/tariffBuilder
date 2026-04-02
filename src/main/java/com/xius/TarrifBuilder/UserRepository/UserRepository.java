package com.xius.TarrifBuilder.UserRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xius.TarrifBuilder.Entity.User;

	public interface UserRepository extends JpaRepository<User, String> {

	    // ADMIN login
	    Optional<User> findByLoginIdAndPassword(String loginId, String password);

	    // USER login
	    @Query("SELECT u FROM User u WHERE u.loginId = :loginId AND u.password = :password AND u.network.networkDisplay = :networkDisplay")
	    Optional<User> findByLoginIdAndPasswordAndNetworkDisplay(@Param("loginId") String loginId,
	                                                             @Param("password") String password,
	                                                             @Param("networkDisplay") String networkDisplay);

	    boolean existsByNetwork_NetworkDisplay(String networkDisplay);

	    boolean existsByLoginIdAndNetwork_NetworkDisplay(String loginId, String networkDisplay);

	    Optional<User> findByLoginIdAndNetwork_NetworkDisplay(String loginId, String networkDisplay);
	}