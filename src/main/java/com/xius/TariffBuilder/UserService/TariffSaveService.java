package com.xius.TariffBuilder.UserService;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.xius.TariffBuilder.Dao.SavePackageRequest;
import com.xius.TariffBuilder.Entity.PeriodicChargeInfo;
import com.xius.TariffBuilder.Entity.TariffPackage;
import com.xius.TariffBuilder.Entity.TariffPublicityMap;
import com.xius.TariffBuilder.Entity.TariffServicePackMap;
import com.xius.TariffBuilder.UserRepository.PeriodicChargeRepository;
import com.xius.TariffBuilder.UserRepository.TariffPackageRepository;
import com.xius.TariffBuilder.UserRepository.TariffPublicityMapRepository;
import com.xius.TariffBuilder.UserRepository.TariffServicePackMapRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TariffSaveService {

    private final TariffPackageRepository packageRepo;
    private final TariffPublicityMapRepository publicityRepo;
    private final TariffServicePackMapRepository serviceRepo;
    private final PeriodicChargeRepository periodicRepo;

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("MM/dd/yyyy");


    @Transactional
    public Long savePackage(
            SavePackageRequest req,
            Long networkId,
            String username)
    {

        /* 1️⃣ CS_RAT_TARIFF_PACKAGE */

        TariffPackage pkg = new TariffPackage();

        pkg.setTariffPackageDesc(
                req.getTariffPackageDesc().toUpperCase());

        pkg.setNetworkId(networkId);

        pkg.setEndDate(
                LocalDate.parse(
                        req.getEndDate(),
                        formatter));

        pkg.setChargeId(
                String.valueOf(req.getChargeId()));

        pkg.setPackageType(
                req.getPackageType());

        pkg.setSubscriberCategoryId(
                req.getSubscriberCategoryId());

        pkg.setIsCorporateYn(
                req.getIsCorporateYn());

        pkg.setTariffPackCategory(
                req.getTariffPackCategory());

        pkg.setPublicityId(
                req.getPublicityId());

        TariffPackage saved =
                packageRepo.save(pkg);

        Long tariffPackageId =
                saved.getTariffPackageId();



        /* 2️⃣ CS_RAT_PERIODIC_CHARGE_INFO */

        if(!periodicRepo.existsByChargeIdAndNetworkId(
                String.valueOf(req.getChargeId()),
                networkId))
        {

            PeriodicChargeInfo charge =
                    new PeriodicChargeInfo();

            charge.setChargeId(
                    String.valueOf(req.getChargeId()));

            charge.setNetworkId(networkId);

            charge.setServiceType(1);

            charge.setRentalType("MONTHLY");

            charge.setRentalPeriod(30);

            charge.setCreatedBy(username);

            periodicRepo.save(charge);
        }



        /* 3️⃣ CS_RAT_TPID_VS_PUBLICITYID */

        TariffPublicityMap pub =
                new TariffPublicityMap();

        pub.setNetworkId(networkId);

        pub.setTariffPackageId(
                tariffPackageId);

        pub.setTariffPackageDesc(
                req.getTariffPackageDesc().toUpperCase());

        pub.setPublicityId(
                req.getPublicityId());

        pub.setRecordInsertedBy(username);

        pub.setRecInsertedDate(
                LocalDate.now());

        publicityRepo.save(pub);



        /* 4️⃣ TP */

        TariffServicePackMap tp =
                new TariffServicePackMap();

        tp.setTariffPackageId(
                tariffPackageId);

        tp.setServicePackageId(
                req.getTariffPlanId());

        tp.setNetworkId(networkId);

        tp.setTariffPlanType("TP");

        serviceRepo.save(tp);



        /* 5️⃣ DATP */

        if(req.getDefaultAtps()!=null)
        {
            for(var item : req.getDefaultAtps())
            {

                TariffServicePackMap datp =
                        new TariffServicePackMap();

                datp.setTariffPackageId(
                        tariffPackageId);

                datp.setServicePackageId(
                        item.getServicePackageId());

                datp.setNetworkId(networkId);

                datp.setTariffPlanType("DATP");

                datp.setChargeId(
                        String.valueOf(item.getChargeId()));

                datp.setPriority(1);

                datp.setServiceDuration(30);

                serviceRepo.save(datp);
            }
        }



        /* 6️⃣ AATP */

        if(req.getAllowedAtps()!=null)
        {
            for(var item : req.getAllowedAtps())
            {

                TariffServicePackMap aatp =
                        new TariffServicePackMap();

                aatp.setTariffPackageId(
                        tariffPackageId);

                aatp.setServicePackageId(
                        item.getServicePackageId());

                aatp.setNetworkId(networkId);

                aatp.setTariffPlanType("AATP");

                aatp.setChargeId(
                        String.valueOf(item.getChargeId()));

                serviceRepo.save(aatp);
            }
        }

        return tariffPackageId;
    }
}