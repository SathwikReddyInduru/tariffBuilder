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

	public List<ServicePlanPackMap> getTpPlans(Long networkId, String types) {
		return repository.getPlansByExactType(networkId, types);
	}

	public List<ServicePlanPackMap> getDAtpPlans(Long networkId, String types) {
		return repository.getDAtpPlansByExactType(networkId, types);
	}

	public List<ServicePlanPackMap> getAAtpPlans(Long networkId, String types) {
		return repository.getAAtpPlansByExactType(networkId, types);
	}
}
