package com.xius.TariffBuilder.UserService;

import java.util.ArrayList;
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xius.TariffBuilder.Dao.TariffDAO;
import com.xius.TariffBuilder.Entity.TariffEntity;
import com.xius.TariffBuilder.UserRepository.TariffRepository;
 
@Service
public class TariffService {
 
	@Autowired
	private TariffRepository repository;
 
	public List<TariffDAO> getTariffPackages() {
 
		List<Object[]> result = repository.getTariffPackagesStatic();
 
		List<TariffDAO> list = new ArrayList<>();
 
		for (Object[] row : result) {
 
			Long networkId = ((Number) row[0]).longValue();
			String tariffName = (String) row[1];
 
			System.out.println(networkId + "  " + tariffName);
 
			list.add(new TariffDAO(networkId, tariffName));
		}
 
		return list;
	}
	
	
	// fetch pending cards
	public List<TariffEntity> getPendingTariffs() {
	    return repository.findByStatusIsNull();
	}
	public void updateStatus(Long networkId, String status) {
	    TariffEntity tariff = repository.findById(networkId).orElseThrow();
	    tariff.setStatus(status);
	    repository.save(tariff);
	}
}