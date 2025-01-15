package com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.swagger;

import com.kennedy.rest_with_spring_boot_and_java_erudio.configs.TestConfigs;
import com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.*;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"spring.profiles.active=test"})
public class SwaggerIntegrationTest extends AbstractIntegrationTest {

	@Test
	void shouldDisplaySwaggerUiPage() {
		String content = given()
				.basePath("/swagger-ui/index.html")
				.port(TestConfigs.SERVER_PORT)
				.when()
					.get()
				.then()
					.statusCode(HttpStatus.OK.value())
				.extract()
					.body()
						.asString();

		assertThat(content).contains("Swagger UI");
	}

}
