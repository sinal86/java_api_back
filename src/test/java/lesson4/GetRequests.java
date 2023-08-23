package lesson4;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;


public class GetRequests extends AbstractTest
{

    @Test
    void websiteResponse()
    {
        RestAssured.given().spec(getRequestSpecification())
                        .when()
                        .get(getBaseUrl() + "recipes/complexSearch")
                        .then()
                        .spec(getResponseSpecification());
    }

    @Test
    void responseValues()
    {
        GetResponse responseInfo =
            RestAssured.given().spec(getRequestSpecification())
                .when()
                .get(getBaseUrl() + "recipes/complexSearch").prettyPeek()
                .then()
                .spec(getResponseSpecification())
                .extract()
                .body()
                .as(GetResponse.class);

        MatcherAssert.assertThat(responseInfo.getOffset(), Matchers.equalTo(0));
        MatcherAssert.assertThat(responseInfo.getNumber(), Matchers.equalTo(10));
        MatcherAssert.assertThat(responseInfo.getTotalResults(), Matchers.equalTo(5220));
    }

    /*  так лучще  */
    @Test
    void testOne()
    {
        GetResponse responseInfo =
                RestAssured.given().spec(getRequestSpecification())
                        .when()
                        .get(getBaseUrl() + "recipes/complexSearch")
                        .then()
                        .spec(getResponseSpecification())
                        .extract()
                        .body()
                        .as(GetResponse.class);

        List<Integer> listId = new ArrayList<>();

        for (Result result : responseInfo.getResults())
        {
            listId.add(result.getId());
        }

        JsonPath responseJSON =
                RestAssured.given().spec(getRequestSpecification())
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .body()
                .jsonPath();

        for(int i = 0; i < listId.size(); i++)
        {
            MatcherAssert.assertThat(listId.get(i),
                    Matchers.equalTo(responseJSON.getList("results.id").get(i)));
        }
    }

    @Test
    void testTwo()
    {
        RestAssured.given().spec(getRequestSpecification())
                .expect()
                .body("results.id", Matchers.contains(782585, 716426, 715497, 715415, 716406, 644387, 715446, 782601, 795751, 766453))
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .then()
                .spec(getResponseSpecification());
    }
}
