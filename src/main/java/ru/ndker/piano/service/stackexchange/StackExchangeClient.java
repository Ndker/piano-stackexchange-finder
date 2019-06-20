package ru.ndker.piano.service.stackexchange;

import org.apache.http.impl.client.HttpClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ru.ndker.piano.model.SearchResultItem;
import ru.ndker.piano.service.SearchClient;
import ru.ndker.piano.service.stackexchange.dto.StackExchangeResultDto;
import ru.ndker.piano.service.stackexchange.dto.StackExchangeResultItemDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.function.BiConsumer;

@Component
public class StackExchangeClient implements SearchClient {

    private final int STACK_EXCHANGE_PAGESIZE = 100;

    private final RestTemplate restTemplate;

    @Value("${stackexchange.url}")
    private String url;

    public StackExchangeClient() {
        // Use apache http clients for automatic gzip parsing.
        var clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().build());
        this.restTemplate = new RestTemplate(clientHttpRequestFactory);
    }

    @Override
    public void searchByText(String text, BiConsumer onResult) {
        System.out.println(text);
        var result = search(text, 1);
        var list = new ArrayList<SearchResultItem>();
        result.getItems().forEach(r -> list.add(mapItem(r)));

        onResult.accept(list, result.isHasMore());
    }

    @Override
    public void nextByText(String text, int currentSize, int requestedSize, BiConsumer onResult) {
        int page = currentSize / STACK_EXCHANGE_PAGESIZE + ((currentSize % STACK_EXCHANGE_PAGESIZE == 0) ? 0 : 1);
        int size = currentSize;
        boolean hasNext = true;
        var list = new ArrayList<SearchResultItem>();

        // it is a bit bad here that if we were asked to immediately 20th page,
        // then we will get to it each page, starting with first page, for example.
        // So for simplicity, we assume that client can only request next or previous page.
        while (hasNext && size < requestedSize) {
            page++;
            var result = search(text, page);
            result.getItems().forEach(r -> list.add(mapItem(r)));
            hasNext = result.isHasMore();
            size = size + result.getItems().size();
        }
        onResult.accept(list, hasNext);
    }

    private StackExchangeResultDto search(String text, int page) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "/search")
                .queryParam("page", page)
                .queryParam("pagesize", STACK_EXCHANGE_PAGESIZE)
                .queryParam("order", "desc")
                .queryParam("sort", "activity")
                .queryParam("intitle", text)
                .queryParam("site", "stackoverflow");

        ResponseEntity<StackExchangeResultDto> response = restTemplate.getForEntity(
                builder.toUriString(), StackExchangeResultDto.class);
        return response.getBody();
    }

    private SearchResultItem mapItem(StackExchangeResultItemDto source) {
        var item = new SearchResultItem();
        item.setAuthor(source.getOwner().getDisplayName());
        item.setTitle(source.getTitle());
        item.setSourceLink(source.getLink());
        item.setDate(new Date(source.getCreationDate() * 1000));
        return item;
    }
}
