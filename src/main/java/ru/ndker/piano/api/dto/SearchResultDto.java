package ru.ndker.piano.api.dto;

import java.util.List;

// Setters and getters are used to mappers.
@SuppressWarnings("unused")
public class SearchResultDto {

    private final List<SearchResultItemDto> data;
    private final MetaDto meta;

    public SearchResultDto(List<SearchResultItemDto> data, int currentPage,
                           int totalPages, boolean hasNext) {
        this.data = data;
        this.meta = new MetaDto(currentPage, totalPages, hasNext);
    }

    public List<SearchResultItemDto> getData() {
        return data;
    }

    public MetaDto getMeta() {
        return meta;
    }


    public static class MetaDto {
        private final int currentPage;
        private final boolean hashNext;
        private final int totalPages;

        public MetaDto(int currentPage, int totalPages, boolean hashNext) {
            this.currentPage = currentPage;
            this.totalPages = totalPages;
            this.hashNext = hashNext;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public boolean isHashNext() {
            return hashNext;
        }

        public int getTotalPages() {
            return totalPages;
        }
    }
}
