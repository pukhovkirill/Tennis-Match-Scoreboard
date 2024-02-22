package org.tms.repository;

import java.util.List;
import java.util.Optional;

public interface ModelRepository<T> {
    Optional<T> find(Long id);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(Long id);
}
