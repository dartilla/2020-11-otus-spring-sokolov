package ru.dartilla.bookkeeper.mms.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImportMongoToSqlJobConfig {

    public static final String IMPORT_MONGO_TO_SQL_JOB = "importMongoToSqlJob";

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job importMongoToSqlJob(Step importGenreStep,
                                   Step importAuthorStep,
                                   Step importScriptStep,
                                   Step importBookStep,
                                   Step importCommentStep,
                                   Step importCommentParentStep) {
        return jobBuilderFactory.get(IMPORT_MONGO_TO_SQL_JOB)
                .incrementer(new RunIdIncrementer())
                .flow(importGenreStep)
                .next(importAuthorStep)
                .next(importScriptStep)
                .next(importBookStep)
                .next(importCommentStep)
                .next(importCommentParentStep)
                .end()
                .build();
    }

}
