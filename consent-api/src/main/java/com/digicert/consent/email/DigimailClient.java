package com.digicert.consent.email;

import com.digicert.consent.config.DigimailConfig;
import com.digicert.consent.exceptions.DigimailException;
import com.digicert.consent.logging.LogUtils;
import com.digicert.consent.utilities.BaseRestClient;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class DigimailClient extends BaseRestClient {



    public DigimailClient(RestTemplate restTemplate, DigimailConfig config) {
        super(restTemplate, config.getBaseUrl(), config.getPath());
    }

    public void sendEmailTemplate(UnbrandedEmailMessageResource emailMessageResource, String templateName) {

        try {
            LogUtils.info("Sending Digimail email app=consent-api");
            String path = getUrl("/send/" + templateName);

            URI uri = UriComponentsBuilder.fromUriString(path)
                    .build()
                    .toUri();

            RequestEntity<UnbrandedEmailMessageResource> request = RequestEntity
                    .post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(emailMessageResource);

            ResponseEntity<Void> response = exchangeWithTimeLogging(request, Void.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new DigimailException("Digi-mail email failed " + response.getStatusCode());
            }
            LogUtils.info("app=consent-api Success sending digi-mail template email");
        } catch (Exception e) {
            LogUtils.error("app=consent-api Failure sending digi-mail template email. Reason: {}", e.getMessage(), e);
            throw handleException(e);
        }
    }

    private DigimailException handleException(Exception exception) {
        if (HttpStatusCodeException.class.isAssignableFrom(exception.getClass())) {
            HttpStatusCodeException ex = (HttpStatusCodeException) exception;
            LogUtils.error("app=consent-api HttpStatusCodeException during digi-mail email sned with message {}. reason: {}, status code: {}. ",
                    ex.getMessage(), ex.getResponseBodyAsString(), ex.getStatusCode());
            String message = String.format("Digi-mail send email template request failed with response : %s",
                    ex.getMessage());
            return new DigimailException(message);
        }
        return new DigimailException(exception.getMessage());
    }

}
