package com.digicert.consent.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VerificationEmailRequestDto {

    private String country;
    private String firstName;
    private String lastName;
    private String countryDialingCode;
    private String phoneNumber;
    private String toEmailAddress;
}
