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
import ru.dartilla.bookkeeper.mms.mongo.domain.Comment;
import ru.dartilla.bookkeeper.mms.sql.repository.CommentRepository;
import ru.dartilla.bookkeeper.mms.sql.repository.ScriptRepository;

import java.util.Map;

@Configuration
public class CommentStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private AppProps appProps;

    @StepScope
    @Bean
    public MongoItemReader<Comment> commentReader(MongoOperations mongoTemplate) {
        return new MongoItemReaderBuilder<Comment>()
                .name("CommentReader")
                .template(mongoTemplate)
                .targetType(Comment.class)
                .sorts(Map.of("_id", Sort.Direction.ASC))
                .jsonQuery("{}")
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Comment, ru.dartilla.bookkeeper.mms.sql.domain.Comment> commentProcessor(
            ScriptRepository scriptRepository) {
        return item -> new ru.dartilla.bookkeeper.mms.sql.domain.Comment(null,
                scriptRepository.getByMongoId(item.getScript().getId()),
                null,
                item.getMessage(),
                item.getId());
    }

    @Bean
    @SuppressWarnings({"rawtypes", "unchecked"})
    public ItemWriter<ru.dartilla.bookkeeper.mms.sql.domain.Comment> commentWriter(ItemWriter jpaWriter) {
        return jpaWriter;
    }

    @Bean
    public Step importCommentStep(ItemReader<Comment> commentReader,
                                  ItemWriter<ru.dartilla.bookkeeper.mms.sql.domain.Comment> commentWriter,
                                  ItemProcessor<Comment, ru.dartilla.bookkeeper.mms.sql.domain.Comment> commentProcessor) {
        return stepBuilderFactory.get("importCommentStep")
                .<Comment, ru.dartilla.bookkeeper.mms.sql.domain.Comment>chunk(appProps.getChunkSize())
                .reader(commentReader)
                .processor(commentProcessor)
                .writer(commentWriter)
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Comment, ru.dartilla.bookkeeper.mms.sql.domain.Comment> commentParentProcessor(
            CommentRepository commentRepository) {
        return item -> {
            ru.dartilla.bookkeeper.mms.sql.domain.Comment comment = commentRepository.getByMongoId(item.getId());
            if (item.getParent() != null) {
                comment.setParent(commentRepository.getByMongoId(item.getId()));
            }
            return comment;
        };
    }

    @Bean
    public Step importCommentParentStep(ItemReader<Comment> commentReader,
                                ItemWriter<ru.dartilla.bookkeeper.mms.sql.domain.Comment> commentWriter,
                                ItemProcessor<Comment, ru.dartilla.bookkeeper.mms.sql.domain.Comment> commentParentProcessor) {
        return stepBuilderFactory.get("importCommentParentStep")
                .<Comment, ru.dartilla.bookkeeper.mms.sql.domain.Comment>chunk(appProps.getChunkSize())
                .reader(commentReader)
                .processor(commentParentProcessor)
                .writer(commentWriter)
                .build();
    }
}
