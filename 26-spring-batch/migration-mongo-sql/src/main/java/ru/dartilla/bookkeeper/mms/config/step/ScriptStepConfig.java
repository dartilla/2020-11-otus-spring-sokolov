package ru.dartilla.bookkeeper.mms.config.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.dartilla.bookkeeper.mms.config.AppProps;
import ru.dartilla.bookkeeper.mms.mongo.domain.Genre;
import ru.dartilla.bookkeeper.mms.mongo.domain.Script;
import ru.dartilla.bookkeeper.mms.sql.repository.AuthorRepository;
import ru.dartilla.bookkeeper.mms.sql.repository.GenreRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class ScriptStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private AppProps appProps;

    @StepScope
    @Bean
    public MongoItemReader<Script> scriptReader(MongoOperations mongoTemplate) {
        return new MongoItemReaderBuilder<Script>()
                .name("ScriptReader")
                .template(mongoTemplate)
                .targetType(Script.class)
                .sorts(Map.of("_id", Sort.Direction.ASC))
                .jsonQuery("{}")
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Script, ru.dartilla.bookkeeper.mms.sql.domain.Script> scriptProcessor(
            AuthorRepository authorRepository,
            GenreRepository genreRepository) {
        return item -> {
            List<String> genreMongoIds = item.getGenres().stream().map(Genre::getId).collect(Collectors.toList());

            return new ru.dartilla.bookkeeper.mms.sql.domain.Script(null, item.getTitle(),
                    authorRepository.getByMongoId(item.getAuthor().getId()),
                    genreMongoIds.isEmpty() ? null : new HashSet<>(genreRepository.findByMongoIdIn(genreMongoIds)),
                    null,
                    item.getId());
        };
    }

    @Bean
    @SuppressWarnings({"rawtypes", "unchecked"})
    public ItemWriter<ru.dartilla.bookkeeper.mms.sql.domain.Script> scriptWriter(ItemWriter jpaWriter) {
        return jpaWriter;
    }

    @Bean
    public Step importScriptStep(ItemReader<Script> scriptReader,
                                ItemWriter<ru.dartilla.bookkeeper.mms.sql.domain.Script> scriptWriter,
                                ItemProcessor<Script, ru.dartilla.bookkeeper.mms.sql.domain.Script> scriptProcessor) {
        return stepBuilderFactory.get("importScriptStep")
                .<Script, ru.dartilla.bookkeeper.mms.sql.domain.Script>chunk(appProps.getChunkSize())
                .reader(scriptReader)
                .processor(scriptProcessor)
                .writer(scriptWriter)
                .build();
    }
}
