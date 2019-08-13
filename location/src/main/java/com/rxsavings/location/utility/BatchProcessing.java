/**
 * 
 */
package com.rxsavings.location.utility;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import com.rxsavings.location.model.Pharmacy;

/**
 * @author haree
 *
 */
@Configuration
@EnableBatchProcessing
public class BatchProcessing {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public DataSource dataSource;

	// @Bean
	// public DataSource dataSource() {
	// final DriverManagerDataSource dataSource = new DriverManagerDataSource();
	// dataSource.setDriverClassName("com.mysql.jdbc.Driver");
	// dataSource.setUrl("jdbc:mysql://localhost/location");
	// dataSource.setUsername("root");
	// dataSource.setPassword("root");
	//
	// return dataSource;
	// }

	@Bean
	@Autowired
	public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
		ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
		resourceDatabasePopulator.addScript(new ClassPathResource(
				"org/springframework/batch/core/schema-drop-mysql.sql"));
		resourceDatabasePopulator.addScript(new ClassPathResource(
				"org/springframework/batch/core/schema-mysql.sql"));

		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();

		dataSourceInitializer.setDataSource(dataSource);
		DatabasePopulatorUtils.execute(resourceDatabasePopulator, dataSource);
		dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);

		return dataSourceInitializer;
	}

	@Bean
	public FlatFileItemReader<Pharmacy> reader() {
		FlatFileItemReader<Pharmacy> reader = new FlatFileItemReader<Pharmacy>();
		reader.setResource(new ClassPathResource("pharmacies.csv"));
		reader.setLineMapper(pharmacyLineMapper());
		reader.setLinesToSkip(1);
		// reader.setLineMapper(new DefaultLineMapper<Pharmacy>() {
		// {
		// setLineTokenizer(new DelimitedLineTokenizer() {
		// {
		// setNames("name", "address", "city", "state", "zip",
		// "latitude", "longitude");
		// setIncludedFields(new int[]{0, 1, 2, 3, 4, 5, 6});
		// }
		// });
		// setFieldSetMapper(new BeanWrapperFieldSetMapper<Pharmacy>() {
		// {
		// setTargetType(Pharmacy.class);
		// }
		// });
		//
		// }
		// });

		return reader;
	}

	@Bean
	public LineMapper<Pharmacy> pharmacyLineMapper() {
		DefaultLineMapper<Pharmacy> lineMapper = new DefaultLineMapper<>();

		// Delimited Line Tokenizer
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setNames("name", "address", "city", "state", "zip",
				"latitude", "longitude");
		lineTokenizer.setIncludedFields(new int[]{0, 1, 2, 3, 4, 5, 6});

		// Bean Wrapper Field SetMapper
		BeanWrapperFieldSetMapper<Pharmacy> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Pharmacy.class);

		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		return lineMapper;
	}

	@Bean
	public PharmacyObjectProcessor processor() {
		return new PharmacyObjectProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Pharmacy> writer() {
		JdbcBatchItemWriter<Pharmacy> writer = new JdbcBatchItemWriter<Pharmacy>();
		writer.setItemSqlParameterSourceProvider(
				new BeanPropertyItemSqlParameterSourceProvider<Pharmacy>());
		writer.setSql("delete from pharmacy");
		writer.setSql(
				"INSERT INTO pharmacy(name,address,city,state,zip,latitude,longitude) VALUES (:name,:address,:city,:state,:zip,:latitude,:longitude)");
		writer.setDataSource(dataSource);

		return writer;
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<Pharmacy, Pharmacy>chunk(3)
				.reader(reader()).processor(processor()).writer(writer())
				.build();
	}

	@Bean
	public Job importUserJob() {
		return jobBuilderFactory.get("importUserJob")
				.incrementer(new RunIdIncrementer()).flow(step1()).end()
				.build();
	}

}
