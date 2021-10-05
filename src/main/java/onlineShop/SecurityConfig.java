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
	//在web.xml里面会有Filter，tomcat会自动把Filter初始化实例化，所有进来的request和response都会经过Filter
	//Filter用于check user name和password
	//Filter需要实现通过数据库check authority的操作需要在SecurityConfig内定义
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.formLogin()
				.loginPage("/login")
				
			.and()//and执行了一步操作，将最后一步的object convert to HttpSecurity
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
		//保存一个admin在内存当中
		//如果没有匹配上就进行接下来的操作
		auth
			.jdbcAuthentication()
			.dataSource(dataSource)//datasource在ApplicationConfig里面定义
			.usersByUsernameQuery("SELECT emailId, password, enabled FROM users WHERE emailId=?")
			//select的三个会在framework定义中具体再check，但是在这里没有显示，只是传到后端
			.authoritiesByUsernameQuery("SELECT emailId, authorities FROM authorities WHERE emailId=?");
		
	}
	
}
