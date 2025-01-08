package ua.yatsergray.football.manager.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CrudRepository<T> {

    T save(T entity);

    Optional<T> findById(UUID entityId);

    List<T> findAll();

    void deleteById(UUID entityId);
}
