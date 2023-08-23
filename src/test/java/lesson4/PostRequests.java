package lesson4;

import io.restassured.RestAssured;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

public class PostRequests extends AbstractTest
{
    @Test
    void test1()
    {
        RestAssured.given().spec(getRequestSpecification())
                .when()
                .post(getBaseUrl()+"recipes/cuisine")
                .then()
                .spec(getResponseSpecification());
    }

    @Test
    void test3()
    {
        String request = RestAssured.given()
                .spec(getRequestSpecification())
                .when()
                .post(getBaseUrl()+"recipes/cuisine")
                .jsonPath().toString();

        MatcherAssert.assertThat(request, Matchers.notNullValue());
}

    @Test
    void test4()
    {
        RestAssured.given().spec(getRequestSpecification())
                .expect()
                .body("cuisine", Matchers.equalTo("Italian"))
                .when()
                .post(getBaseUrl() + "recipes/cuisine")
                .then()
                .spec(getResponseSpecification());
    }

    @Test
    void test5()
    {
        RestAssured.given().spec(getRequestSpecification())
                .expect()
                .body("confidence", Matchers.equalTo(0.0F))
                .when()
                .post(getBaseUrl() + "recipes/cuisine")
                .then()
                .spec(getResponseSpecification());
    }

    @Test
    void addMealTest()
    {
        String responseID =
                RestAssured.given().spec(getRequestSpecification())
                        .queryParam("hash", getHash())
                        .pathParams("user_name", getUserName())
                        .body("{\n"
                                + " \"item\": \"5 package sugar\",\n"
                                + " \"aisle\": \"Baking\",\n"
                                + " \"parse\": true\n"
                                + "}"
                        )
                        .log().all()
                        .when()
                        .post(getBaseUrl() + "mealplanner/{user_name}/shopping-list/items")
                        .prettyPeek()
                        .jsonPath()
                        .get("id")
                        .toString();

        RestAssured.given().spec(getRequestSpecification())
                .pathParams("user_name", getUserName())
                .queryParam("hash", getHash())
                .pathParams("id", responseID)
                .log().all()
                .when()
                .delete(getBaseUrl() + "mealplanner/{user_name}/shopping-list/items/{id}")
                .prettyPeek()
                .then()
                .spec(getResponseSpecification());


    }

    @Test
    void checkDeleteElem()
    {
        RestAssured.given().spec(getRequestSpecification())
                .queryParam("hash", getHash())
                .pathParams("user_name", getUserName())
                .expect()
                .body("cost", Matchers.equalTo(0.0F))
                .log().all()
                .when()
                .get(getBaseUrl() + "mealplanner/{user_name}/shopping-list")
                .prettyPeek()
                .then()
                .spec(getResponseSpecification());
    }
}
