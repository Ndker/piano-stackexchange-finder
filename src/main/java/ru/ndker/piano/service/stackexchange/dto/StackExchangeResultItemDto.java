package ru.ndker.piano.service.stackexchange.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Setters and getters are used to mappers.
@SuppressWarnings("unused")
@JsonIgnoreProperties
public class StackExchangeResultItemDto {

    private StackExchangeResultItemOwnerDto owner;

    @JsonProperty("creation_date")
    private long creationDate;

    private String link;
    private String title;

    @JsonProperty("is_answered")
    private boolean answered;

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public StackExchangeResultItemOwnerDto getOwner() {
        return owner;
    }

    public void setOwner(StackExchangeResultItemOwnerDto owner) {
        this.owner = owner;
    }

    @JsonIgnoreProperties
    public static class StackExchangeResultItemOwnerDto {
        @JsonProperty("display_name")
        private String displayName;

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
    }

}
