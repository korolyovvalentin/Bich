package org.fuc.core;

public interface Command<T> {
    void execute(T context);
}
