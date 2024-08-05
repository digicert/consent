package com.digicert.consent.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnbrandedEmailMessageResource {
    private MailToResource to;
    private Map<String, Object> data;

    @JsonProperty("ignore_blacklist")
    private boolean ignoreBlacklist;

    @JsonProperty("transaction_id")
    private String transactionId;
}
