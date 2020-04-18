package com.cm.persistence.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.cm.persistence.domain.Profile;

@Mapper
public interface AuthenticationDao {
	
	@Results({
		@Result(property="userId", column="user_id"),
		@Result(property="name", column="name"),
		@Result(property="email", column="email"),
		@Result(property="phone", column="phone"),
		@Result(property="password", column="password")
	})	
	@Select(Queries.authentication.getProfileByEmail.query)
	public Profile getProfileByEmail(@Param("email") String email);

}
