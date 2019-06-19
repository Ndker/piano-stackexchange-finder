package ru.ndker.piano.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SearchResult {

    private List<SearchResultItem> data = new ArrayList<>();

    private AtomicBoolean hasNext = new AtomicBoolean(false);
    private AtomicBoolean locked = new AtomicBoolean(false);

    public SearchResult() {
    }

    public List<SearchResultItem> getData() {
        return data;
    }

    public AtomicBoolean getHasNext() {
        return hasNext;
    }

    public AtomicBoolean getLocked() {
        return locked;
    }
}
