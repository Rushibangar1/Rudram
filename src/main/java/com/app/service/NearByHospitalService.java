package com.app.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.Repo.NearByHospitalRepository;
import com.app.entity.NearByBloodHospitals;


@Service
public class NearByHospitalService {
	
	 @Autowired
	    private NearByHospitalRepository hospitalRepository;

	   
		public List<NearByBloodHospitals> getHospitalByStateAndCity(Long stateId, Long cityId) {
			return hospitalRepository.findByStateIdAndCityId(stateId, cityId);
		}

}
