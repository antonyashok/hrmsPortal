package com.tm.commonapi.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import com.tm.commonapi.oidc.OIDCConfiguration;
import com.tm.commonapi.web.rest.util.TemplateConfiguration;


@Configuration
@Import(TemplateConfiguration.class)
public class MicroserviceSecurityConfiguration extends OIDCConfiguration {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off

        http.csrf().disable().headers().frameOptions().disable().and().sessionManagement()
                .sessionCreationPolicy(STATELESS).and().authorizeRequests()
                .antMatchers("/cron/manual/**").permitAll()
                .antMatchers("/employee-profile/forgotpassword").permitAll()
                .antMatchers("/employee-profile/invitation/**").permitAll()
                .antMatchers("/employee-profile/template/download/**").permitAll()
                .antMatchers("/my_timesheets_proxy/template/download/**").permitAll()
                .antMatchers("/system/**").access(LOCAL_ADDRESS_EXPRESSION)
                .antMatchers("/v2/api-docs/**", "/swagger-ui.html", "/swagger-resources/**")
                .permitAll().anyRequest().authenticated();

        // @formatter:on
    }

}
