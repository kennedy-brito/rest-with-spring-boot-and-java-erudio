package com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.controller.withyml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.kennedy.rest_with_spring_boot_and_java_erudio.configs.TestConfigs;
import com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.controller.withyml.mapper.CustomRestAssuredYamlMapper;
import com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.vo.PersonVO;
import com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.vo.security.AccountCredentialsVO;
import com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.vo.security.TokenVO;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static com.kennedy.rest_with_spring_boot_and_java_erudio.commons.PersonConstants.p1;
import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
		properties = {"spring.profiles.active=test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerYmlTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private static CustomRestAssuredYamlMapper customMapper;

	private static PersonVO person;


    @BeforeAll
	public static void setUp(){
		objectMapper = new YAMLMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		person = new PersonVO();

		customMapper = new CustomRestAssuredYamlMapper(objectMapper);
	}

	@Test
	@Order(1)
	public void authorization() throws JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO(TestConfigs.PASSWORD,TestConfigs.USER);
		RestAssuredConfig config =
				RestAssured.config()
						.encoderConfig(encoderConfig()
								.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
						);
		assert(config != null);
        String accessToken = given()
				.config(config)
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
                .as(TokenVO.class, customMapper)
                .getAccessToken();

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, TestConfigs.BEARER_TOKEN + accessToken)
				.addHeader(TestConfigs.HEADER_PARAM_ACCEPT, TestConfigs.CONTENT_TYPE_YML)
				.addHeader(TestConfigs.HEADER_PARAM_CONTENT_TYPE, TestConfigs.CONTENT_TYPE_YML)
				.setConfig(config)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(2)
	public void testCreate() throws JsonProcessingException {
		mockPerson(person);

		var content = given()
				.spec(specification)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.contentType(TestConfigs.CONTENT_TYPE_YML)
					.body(person, customMapper)
					.when()
					.post()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();

		PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);

		person = createdPerson;

		assertThat(createdPerson).isNotNull();
		assertThat(createdPerson.getId()).isNotNull();
		assertThat(createdPerson.getFirstName()).isNotNull();
		assertThat(createdPerson.getLastName()).isNotNull();
		assertThat(createdPerson.getGender()).isNotNull();
		assertThat(createdPerson.getAddress()).isNotNull();

		assertThat(createdPerson.getId()).isGreaterThan(0L);

		assertThat(createdPerson.getFirstName()).isEqualTo(p1.getFirstName());
		assertThat(createdPerson.getLastName()).isEqualTo(p1.getLastName());
		assertThat(createdPerson.getGender()).isEqualTo(p1.getGender());
		assertThat(createdPerson.getAddress()).isEqualTo(p1.getAddress());
	}

	@Test
	@Order(3)
	public void testCreateWithWrongOrigin() throws JsonProcessingException {
		mockPerson(person);

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.header(TestConfigs.HEADER_PARAM_ORIGIN,TestConfigs.ORIGIN_SEMERU)
				.body(person, customMapper)
				.when()
					.post()
				.then()
					.statusCode(403)
				.extract()
					.body()
						.asString();


		assertThat(content).isNotNull();
		assertThat(content).isEqualTo(TestConfigs.INVALID_CORS_MESSAGE);
	}

	@Test
	@Order(4)
	public void testFindById() throws JsonProcessingException {
		mockPerson(person);

		var content = given().spec(specification)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.contentType(TestConfigs.CONTENT_TYPE_YML)
					.pathParam("id", person.getId())
					.when()
					.get("{id}")
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();

		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);

		person = persistedPerson;

		assertThat(persistedPerson).isNotNull();
		assertThat(persistedPerson.getId()).isNotNull();
		assertThat(persistedPerson.getFirstName()).isNotNull();
		assertThat(persistedPerson.getLastName()).isNotNull();
		assertThat(persistedPerson.getGender()).isNotNull();
		assertThat(persistedPerson.getAddress()).isNotNull();

		assertThat(persistedPerson.getId()).isGreaterThan(0L);

		assertThat(persistedPerson.getFirstName()).isEqualTo(p1.getFirstName());
		assertThat(persistedPerson.getLastName()).isEqualTo(p1.getLastName());
		assertThat(persistedPerson.getGender()).isEqualTo(p1.getGender());
		assertThat(persistedPerson.getAddress()).isEqualTo(p1.getAddress());
	}

	@Test
	@Order(5)
	public void testFindByIdWithWrongOrigin() throws JsonProcessingException {
		mockPerson(person);

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
				.pathParam("id", person.getId())
				.when()
				.get("{id}")
				.then()
				.statusCode(403)
				.extract()
				.body()
				.asString();


		assertThat(content).isNotNull();
		assertThat(content).isEqualTo(TestConfigs.INVALID_CORS_MESSAGE);
	}

	private void mockPerson(PersonVO person) {
		person.setFirstName(p1.getFirstName());
		person.setLastName(p1.getLastName());
		person.setAddress(p1.getAddress());
		person.setGender(p1.getGender());
		person.setEnabled(p1.getEnabled());

	}
}
