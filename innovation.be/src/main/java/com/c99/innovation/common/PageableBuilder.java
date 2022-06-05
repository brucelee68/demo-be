package com.c99.innovation.common;

import com.c99.innovation.entity.Innovation;
import com.c99.innovation.exception.NotValidSortingFieldName;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static com.c99.innovation.common.ReflectionUtils.checkValidField;

public class PageableBuilder {

    private PageableBuilder() {
    }

    public static Pageable buildPage(Optional<String> order, Optional<String> sortField, Optional<Integer> pageSize, Optional<Integer> pageIndex) {
        String actualSortField = sortField.orElse("id");
        if (!checkValidField(actualSortField, Innovation.class)) {
            throw new NotValidSortingFieldName();
        }
        return PageRequest.of(
                pageIndex.orElse(0),
                pageSize.orElse(9),
                Sort.by(order.orElse("asc").equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, actualSortField
                ));
    }
}
