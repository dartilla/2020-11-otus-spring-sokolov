package ru.dartilla.bookkeeper.script;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.repositores.ScriptRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ScriptServiceImpl implements ScriptService {

    private final ScriptRepository scriptRepository;

    @Override
    public Optional<Script> findByAuthorIdAndTitle(String authorId, String title) {
        return scriptRepository.findByAuthorIdAndTitle(authorId, title);
    }

    @Override
    public Script save(Script script) {
        return scriptRepository.save(script);
    }

    @Override
    public Optional<Script> findById(String id) {
        return scriptRepository.findById(id);
    }
}
