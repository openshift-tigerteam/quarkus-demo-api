package com.redhat.demo.application;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class ApplicationResourceTest {

    @Test
    public void getAll() {
        given()
                .when()
                .get("/api/v1/applications")
                .then()
                .statusCode(200);
    }

    @Test
    public void postAndGetById() {
        Application application = createApplication();
        Application saved = given()
                .contentType(ContentType.JSON)
                .body(application)
                .post("/api/v1/applications")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract().as(Application.class);
        assertNotNull(saved.applicationId());
        Application got = given()
                .when()
                .get("/api/v1/applications/{applicationId}", saved.applicationId())
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(Application.class);
        assertEquals(saved, got);
    }

    @Test
    public void getByIdNotFound() {
        given()
                .when()
                .get("/api/v1/applications/{applicationId}", 987654321)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void postFailNoName() {
        Application application = new Application(null, null);
        given()
                .contentType(ContentType.JSON)
                .body(application)
                .post("/api/v1/applications")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void postAndPutAndGetById() {
        Application application = createApplication();
        Application saved = given()
                .contentType(ContentType.JSON)
                .body(application)
                .post("/api/v1/applications")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract().as(Application.class);
        Application updated = new Application(saved.applicationId(), RandomStringUtils.randomAlphabetic(10));
        given()
                .contentType(ContentType.JSON)
                .body(updated)
                .put("/api/v1/applications/{applicationId}", updated.applicationId())
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
        Application got = given()
                .when()
                .get("/api/v1/applications/{applicationId}", saved.applicationId())
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(Application.class);
        assertEquals(updated, got);
    }

    private Application createApplication() {
        return new Application(null, RandomStringUtils.randomAlphabetic(10));
    }

}