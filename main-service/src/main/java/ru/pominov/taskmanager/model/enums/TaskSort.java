package ru.pominov.taskmanager.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum TaskSort {

    DESC_ID(Sort.by(Sort.Direction.DESC, "id")),
    ASC_ID(Sort.by(Sort.Direction.ASC, "id"));

    private final Sort sortValue;
}
