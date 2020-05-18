package org.felipeagger.ecommerce;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class CatalogResourceTest {

    @Test
    public void testCatalogEndpoint() {
        given()
          .when().get("/")
          .then()
             .statusCode(200);
    }

}