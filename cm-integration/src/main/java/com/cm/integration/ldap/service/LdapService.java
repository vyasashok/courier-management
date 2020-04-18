package com.cm.integration.ldap.service;

import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.SearchScope;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import com.cm.integration.model.entity.JwtUser;
import com.cm.integration.model.entity.PersonAttributesMapper;


@Service
public class LdapService {
	
	@Autowired
	private LdapTemplate ldapTemplate;
	
	public JwtUser getUserDetails(String username){
		
		LdapQuery query = query().searchScope(SearchScope.SUBTREE).timeLimit(30000).countLimit(1).attributes("cn", "mail")
				.base(LdapUtils.emptyLdapName()).where("objectclass").is("person").and("uid").like(username).and("uid")
				.isPresent();
		JwtUser jwtuser = null;
		List<JwtUser> list = ldapTemplate.search(query, new PersonAttributesMapper());
		if (list != null && !list.isEmpty()) {
			jwtuser = list.get(0);
		}
		return jwtuser;
	}

}
