package com.example.springbatch.application.job.param;

import lombok.Getter;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Component;

@Component
@JobScope
@Getter
public class CreateArticleJobParam {
    private String name;
}
