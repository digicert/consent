package com.digicert.consent.config.model;

public class ConsentTemplate {

    private String localeLanguageId;
    private String title;
    private String content;

    public String getLocaleLanguageId() {
        return localeLanguageId;
    }

    public void setLocaleLanguageId(String localeLanguageId) {
        this.localeLanguageId = localeLanguageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
