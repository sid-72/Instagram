package com.example.SidProject.Instagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(DemoApplication.class, args);
		/*HttpClient client = HttpClient.newHttpClient();

		// Create a request (GET example)
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.example.com/data"))
				.GET() //
				.build();
		client.send()*/
		S3Client s3Client= configurableApplicationContext.getBean(S3Client.class);
		CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
				.bucket("testsid1")
				.build();

		CreateBucketResponse createBucketResponse = s3Client.createBucket(createBucketRequest);
		System.out.println(createBucketResponse);


	}

}
