package com.cm.persistence.dao;

public interface Queries {
	
	interface registration {
		interface registerNewUser{
			String query = "insert into  user_details(name, email, phone, password) values(#{name}, #{email}, #{phone}, #{password})";
		}
		
		interface checkExistinUser{
			String query = "select user_id from user_details where email=#{email}";
		}
	}
	
	interface authentication {
		interface getProfileByEmail{
			String query = "select * from user_details where email=#{email}";
		}
	}

}
