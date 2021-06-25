package com.example.springbatch.application.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CreateBoardJobConfig  {
    private static final int CHUNK_SIZE = 1000;
//    private final jobBuilderFactory jobBuilderFactory;
}
