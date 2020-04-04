package com.cm.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cm.persistence.dao.RegistrationDao;
import com.cm.persistence.domain.RegistrationDTO;

@Transactional
@Service
public class RegistrationService {
	
	@Autowired
	private RegistrationDao registrationDao;
	
	
	@Transactional(readOnly = false)
	public Integer registerNewUser(RegistrationDTO registrationObject){
		return registrationDao.registerNewUser(registrationObject);
	}
	
	@Transactional(readOnly = false)
	public List<Integer> checkExistingUser(String email){
		return registrationDao.checkExistingUser(email);
	}

}
