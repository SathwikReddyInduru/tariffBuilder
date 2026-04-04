package com.xius.TariffBuilder.UserService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xius.TariffBuilder.Dao.TariffDao;
import com.xius.TariffBuilder.Entity.TariffEntity;
import com.xius.TariffBuilder.UserRepository.TariffRepository;

@Service
public class TariffService {

	@Autowired
	private TariffRepository repository;

	public List<TariffDao> getTariffPackages() {

		List<Object[]> result = repository.getTariffPackagesStatic();

		List<TariffDao> list = new ArrayList<>();

		for (Object[] row : result) {

			Long networkId = ((Number) row[0]).longValue();
			String tariffName = (String) row[1];
			Long tariffPackageId = ((Number) row[2]).longValue();
			String status = (String) row[3];
			System.out.println(networkId + "  " + tariffName + "  " + status);

			list.add(new TariffDao(networkId, tariffName, status, tariffPackageId));
		}

		return list;
	}

	// fetch pending cards
	public List<TariffEntity> getPendingTariffs() {
		return repository.findByStatusIsNull();
	}

	public void updateStatus(Long tariffPackageId, String status) {
		System.out.println("Updating status for Network ID: " + tariffPackageId + " to " + status);
		TariffEntity tariff = repository.findById(tariffPackageId).orElseThrow();
		System.out.println("Updating status for Network ID2: " + tariffPackageId + " to " + status);
		tariff.setStatus(status);
		repository.save(tariff);
	}
}