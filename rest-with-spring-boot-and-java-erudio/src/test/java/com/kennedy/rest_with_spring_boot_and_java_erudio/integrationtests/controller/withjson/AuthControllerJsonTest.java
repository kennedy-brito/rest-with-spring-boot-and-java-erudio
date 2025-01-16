package com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.controller.withjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kennedy.rest_with_spring_boot_and_java_erudio.configs.TestConfigs;
import com.kennedy.rest_with_spring_boot_and_java_erudio.data.vo.v1.security.AccountCredentialsVO;
import com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.vo.PersonVO;
import com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.vo.security.TokenVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static com.kennedy.rest_with_spring_boot_and_java_erudio.commons.PersonConstants.p1;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
		properties = {"spring.profiles.active=test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;

	private static TokenVO tokenVO;

    @BeforeAll
	public static void setUp(){
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

	}

	@Test
	@Order(1)
	public void testSignin() throws JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO(TestConfigs.PASSWORD,TestConfigs.USER);

		tokenVO = given()
				.filter(new RequestLoggingFilter(LogDetail.ALL))
				.filter(new ResponseLoggingFilter(LogDetail.ALL))
				.basePath("/auth/signin")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(user)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(TokenVO.class);

		assertThat(tokenVO).isNotNull();
		assertThat(tokenVO.getAccessToken()).isNotNull();
		assertThat(tokenVO.getRefreshToken()).isNotNull();
		assertThat(tokenVO.getAuthenticated()).isTrue();
		assertThat(tokenVO.getCreated()).isBeforeOrEqualTo(new Date());
		assertThat(tokenVO.getExpiration()).isAfter(new Date());
		assertThat(tokenVO.getUsername()).isEqualTo(TestConfigs.USER);
	}

	@Test
	@Order(2)
	public void testRefresh() throws JsonProcessingException {

		var newTokenVO = given()
				.filter(new RequestLoggingFilter(LogDetail.ALL))
				.filter(new ResponseLoggingFilter(LogDetail.ALL))
				.basePath("/auth/refresh")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.header(TestConfigs.HEADER_PARAM_AUTHORIZATION, TestConfigs.BEARER_TOKEN + tokenVO.getRefreshToken())
					.pathParam("username", TestConfigs.USER)
					.when()
				.put("{username}")
					.then()
				.statusCode(200)
				.extract()
				.body()
				.as(TokenVO.class);

		assertThat(newTokenVO).isNotNull();
		assertThat(newTokenVO.getAccessToken()).isNotNull();
		assertThat(newTokenVO.getRefreshToken()).isNotNull();
		assertThat(newTokenVO.getAuthenticated()).isTrue();
		assertThat(newTokenVO.getCreated()).isBeforeOrEqualTo(new Date());
		assertThat(newTokenVO.getExpiration()).isAfter(new Date());
		assertThat(newTokenVO.getUsername()).isEqualTo(TestConfigs.USER);
	}

}
