package org.fuc.core;

public interface QuerySingle<T> {
    T query(Criteria criteria);
}
