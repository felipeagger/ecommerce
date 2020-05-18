package org.felipeagger.ecommerce;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class OrderResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/orders/")
          .then()
             .statusCode(200);
    }

}