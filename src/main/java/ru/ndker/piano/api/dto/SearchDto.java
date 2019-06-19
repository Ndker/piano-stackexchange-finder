package ru.ndker.piano.api.dto;

public class SearchDto {
    private final String requestId;

    public SearchDto(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }
}
