package com.cm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cm.core.service.RegistrationService;
import com.cm.persistence.domain.RegistrationDTO;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {
	
	@Autowired
	private RegistrationService registrationService;
	
	@PostMapping("/save")
	public boolean registerNewUser(@RequestBody RegistrationDTO registrationObject){
		
		try{
			
			List<Integer> userIds = registrationService.checkExistingUser(registrationObject.getEmail());
			
	
			
			if(userIds != null && userIds.size() == 0){
				
				registrationService.registerNewUser(registrationObject);
				
				return true;
			}
			else{
				return false;
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}

}
