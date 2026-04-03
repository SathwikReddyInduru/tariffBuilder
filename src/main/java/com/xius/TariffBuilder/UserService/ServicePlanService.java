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

	public List<ServicePlanPackMap> getDAtpPlans(String types) {
		return repository.getDAtpPlansByExactType(types);
	 }
	
	public List<ServicePlanPackMap> getAAtpPlans(String types) {
		return repository.getAAtpPlansByExactType(types);
	 }
}
