package org.fuc.core;

import java.util.Collection;

public interface Query<T> {
    Collection<T> query(Criteria criteria);
}
