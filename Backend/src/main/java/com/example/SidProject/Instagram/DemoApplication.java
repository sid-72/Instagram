package com.example.SidProject.Instagram;

import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

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
		MinioClient minioClient = configurableApplicationContext.getBean(MinioClient.class);
		try {
			minioClient.makeBucket(
					MakeBucketArgs.builder()
							.bucket("testsid1")
							.build()
			);
			System.out.println("Bucket created: testsid1");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}


	}

}
