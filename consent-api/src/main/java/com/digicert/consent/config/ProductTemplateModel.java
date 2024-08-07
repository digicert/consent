package com.digicert.consent.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductTemplateModel {

    private String name;
    private String language;
    private String locale;
    private boolean active;
    private String template;

}
