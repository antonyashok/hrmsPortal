package com.tm.commonapi.web.rest.util;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TemplateConfiguration {

    /**
     * Use this template for calls to external services that do not register with the Discovery
     * Service
     * 
     * @return
     */
    @Bean
    @Primary
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Use this template for calls to internal services that register with the Discovery Service
     * <p>
     * 
     * <pre>
     * &#064;Autowired
     * &#064;LoadBalanced
     * private RestTemplate restTemplate;
     * </pre>
     * 
     * </p>
     * <p>
     * 
     * <pre>
     * &#064;Inject
     * void foo(@LoadBalanced RestTemplate restTemplate);
     * </pre>
     * 
     * </p>
     */
    @Bean
    @LoadBalanced
    public RestTemplate loadBalancedRestTemplate() {
        return new LBRestTemplate();
    }

    public static class LBRestTemplate extends RestTemplate {
    }
}
