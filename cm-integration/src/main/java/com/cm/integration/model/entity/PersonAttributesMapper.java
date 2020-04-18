package com.cm.integration.model.entity;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;



public class PersonAttributesMapper implements AttributesMapper<JwtUser> {
	public JwtUser mapFromAttributes(Attributes attrs) throws NamingException {
		JwtUser jwtuser = new JwtUser();
		Attribute cn = attrs.get("cn");
		if(cn !=null) {
			String[] nameArray = ((String) cn.get()).split(",");
			jwtuser.setFirstName(nameArray[0]);
			/*if(nameArray.length > 1) {
				jwtuser.setLastName(nameArray[1]);
			}*/
		}
		Attribute email = attrs.get("mail");
		if(email !=null) {
			jwtuser.setEmail((String) email.get());
		}
		Attribute phoneNumber = attrs.get("telephoneNumber");
		if(phoneNumber!=null) {
			jwtuser.setPhoneNumber((String)phoneNumber.get());
		}
		Attribute sn = attrs.get("sn");
		if (sn != null) {
			jwtuser.setLastName((String) sn.get());
		}
		return jwtuser;
	}
}
