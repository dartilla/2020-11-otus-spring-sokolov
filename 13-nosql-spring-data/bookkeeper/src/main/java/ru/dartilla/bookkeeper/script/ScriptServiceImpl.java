package ru.dartilla.bookkeeper.script;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.repositores.ScriptRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ScriptServiceImpl implements ScriptService {

    private final ScriptRepository scriptRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Script> findByAuthorIdAndTitle(Long authorId, String title) {
        return scriptRepository.findFirstByAuthorIdAndTitle(authorId, title);
    }

    @Override
    @Transactional
    public Script save(Script script) {
        return scriptRepository.save(script);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Script> findById(Long id) {
        return scriptRepository.findById(id);
    }
}
