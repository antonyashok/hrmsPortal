package com.tm.commonapi.oidc;

import static java.util.Collections.emptyMap;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.mitre.jwt.signer.service.impl.JWKSetCacheService;
import org.mitre.openid.connect.client.service.ServerConfigurationService;
import org.mitre.openid.connect.client.service.impl.HybridServerConfigurationService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.client.RestTemplate;

import com.tm.commonapi.web.rest.util.TemplateConfiguration;


@Configuration
@EnableResourceServer
@Import(TemplateConfiguration.class)
public class OIDCConfiguration extends ResourceServerConfigurerAdapter {

    protected static final String LOCAL_ADDRESS_EXPRESSION =
            "" + "hasIpAddress('10.0.0.0/8') or " + "hasIpAddress('172.16.0.0/12') or "
                    + "hasIpAddress('192.168.0.0/16') or " + "hasIpAddress('127.0.0.1/32')";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(null); // Disabled since Microsoft does not support resource ids.
    }

    @Bean
    public TokenStore tokenStore(final @NotNull @LoadBalanced RestTemplate restTemplate) {
        requireNonNull(restTemplate, "restTemplate cannot be null");
        if (!TemplateConfiguration.LBRestTemplate.class.isAssignableFrom(restTemplate.getClass()))
            throw new IllegalArgumentException();
        return new OIDCTokenStore(new OIDCAccessTokenConverter(restTemplate));
    }

    @Bean
    public JWKSetCacheService jwkSetCacheService() {
        return new JWKSetCacheService();
    }

    @Bean
    public ServerConfigurationService serverConfigurationService(
            final @NotNull OIDCProviders oidcProviders) {
        requireNonNull(oidcProviders, "oidcProviders cannot be null");

        // TODO: How do we adapt if the configuration changes at run-time?
        final HybridServerConfigurationService result = new HybridServerConfigurationService();
        result.setWhitelist(oidcProviders.getWhitelist().stream().collect(toSet()));
        result.setBlacklist(oidcProviders.getBlacklist().stream().collect(toSet()));
        result.setServers(emptyMap());
        return result;
    }

    @Bean
    @ConfigurationProperties("openid-connect")
    public OIDCProviders oidcProviders() {
        return new OIDCProviders();
    }

    public static class OIDCProviders {
        private List<String> whitelist = new ArrayList<>();
        private List<String> blacklist = new ArrayList<>();

        public List<String> getWhitelist() {
            return this.whitelist;
        }

        public void setWhitelist(List<String> whitelist) {
            this.whitelist = whitelist;
        }

        public List<String> getBlacklist() {
            return this.blacklist;
        }

        public void setBlacklist(List<String> blacklist) {
            this.blacklist = blacklist;
        }

    }

}
