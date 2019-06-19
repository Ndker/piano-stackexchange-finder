package ru.ndker.piano.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.ndker.piano.api.dto.SearchDto;
import ru.ndker.piano.api.dto.SearchResultDto;
import ru.ndker.piano.api.dto.SearchResultItemDto;
import ru.ndker.piano.service.SearchManager;

import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/api")
public class SearchController {

    private final ObjectMapper mapper = new ObjectMapper();

    private SearchManager searchManager;

    @Autowired
    public SearchController(SearchManager searchManager) {
        this.searchManager = searchManager;
    }

    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SearchDto> search(@RequestParam String text) {
        return new ResponseEntity<>(
                new SearchDto(searchManager.startSearching(text)),
                HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/search/result", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SearchResultDto> search(@RequestParam String requestId,
                                                  @RequestParam(required = false, defaultValue = "1") Integer page,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        if (page <= 0 || size <= 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (searchManager.isResultReady(requestId, page, size)) {
            var result = searchManager.getResult(requestId);
            boolean hasNext = result.getData().size() > page * size || result.getHasNext().get();
            int totalPages = !result.getHasNext().get() ?
                    result.getData().size() / size + ((result.getData().size() % size == 0) ? 0 : 1) :
                    0;

            return new ResponseEntity<>(
                    new SearchResultDto(
                            result.getData().stream()
                                    .skip((page - 1) * size)
                                    .limit(size)
                                    .map(r -> mapper.convertValue(r, SearchResultItemDto.class))
                                    .collect(Collectors.toList()),
                            page,
                            totalPages,
                            hasNext
                    ),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                    HttpStatus.ACCEPTED
            );
        }
    }

}
