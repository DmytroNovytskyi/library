package com.my.library.util;

import org.springframework.data.domain.*;

import java.util.List;

public class CommonTestData {
    public static final Integer PAGE = 0;
    public static final Integer SIZE = 5;
    public static final String SORT_BY = "id";
    public static final String ORDER = "asc";
    public static final Integer INVALID_PAGE = -1;
    public static final Integer INVALID_SIZE = 0;
    public static final String INVALID_SORT_BY = "not_id";
    public static final String INVALID_ORDER = "not_asc";

    public static Pageable createPageable() {
        return PageRequest.of(PAGE, SIZE, Sort.by(SORT_BY).ascending());
    }

    public static <T> Page<T> createPage(List<T> data) {
        return new PageImpl<>(data, createPageable(), data.size());
    }
}
