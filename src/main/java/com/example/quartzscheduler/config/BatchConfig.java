package com.example.quartzscheduler.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.quartzscheduler.tasks.TaskOne;
import com.example.quartzscheduler.tasks.TaskTwo;

@Configuration
public class BatchConfig {


	@Bean
	Step stepOne(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("stepOne", jobRepository)
				.tasklet(new TaskOne(), transactionManager).build();
	}

	@Bean
	Step stepTwo(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("stepTwo", jobRepository)
				.tasklet(new TaskTwo(), transactionManager).build();
	}

	@Bean(name = "demoJobOne")
	Job demoJobOne(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new JobBuilder("demoJobOne", jobRepository)
				.start(stepOne(jobRepository, transactionManager))
				.next(stepTwo(jobRepository, transactionManager)).build();
	}

	@Bean(name = "demoJobTwo")
	Job demoJobTwo(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new JobBuilder("demoJobTwo", jobRepository)
				.flow(stepOne(jobRepository, transactionManager)).build().build();
	}
}
