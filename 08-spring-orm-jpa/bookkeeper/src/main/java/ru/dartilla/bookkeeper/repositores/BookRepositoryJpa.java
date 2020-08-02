package ru.dartilla.bookkeeper.repositores;

import org.springframework.stereotype.Repository;
import ru.dartilla.bookkeeper.domain.Book;
import ru.dartilla.bookkeeper.repositores.exception.MergeEntityNotExistingId;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            em.persist(book);
            return book;
        } else if (em.find(Book.class, book.getId()) != null) {
            return em.merge(book);
        } else {
            throw new MergeEntityNotExistingId();
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public List<Book> getAll() {
        return em.createQuery("select b from Book b join fetch b.script m join fetch m.author", Book.class).getResultList();
    }

    @Override
    public Optional<Book> findInStorageByScript(Long scriptId) {
        TypedQuery<Book> query= em.createQuery("select b from Book b " +
                "where b.inStorage = :inStorage and b.script.id = :scriptId", Book.class);
        query.setParameter("inStorage", true);
        query.setParameter("scriptId", scriptId);
        return query.getResultList().stream().findAny();
    }
}
