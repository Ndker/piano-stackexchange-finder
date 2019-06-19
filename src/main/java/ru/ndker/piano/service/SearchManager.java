package ru.ndker.piano.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import ru.ndker.piano.model.SearchResult;
import ru.ndker.piano.model.SearchResultItem;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

@Component
public class SearchManager {

    private final int EXPIRED_TASK_MINUTES = 30;

    private final ThreadPoolTaskScheduler scheduler;
    private final SearchClient client;

    private ConcurrentHashMap<String, String> tasks = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, SearchResult> results = new ConcurrentHashMap<>();

    @Autowired
    public SearchManager(@Qualifier("threadPoolTaskScheduler") ThreadPoolTaskScheduler scheduler,
                         SearchClient client) {
        this.scheduler = scheduler;
        this.client = client;
    }

    /**
     * Add task for searching by text
     *
     * @param text text for searching
     * @return unique id fo request
     */
    public String startSearching(String text) {
        var id = UUID.randomUUID().toString();
        tasks.put(id, text);
        // Schedule immediately searching by text.
        var time = Calendar.getInstance();
        scheduler.schedule(() -> startSearchTask(id), time.getTime());

        // Schedule deleting task.
        time.add(Calendar.MINUTE, EXPIRED_TASK_MINUTES);
        scheduler.schedule(() -> deleteExpiredTask(id), time.getTime());
        return id;
    }

    /**
     * Checking availability of results.
     * After receive of request, at first obtain a fixed number of questions.
     * If the user has reached off limit, try to get next questions from service.
     *
     * @param id   unique id fo request
     * @param page requested page.
     * @param size requested questions per page.
     * @return availability of results.
     */
    public boolean isResultReady(String id, int page, int size) {
        var result = results.get(id);
        if (result == null)
            return false;
        else if (result.getLocked().get())
            return false;
        else if (result.getHasNext().get() && (page - 1) * size >= result.getData().size()) {
            result.getLocked().set(true);
            // Schedule immediately continue searching by text.
            scheduler.schedule(() -> nextSearchTask(id, result.getData().size(), page * size), new Date());
            return false;
        } else
            return true;
    }

    /**
     * Return results for request.
     *
     * @param id unique id fo request
     * @return object of search result.
     */
    public SearchResult getResult(String id) {
        return results.get(id);
    }

    /**
     * Start searching questions by text.
     *
     * @param id unique id for search request
     */
    private void startSearchTask(String id) {
        BiConsumer<List<SearchResultItem>, Boolean> onResult = (items, hasNext) -> {
            var result = new SearchResult();
            result.getData().addAll(items);
            result.getHasNext().set(hasNext);
            results.put(id, result);
        };

        client.searchByText(tasks.get(id), onResult);
    }

    /**
     * After receive of request, at first obtain a fixed number of questions.
     * If the user has reached off limit, try to get next questions from service.
     *
     * @param id            unique id for search request
     * @param currentSize   current size of list results.
     * @param requestedSize min value for size of list results.
     */
    private void nextSearchTask(String id, int currentSize, int requestedSize) {
        BiConsumer<List<SearchResultItem>, Boolean> onResult = (items, hasNext) -> {
            var result = results.get(id);
            if (result != null) {
                synchronized (result) {
                    result.getData().addAll(items);
                    result.getHasNext().set(hasNext);
                    result.getLocked().set(false);
                }
            }
        };

        client.nextByText(tasks.get(id), currentSize, requestedSize, onResult);
    }

    /**
     * Clear tasks after 30 minutes after searching.
     */
    private void deleteExpiredTask(String id) {
        tasks.remove(id);
        results.remove(id);
    }

}
