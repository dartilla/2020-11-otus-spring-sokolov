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
import ru.dartilla.bookkeeper.mms.mongo.domain.Book;
import ru.dartilla.bookkeeper.mms.sql.repository.ScriptRepository;

import java.util.Map;

@Configuration
public class BookStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private AppProps appProps;

    @StepScope
    @Bean
    public MongoItemReader<Book> bookReader(MongoOperations mongoTemplate) {
        return new MongoItemReaderBuilder<Book>()
                .name("BookReader")
                .template(mongoTemplate)
                .targetType(Book.class)
                .sorts(Map.of("_id", Sort.Direction.ASC))
                .jsonQuery("{}")
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Book, ru.dartilla.bookkeeper.mms.sql.domain.Book> bookProcessor(
            ScriptRepository scriptRepository) {
        return item -> {
            return new ru.dartilla.bookkeeper.mms.sql.domain.Book(null, item.isInStorage(),
                    scriptRepository.getByMongoId(item.getScript().getId()),
                    item.getId());
        };
    }

    @Bean
    @SuppressWarnings({"rawtypes", "unchecked"})
    public ItemWriter<ru.dartilla.bookkeeper.mms.sql.domain.Book> bookWriter(ItemWriter jpaWriter) {
        return jpaWriter;
    }

    @Bean
    public Step importBookStep(ItemReader<Book> bookReader,
                                ItemWriter<ru.dartilla.bookkeeper.mms.sql.domain.Book> bookWriter,
                                ItemProcessor<Book, ru.dartilla.bookkeeper.mms.sql.domain.Book> bookProcessor) {
        return stepBuilderFactory.get("importBookStep")
                .<Book, ru.dartilla.bookkeeper.mms.sql.domain.Book>chunk(appProps.getChunkSize())
                .reader(bookReader)
                .processor(bookProcessor)
                .writer(bookWriter)
                .build();
    }
}
