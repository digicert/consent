package com.digicert.consent.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsentModel {

    private String localeLanguageId;
    private String title;
    private String content;
    private String type;

}
