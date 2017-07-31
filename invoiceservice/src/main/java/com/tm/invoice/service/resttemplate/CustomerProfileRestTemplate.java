package com.tm.invoice.service.resttemplate;

import java.math.BigDecimal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.tm.invoice.engagement.dto.CustomerProfileDTO;
import com.tm.invoice.engagement.dto.EngagementDTO;

public class CustomerProfileRestTemplate {

	public static final String HTTP = "http://";

	public static final String BEARER = "Bearer";

	public static final String ENGAGEMENT_GROUP_KEY = "ENGAGEMENTMANAGEMENT";
	
	public static final String TIMEOFF_GROUP_KEY = "TIMESHEETMANAGEMENT";

	private static final String ENGAGEMENT_CUSTOMER_PROFILE_URI = "/engagements/customerprofileEdit?customerId=";

	private static final String ENGAGEMENT_DETAIL_URI="/engagements/engagementEdit/";
	private static final String ENGAGEMENT_UPDATE_URI ="/engagements/updateEngagementDetails?engagementId=";
	
	private String comtrackServiceUrl;

	private String requestedToken;

	private RestTemplate restTemplate;

	Long customerId;

	private final Logger log = LoggerFactory
			.getLogger(CustomerProfileRestTemplate.class);

	public CustomerProfileRestTemplate(@LoadBalanced RestTemplate restTemplate,
			String comtrackServiceUrl, String token, Long customerId) {
		this.restTemplate = restTemplate;
		this.comtrackServiceUrl = comtrackServiceUrl.startsWith(HTTP) ? comtrackServiceUrl
				: new StringBuilder(HTTP).append(comtrackServiceUrl).toString();
		this.requestedToken = token;
		this.customerId = customerId;
	}

	public CustomerProfileDTO getCustomerProfileDTO(){
		ParameterizedTypeReference<CustomerProfileDTO> responseType = new ParameterizedTypeReference<CustomerProfileDTO>() {
		};
		try {
			ResponseEntity<CustomerProfileDTO> resp = restTemplate.exchange(
					new StringBuilder(comtrackServiceUrl)
							.append(ENGAGEMENT_CUSTOMER_PROFILE_URI)
							.append(customerId).toString(), HttpMethod.GET,
					new HttpEntity<>(createHeaders(requestedToken)),
					responseType);
			if (resp.getStatusCode() == HttpStatus.OK) {
				return resp.getBody();
			}
		} catch (Exception exception) {
			log.error(
					"Error collecting CustomerProfile from engagement-services through rest template.",
					exception);
		}
		return new CustomerProfileDTO();
	}

	private static HttpHeaders createHeaders(String token) {
		return new HttpHeaders() {
			private static final long serialVersionUID = 2597867132136283277L;
			{
				set(HttpHeaders.AUTHORIZATION, new StringBuilder(BEARER)
						.append(" ").append(token).toString());
			}
		};
	}
	
    public EngagementDTO getEngagementDetails(UUID engagementId) {
        ParameterizedTypeReference<EngagementDTO> responseType =
                new ParameterizedTypeReference<EngagementDTO>() {};
        try {
            ResponseEntity<EngagementDTO> resp = restTemplate.exchange(
                    new StringBuilder(comtrackServiceUrl).append(ENGAGEMENT_DETAIL_URI)
                            .append(engagementId).toString(),
                    HttpMethod.GET, new HttpEntity<>(createHeaders(requestedToken)), responseType);
            if (resp.getStatusCode() == HttpStatus.OK) {
                return resp.getBody();
            }
        } catch (Exception exception) {
            log.error(
                    "Error collecting engagement details from engagement-service through rest template.",
                    exception);
        }
        return new EngagementDTO();
    }
    
    public Integer updateEngagementDetails(UUID engagementId, String type, UUID poId,
            String poNumber, BigDecimal initialAmount, BigDecimal totalAmount,
            BigDecimal balanceAmount, String engmtDate, String issueDate) {
        ParameterizedTypeReference<Integer> responseType =
                new ParameterizedTypeReference<Integer>() {};
        try {
            ResponseEntity<Integer> resp = restTemplate.exchange(
                    new StringBuilder(comtrackServiceUrl).append(ENGAGEMENT_UPDATE_URI)
                            .append(engagementId).append("&type=").append(type)
                           .append("&poId=").append(poId).append("&poNumber=").append(poNumber)
                            .append("&initialAmount=").append(initialAmount).append("&totalAmount=")
                            .append(totalAmount).append("&balanceAmount=")
                            .append(balanceAmount)
                            .append("&engmtDate=")
                            .append(engmtDate)
                            .append("&poIssueDate=")
                            .append(issueDate).toString(),
                    HttpMethod.PUT, new HttpEntity<>(createHeaders(requestedToken)), responseType);

            if (resp.getStatusCode() == HttpStatus.OK) {
                return resp.getBody();
            }
        } catch (Exception exception) {
            log.error(
                    "Error while updating engagement details from engagement-service through rest template "
                            + exception);
        }
        return -1;
    }
    
}
