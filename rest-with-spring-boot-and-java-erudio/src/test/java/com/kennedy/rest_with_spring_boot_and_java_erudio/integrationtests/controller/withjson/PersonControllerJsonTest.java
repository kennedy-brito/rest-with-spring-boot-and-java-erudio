package com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.controller.withjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kennedy.rest_with_spring_boot_and_java_erudio.configs.TestConfigs;
import com.kennedy.rest_with_spring_boot_and_java_erudio.data.vo.v1.security.AccountCredentialsVO;
import com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.vo.PersonVO;
import com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.vo.security.TokenVO;
import com.kennedy.rest_with_spring_boot_and_java_erudio.integrationtests.vo.wrappers.WrapperPersonVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.apache.http.client.methods.HttpHead;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

import static com.kennedy.rest_with_spring_boot_and_java_erudio.commons.PersonConstants.*;
import static org.assertj.core.api.Assertions.*;
import static io.restassured.RestAssured.given;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
		properties = {"spring.profiles.active=test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;

	private static PersonVO person;

    @BeforeAll
	public static void setUp(){
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		person = new PersonVO();
	}

	@Test
	@Order(1)
	public void authorization() throws JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO(TestConfigs.PASSWORD,TestConfigs.USER);

        String accessToken = given()
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
                .as(TokenVO.class)
                .getAccessToken();

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, TestConfigs.BEARER_TOKEN + accessToken)
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
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.body(person)
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
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN,TestConfigs.ORIGIN_SEMERU)
				.body(person)
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
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
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
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
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

	@Test
	@Order(6)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.accept(TestConfigs.CONTENT_TYPE_JSON)
				.queryParams("page", 3, "size", 10, "direction", "asc")
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		WrapperPersonVO wrapper = objectMapper.readValue(content, WrapperPersonVO.class);
		var people = wrapper.getEmbedded().getPersons();

		PersonVO foundPersonOne = people.get(0);

		assertThat(foundPersonOne.getId()).isNotNull();
		assertThat(foundPersonOne.getFirstName()).isNotNull();
		assertThat(foundPersonOne.getLastName()).isNotNull();
		assertThat(foundPersonOne.getAddress()).isNotNull();
		assertThat(foundPersonOne.getGender()).isNotNull();

		assertThat(foundPersonOne.getEnabled()).isNotNull();

		assertThat(foundPersonOne.getId()).isEqualTo(677);

		assertThat(foundPersonOne.getFirstName()).isEqualTo("Alic");
		assertThat(foundPersonOne.getLastName()).isEqualTo("Terbrug");
		assertThat(foundPersonOne.getAddress()).isEqualTo("3 Eagle Crest Court");
		assertThat(foundPersonOne.getGender()).isEqualTo("Male");

		PersonVO foundPersonSix = people.get(5);

		assertThat(foundPersonSix.getId()).isNotNull();
		assertThat(foundPersonSix.getFirstName()).isNotNull();
		assertThat(foundPersonSix.getLastName()).isNotNull();
		assertThat(foundPersonSix.getAddress()).isNotNull();
		assertThat(foundPersonSix.getGender()).isNotNull();

		assertThat(foundPersonSix.getEnabled()).isTrue();

		assertThat(foundPersonSix.getId()).isEqualTo(911);

		assertThat(foundPersonSix.getFirstName()).isEqualTo("Allegra");
		assertThat(foundPersonSix.getLastName()).isEqualTo("Dome");
		assertThat(foundPersonSix.getAddress()).isEqualTo("57 Roxbury Pass");
		assertThat(foundPersonSix.getGender()).isEqualTo("Female");
	}


	private void mockPerson(PersonVO person) {
		person.setFirstName(p1.getFirstName());
		person.setLastName(p1.getLastName());
		person.setAddress(p1.getAddress());
		person.setGender(p1.getGender());
		person.setEnabled(p1.getEnabled());
	}
}
