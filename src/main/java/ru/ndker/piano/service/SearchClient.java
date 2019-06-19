package ru.ndker.piano.service;

import java.util.function.BiConsumer;

public interface SearchClient {
    /**
     * Search questions by text.
     *
     * @param text     text for searching.
     * @param onResult handler for processing searching results.
     */
    void searchByText(String text, BiConsumer onResult);

    /**
     * After receive of request, at first obtain a fixed number of questions.
     * If the user has reached off limit, try to get next questions from service.
     *
     * @param text          text for searching.
     * @param currentSize   current size of list results.
     * @param requestedSize min value for size of list results.
     * @param onResult      handler for processing searching results.
     */
    void nextByText(String text, int currentSize, int requestedSize, BiConsumer onResult);
}
