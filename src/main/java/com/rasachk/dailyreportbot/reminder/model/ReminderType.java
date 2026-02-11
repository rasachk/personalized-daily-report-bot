package com.rasachk.dailyreportbot.reminder.model;

import lombok.Getter;

@Getter
public enum ReminderType {
    WEATHER_FORCAST("Weather Forcast"),
    CURRENCY("Currency"),
    PERSONAL("Personal"),
    SPORTS("Sports");

    private final String title;

    ReminderType(String title) {
        this.title = title;
    }

    public static ReminderType fromTitle(String title) {
        for (ReminderType type : values()) {
            if (type.title.equalsIgnoreCase(title)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown reminder type: " + title);
    }
}
