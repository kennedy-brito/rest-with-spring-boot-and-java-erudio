package com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.controller.withyml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.kennedy.rest_with_spring_boot_and_java_erudio.configs.TestConfigs;
import com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.controller.withyml.mapper.CustomRestAssuredYamlMapper;
import com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.vo.security.AccountCredentialsVO;
import com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.vo.security.TokenVO;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
		properties = {"spring.profiles.active=test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerYmlTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static RestAssuredConfig config;

	private static ObjectMapper objectMapper;

	private static TokenVO tokenVO;

	private static CustomRestAssuredYamlMapper customMapper;


    @BeforeAll
	public static void setUp(){
		objectMapper = new YAMLMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		customMapper = new CustomRestAssuredYamlMapper(objectMapper);
	}

	@Test
	@Order(1)
	public void testSignin() throws JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO(TestConfigs.PASSWORD,TestConfigs.USER);

		RestAssured.registerParser(TestConfigs.CONTENT_TYPE_YML, Parser.TEXT);

				specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ACCEPT, TestConfigs.CONTENT_TYPE_YML)
				.addHeader(TestConfigs.HEADER_PARAM_CONTENT_TYPE, TestConfigs.CONTENT_TYPE_YML)
						.setConfig(RestAssured
								.config()
								.encoderConfig(
										encoderConfig()
												.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
						.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		tokenVO = given().spec(specification)
								.accept(TestConfigs.CONTENT_TYPE_YML)
				.basePath("/auth/signin")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_YML)
					.body(user, customMapper)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(TokenVO.class, customMapper);

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

		var newTokenVO = given().spec(specification)
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
				.as(TokenVO.class, customMapper);

		assertThat(newTokenVO).isNotNull();
		assertThat(newTokenVO.getAccessToken()).isNotNull();
		assertThat(newTokenVO.getRefreshToken()).isNotNull();
		assertThat(newTokenVO.getAuthenticated()).isTrue();
		assertThat(newTokenVO.getCreated()).isBeforeOrEqualTo(new Date());
		assertThat(newTokenVO.getExpiration()).isAfter(new Date());
		assertThat(newTokenVO.getUsername()).isEqualTo(TestConfigs.USER);
	}

}
