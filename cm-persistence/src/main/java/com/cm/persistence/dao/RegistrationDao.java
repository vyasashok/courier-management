package com.cm.persistence.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cm.persistence.domain.RegistrationDTO;

@Mapper
public interface RegistrationDao {

	@Insert(Queries.registration.registerNewUser.query)
	@Options(useGeneratedKeys = true, keyProperty = "user_id")
	public Integer registerNewUser(RegistrationDTO registrationObject);
	
	@Select(Queries.registration.checkExistinUser.query)
	public List<Integer> checkExistingUser(@Param("email") String email);
	
}
