package com.lingohammer.aws.auth.data;

import java.util.HashMap;
import java.util.Map;

public class UserInfo extends IsFlawed {
    private Map<String, String> attributes = new HashMap<>();

    private String userIdentifier;

    private String email;

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }


    public void addAttribute(String name, String value) {
        attributes.put(name, value);
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
}
