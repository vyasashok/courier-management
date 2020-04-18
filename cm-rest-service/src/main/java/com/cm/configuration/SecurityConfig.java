package com.cm.configuration;

import java.nio.charset.Charset;
import java.util.Collection;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.SpringSecurityLdapTemplate;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

import com.cm.filter.CORSFilter;
import com.cm.filter.JwtTokenAuthenticationFilter;
import com.cm.filter.RestAccessDeniedHandler;
import com.cm.filter.SecurityAuthenticationEntryPoint;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	

	
	public SecurityConfig() {
		/*
		 * Ignores the default configuration, useless in our case (session
		 * management, etc..)
		 */
		super(true);
	}

	@Bean
	LdapAuthoritiesPopulator ldapAuthoritiesPopulator() throws Exception {

		/*
		 * Specificity here : we don't get the Role by reading the members of
		 * available groups (which is implemented by default in Spring security
		 * LDAP), but we retrieve the groups from the field memberOf of the
		 * user.
		 */
		class MyLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

			SpringSecurityLdapTemplate ldapTemplate;
			public final String[] GROUP_ATTRIBUTE = { "cn" };
			public final String GROUP_MEMBER_OF = "memberof";

			MyLdapAuthoritiesPopulator(ContextSource contextSource) {
				ldapTemplate = new SpringSecurityLdapTemplate(contextSource);
			}

			@Override
			public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData,
					String username) {

				String[] groupDns = userData.getStringAttributes(GROUP_MEMBER_OF);
				String roles = "";

				return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
			}
		}

		return new MyLdapAuthoritiesPopulator(contextSource());
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.ldapAuthentication().userDnPatterns("uid={0},ou=people")
				.ldapAuthoritiesPopulator(ldapAuthoritiesPopulator()).contextSource(contextSource());

	}
	
	@Bean
	public BaseLdapPathContextSource contextSource() throws Exception {
		DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource("ldap://localhost:8389/dc=springframework,dc=org");
		//contextSource.setUserDn(LDAP_USDN);
		//contextSource.setPassword(LDAP_PASSWORD);
		return contextSource;
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		/*
		 * Overloaded to expose Authenticationmanager's bean created by
		 * configure(AuthenticationManagerBuilder). This bean is used by the
		 * AuthenticationController.
		 */
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		/*
		 * the secret key used to signe the JWT token is known exclusively by
		 * the server. With Nimbus JOSE implementation, it must be at least 256
		 * characters longs.
		 */
		String secret = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("secret.key"),
				Charset.defaultCharset());

		httpSecurity
				/*
				 * Filters are added just after the ExceptionTranslationFilter
				 * so that Exceptions are catch by the exceptionHandling()
				 * Further information about the order of filters, see
				 * FilterComparator
				 */
				.addFilterAfter(jwtTokenAuthenticationFilter("/**", secret), ExceptionTranslationFilter.class)
				.addFilterAfter(new CORSFilter(), ChannelProcessingFilter.class)
				/*
				 * Exception management is handled by the
				 * authenticationEntryPoint (for exceptions related to
				 * authentications) and by the AccessDeniedHandler (for
				 * exceptions related to access rights)
				 */
				.exceptionHandling().authenticationEntryPoint(new SecurityAuthenticationEntryPoint())
				.accessDeniedHandler(new RestAccessDeniedHandler()).and()
				/*
				 * anonymous() consider no authentication as being anonymous
				 * instead of null in the security context.
				 */
				.anonymous().and()
				/* No Http session is used to get the security context */
				.sessionManagement().sessionCreationPolicy(STATELESS).and().authorizeRequests()
				/*
				 * All access to the authentication service are permitted
				 * without authentication (actually as anonymous)
				 */
				 .antMatchers(" ").permitAll()
//				.antMatchers(unsecureduris).permitAll()
				/*
				 * All the other requests need an authentication. Role access is
				 * done on Methods using annotations like @PreAuthorize
				 */
				.anyRequest().authenticated();
	}

	private JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter(String path, String secret) {
		return new JwtTokenAuthenticationFilter(path, secret);
	}
    
}
