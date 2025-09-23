package com.project.hiptour.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
//@AllArgsConstructor
public class PageResponseDto<T> {
    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;
    private final boolean first;
    private final boolean last;

    private PageResponseDto(List<T> content, Page<?> page) {
        this.content = content;
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.first = page.isFirst();
        this.last = page.isLast();
    }

    public static <E, T> PageResponseDto<T> fromPage(Page<E> page, Function<E, T> converter) {
        List<T> content = page.getContent().stream()
                .map(converter)
                .collect(Collectors.toList());
        return new PageResponseDto<>(content, page);
    }
}