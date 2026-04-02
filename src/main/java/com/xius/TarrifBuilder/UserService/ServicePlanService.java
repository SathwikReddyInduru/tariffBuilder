package com.xius.TarrifBuilder.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xius.TarrifBuilder.Entity.ServicePlanPackMap;
import com.xius.TarrifBuilder.UserRepository.ServicePlanRepository;

@Service
public class ServicePlanService {

	 @Autowired
	    private ServicePlanRepository repository;
	 public List<ServicePlanPackMap> getPlans(String types) {
		    return repository.getPlansByExactType(types);
		}
}

