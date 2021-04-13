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
import ru.dartilla.bookkeeper.mms.mongo.domain.Author;

import java.util.Map;

@Configuration
public class AuthorStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private AppProps appProps;

    @StepScope
    @Bean
    public MongoItemReader<Author> authorReader(MongoOperations mongoTemplate) {
        return new MongoItemReaderBuilder<Author>()
                .name("AuthorReader")
                .template(mongoTemplate)
                .targetType(Author.class)
                .sorts(Map.of("_id", Sort.Direction.ASC))
                .jsonQuery("{}")
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Author, ru.dartilla.bookkeeper.mms.sql.domain.Author> authorProcessor() {
        return item -> new ru.dartilla.bookkeeper.mms.sql.domain.Author(null, item.getName(), item.getId());
    }

    @Bean
    @SuppressWarnings({"rawtypes", "unchecked"})
    public ItemWriter<ru.dartilla.bookkeeper.mms.sql.domain.Author> authorWriter(ItemWriter jpaWriter) {
        return jpaWriter;
    }

    @Bean
    public Step importAuthorStep(ItemReader<Author> authorReader,
                                ItemWriter<ru.dartilla.bookkeeper.mms.sql.domain.Author> authorWriter,
                                ItemProcessor<Author, ru.dartilla.bookkeeper.mms.sql.domain.Author> authorProcessor) {
        return stepBuilderFactory.get("importAuthorStep")
                .<Author, ru.dartilla.bookkeeper.mms.sql.domain.Author>chunk(appProps.getChunkSize())
                .reader(authorReader)
                .processor(authorProcessor)
                .writer(authorWriter)
                .build();
    }
}
