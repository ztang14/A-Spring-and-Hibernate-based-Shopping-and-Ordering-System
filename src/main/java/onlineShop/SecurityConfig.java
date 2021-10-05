package onlineShop;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;
	//��web.xml�������Filter��tomcat���Զ���Filter��ʼ��ʵ���������н�����request��response���ᾭ��Filter
	//Filter����check user name��password
	//Filter��Ҫʵ��ͨ�����ݿ�check authority�Ĳ�����Ҫ��SecurityConfig�ڶ���
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.formLogin()
				.loginPage("/login")
				
			.and()//andִ����һ�������������һ����object convert to HttpSecurity
			.authorizeRequests()
			.antMatchers("/cart/**").hasAuthority("ROLE_USER")
			.antMatchers("/get*/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
			.antMatchers("/admin*/**").hasAuthority("ROLE_ADMIN")
			.anyRequest().permitAll()
			.and()
			.logout()
				.logoutUrl("/logout");
		//  /admin* ->/admindw
       //	/admin** ->/admin/a/s		
	}
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication().withUser("1111@gmail.com").password("2222").authorities("ROLE_ADMIN");
		//����һ��admin���ڴ浱��
		//���û��ƥ���Ͼͽ��н������Ĳ���
		auth
			.jdbcAuthentication()
			.dataSource(dataSource)//datasource��ApplicationConfig���涨��
			.usersByUsernameQuery("SELECT emailId, password, enabled FROM users WHERE emailId=?")
			//select����������framework�����о�����check������������û����ʾ��ֻ�Ǵ������
			.authoritiesByUsernameQuery("SELECT emailId, authorities FROM authorities WHERE emailId=?");
		
	}
	
}
