package ru.pominov.taskmanager.abstraction;

import org.springframework.data.domain.PageRequest;
import ru.pominov.taskmanager.model.enums.CommentSort;
import ru.pominov.taskmanager.model.enums.TaskSort;

public abstract class PageRequestUtil {

    protected PageRequest createPageRequest(int from, int size) {
        return PageRequest.of(from, size);
    }

    protected PageRequest createPageRequest(int from, int size, TaskSort sort) {
        return PageRequest.of(from, size, sort.getSortValue());
    }

    protected PageRequest createPageRequest(int from, int size, CommentSort sort) {
        return PageRequest.of(from, size, sort.getSortValue());
    }
}
