package ru.pominov.taskmanager.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum CommentSort {

    NEWEST_FIRST(Sort.by(Sort.Direction.DESC, "created")),
    OLDEST_FIRST(Sort.by(Sort.Direction.ASC, "created"));

    private final Sort sortValue;
}
