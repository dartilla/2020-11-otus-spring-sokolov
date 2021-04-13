package ru.dartilla.bookkeeper.mms.config.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.dartilla.bookkeeper.mms.config.AppProps;
import ru.dartilla.bookkeeper.mms.mongo.domain.Genre;

import java.util.Map;

@Configuration
public class GenreStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private AppProps appProps;

    @StepScope
    @Bean
    public MongoItemReader<Genre> genreReader(MongoOperations mongoTemplate) {
        return new MongoItemReaderBuilder<Genre>()
                .name("genreReader")
                .template(mongoTemplate)
                .targetType(Genre.class)
                .sorts(Map.of("_id", Sort.Direction.ASC))
                .jsonQuery("{}")
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Genre, ru.dartilla.bookkeeper.mms.sql.domain.Genre> genreProcessor() {
        return item -> new ru.dartilla.bookkeeper.mms.sql.domain.Genre(null, item.getName(), item.getId());
    }

    @Bean
    @SuppressWarnings({"rawtypes", "unchecked"})
    public ItemWriter<ru.dartilla.bookkeeper.mms.sql.domain.Genre> genreWriter(ItemWriter jpaWriter) {
        return jpaWriter;
    }

    @Bean
    public Step importGenreStep(ItemReader<Genre> genreReader,
                                ItemWriter<ru.dartilla.bookkeeper.mms.sql.domain.Genre> genreWriter,
                                ItemProcessor<Genre, ru.dartilla.bookkeeper.mms.sql.domain.Genre> genreProcessor) {
        return stepBuilderFactory.get("importGenreStep")
                .<Genre, ru.dartilla.bookkeeper.mms.sql.domain.Genre>chunk(appProps.getChunkSize())
                .reader(genreReader)
                .processor(genreProcessor)
                .writer(genreWriter)
                .build();
    }
}
