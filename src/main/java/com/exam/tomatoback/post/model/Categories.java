package com.exam.tomatoback.post.model;

public enum Categories {
    DIGITAL_DEVICE("디지털 기기"),
    HOME_APPLIANCE("생활가전"),
    FURNITURE("가구/인테리어"),
    KITCHEN("생활/주방"),
    KIDS("유아동");

    private final String label;

    Categories(String label) {
        this.label = label;
    }

    public String getLabel() {
            return label;
    }
}

