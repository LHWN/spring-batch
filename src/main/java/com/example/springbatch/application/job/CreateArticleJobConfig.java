package com.example.springbatch.application.job;

import com.example.springbatch.application.model.ArticleModel;
import com.example.springbatch.domain.entity.Article;
import com.example.springbatch.domain.repository.ArticleRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class CreateArticleJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ArticleRespository articleRespository;
    private final JdbcTemplate jdbcTemplate;

    @Bean
    public Job createArticleJob() {
        return jobBuilderFactory.get("createArticleJob")
                .incrementer(new RunIdIncrementer())
                .start(createArticleStep())
                .build();
    }

    @Bean
    public Step createArticleStep() {
        return stepBuilderFactory.get("createArticleStep")
                .<ArticleModel, Article>chunk(100)
                .reader(createArticleReader())
                .processor(createArticleProcessor())
                .writer(createArticleWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<ArticleModel> createArticleReader() {
         return new FlatFileItemReaderBuilder<ArticleModel>()
                 .name("createArticleReader")
                 .resource(new ClassPathResource("Articles.csv"))
                 .delimited()
                 .names("title", "content")
                 .fieldSetMapper(new BeanWrapperFieldSetMapper<>())
                 .targetType(ArticleModel.class)
                 .build();
    }

    @Bean
    public ItemProcessor<ArticleModel, Article> createArticleProcessor() {
        LocalDateTime now = LocalDateTime.now();
        return articleModel -> Article.builder()
                .title(articleModel.getTitle())
                .content(articleModel.getContent())
                .createdAt(now)
                .build();
    }

    @Bean
    public ItemWriter<Article> createArticleWriter() {
        return items -> jdbcTemplate.batchUpdate("insert into Article (content, createdAt, title) values (?, ?, ?)",
                items,
                100,
                (ps, article) -> {
                    ps.setObject(1, article.getTitle());
                    ps.setObject(2, article.getContent());
                    ps.setObject(3, article.getCreatedAt());
                });
    }

    @Bean
    public RepositoryItemWriter<Article> createArticleRepositoryWriter() {
        return new RepositoryItemWriterBuilder<Article>()
                .repository(articleRespository)
                .build();
    }



}
