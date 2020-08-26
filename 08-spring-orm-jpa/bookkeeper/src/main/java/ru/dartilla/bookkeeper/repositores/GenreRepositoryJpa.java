package ru.dartilla.bookkeeper.repositores;

import org.springframework.stereotype.Repository;
import ru.dartilla.bookkeeper.domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepositoryJpa implements GenreRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Genre getById(Long id) {
        return em.find(Genre.class, id);
    }

    @Override
    public List<Genre> getAll() {
        return em.createQuery("select g from Genre g").getResultList();
    }

    @Override
    public Optional<Genre> findByName(String name) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.name = :name", Genre.class);
        query.setParameter("name", name);
        return query.getResultList().stream().findAny();
    }

    @Override
    public List<Genre> findByNames(Collection<String> names) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.name in :names", Genre.class);
        query.setParameter("names", names);
        return query.getResultList();
    }
}
