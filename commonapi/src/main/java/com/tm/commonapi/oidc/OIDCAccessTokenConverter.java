/*
 * Cloud Foundry 2012.02.03 Beta Copyright (c) [2009-2012] VMware, Inc. All Rights Reserved.
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License"). You may
 * not use this product except in compliance with the License.
 *
 * This product includes a number of subcomponents with separate copyright notices and license
 * terms. Your use of these subcomponents is subject to the terms and conditions of the
 * subcomponent's license, as noted in the LICENSE file.
 */
package com.tm.commonapi.oidc;

import static java.util.Objects.requireNonNull;

import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.tm.commonapi.web.rest.util.TemplateConfiguration;

/**
 * Helper that translates between JWT encoded token values and OAuth authentication information (in
 * both directions). Also acts as a {@link TokenEnhancer} when tokens are granted.
 * 
 * @see TokenEnhancer
 * @see AccessTokenConverter
 * 
 */
public class OIDCAccessTokenConverter
        implements TokenEnhancer, AccessTokenConverter, InitializingBean {

    /**
     * Field name for token id.
     */
    public static final String TOKEN_ID = AccessTokenConverter.JTI;

    /**
     * Field name for access token id.
     */
    public static final String ACCESS_TOKEN_ID = AccessTokenConverter.ATI;

    private AccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
    private final @NotNull RestTemplate restTemplate;

    @Inject
    public OIDCAccessTokenConverter(final @NotNull @LoadBalanced RestTemplate restTemplate) {
        this.restTemplate = requireNonNull(restTemplate, "restTemplate cannot be null");
        if (!TemplateConfiguration.LBRestTemplate.class.isAssignableFrom(restTemplate.getClass()))
            throw new IllegalArgumentException();
    }

    /**
     * @param tokenConverter the tokenConverter to set
     */
    public void setAccessTokenConverter(AccessTokenConverter tokenConverter) {
        this.tokenConverter = tokenConverter;
    }

    /**
     * @return the tokenConverter in use
     */
    public AccessTokenConverter getAccessTokenConverter() {
        return tokenConverter;
    }

    @Override
    public Map<String, ?> convertAccessToken(OAuth2AccessToken token,
            OAuth2Authentication authentication) {
        return tokenConverter.convertAccessToken(token, authentication);
    }

    @Override
    public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(value);
        Map<String, Object> info = new HashMap<String, Object>(map);
        info.remove(EXP);
        info.remove(AUD);
        info.remove(CLIENT_ID);
        info.remove(SCOPE);
        if (map.containsKey(EXP)) {
            final Object exp = map.get(EXP);
            if (exp instanceof Date) {
                token.setExpiration((Date) exp);
            }
            if (exp instanceof Long) {
                token.setExpiration(new Date((Long) exp * 1000L));
            }
            if (exp instanceof Integer) {
                token.setExpiration(new Date((Integer) exp * 1000L));
            }
        }
        if (map.containsKey(JTI)) {
            info.put(JTI, map.get(JTI));
        }
        token.setScope(extractScope(map));
        token.setAdditionalInformation(info);
        return token;
    }

    private Set<String> extractScope(Map<String, ?> map) {
        Set<String> scope = Collections.emptySet();
        if (map.containsKey(SCOPE)) {
            Object scopeObj = map.get(SCOPE);
            if (String.class.isInstance(scopeObj)) {
                scope = Collections.singleton(String.class.cast(scopeObj));
            } else if (Collection.class.isAssignableFrom(scopeObj.getClass())) {
                @SuppressWarnings("unchecked")
                Collection<String> scopeColl = (Collection<String>) scopeObj;
                scope = new LinkedHashSet<String>(scopeColl); // Preserve ordering
            }
        }
        return scope;
    }

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        return tokenConverter.extractAuthentication(map);
    }

    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
            OAuth2Authentication authentication) {
        return accessToken;
    }

    boolean isRefreshToken(OAuth2AccessToken token) {
        return token.getAdditionalInformation().containsKey(ACCESS_TOKEN_ID);
    }

    JWTClaimsSet readClaims(String token) throws RestClientException {
        return getUserInfo(token);
    }

    private JWTClaimsSet getUserInfo(final @NotNull String token) throws RestClientException {
        try {
            return JWTParser.parse(token).getJWTClaimsSet();
        } catch (ParseException e) {
            throw new InvalidTokenException("Couldn't parse token: " + token, e);
        }
    }

    public void afterPropertiesSet() throws Exception {
        /* nothing to do */
    }

}
