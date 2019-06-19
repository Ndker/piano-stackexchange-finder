package ru.ndker.piano.service.stackexchange.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties
public class StackExchangeResultDto {

    private List<StackExchangeResultItemDto> items;

    @JsonProperty("has_more")
    private boolean hasMore;

    public List<StackExchangeResultItemDto> getItems() {
        return items;
    }

    public void setItems(List<StackExchangeResultItemDto> items) {
        this.items = items;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}
