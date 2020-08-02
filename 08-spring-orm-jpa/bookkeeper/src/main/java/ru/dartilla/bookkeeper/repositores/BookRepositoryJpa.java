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
    public void delete(Long id) {
        Book book = em.find(Book.class, id);
        if (book != null) {
            em.remove(book);
        }
    }

    @Override
    public Book getById(Long id) {
        return em.find(Book.class, id);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(getById(id));
    }

    @Override
    public List<Book> getAll() {
        return em.createQuery("select b from Book b join fetch b.author order by b.author.name, b.title", Book.class)
                .getResultList();
    }

    @Override
    public List<Book> findByAuthorIdAndTitleAndStorage(Long authorId, String title, boolean isInStorage) {
        TypedQuery<Book> query = em.createQuery("select b from Book b where b.author.id = :authorId" +
                " and b.title = :title and b.inStorage = :inStorage", Book.class);
        query.setParameter("authorId", authorId);
        query.setParameter("title", title);
        query.setParameter("inStorage", isInStorage);
        return query.getResultList();
    }
}
