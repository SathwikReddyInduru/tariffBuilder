package com.xius.TariffBuilder.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xius.TariffBuilder.Entity.ServicePlanPackMap;
import com.xius.TariffBuilder.UserRepository.ServicePlanRepository;

@Service
public class ServicePlanService {

	@Autowired
	private ServicePlanRepository repository;

	public List<ServicePlanPackMap> getPlans(String types) {
		return repository.getPlansByExactType(types);
	}

	public List<ServicePlanPackMap> getAtpPlans(String types) {
		return repository.getAtpPlansByExactType(types);
	 }
}
