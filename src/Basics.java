import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

public class Basics {

	public static void main(String[] args) throws IOException {
		/*TC1: Verify GET Users request
		1. Verify 200 OK message is returned
		2. Verify that there are 10 users in the results */

		RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
		String response = given().log().all()
		.when().get("/users")
		.then().log().all().statusCode(200).extract().response().asString();
		
		System.out.println(response);
		JsonPath path= new JsonPath(response);
		Assert.assertEquals(path.getList("$").size(),10);

		System.out.println(path.getList("$").size());
		
		/*TC2: Verify GET User request by Id 
		1. Verify 200 OK message is returned
		2. Verify if user with id8 is Nicholas Runolfsdottir V */
		Response res = given().log().all().queryParam("id", "8")
				.when().get("/users")
				.then().log().all().assertThat().statusCode(200).extract().response();
		JsonPath jpath = res.getBody().jsonPath();
		String actual = jpath.getString("name");
		Assert.assertTrue(actual.contains("Nicholas Runolfsdottir V"));
		
		/*TC3: Verify POST Users request 
		 * Verify 201 Created message is returned
			2. Verify that the posted data are showing up in the result
		 */
		String responsepost = given().log().all()
				.body(new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"\\src\\files\\PayLoad.json"))))
				.when().post("/users")
				.then().log().all().statusCode(201)
				.extract().response().asString();
		System.out.println("responsepost:"+responsepost);
		
		jpath = new JsonPath(responsepost);
		System.out.println("id"+jpath.get("id").toString());
		//Need to discuss tomorrow
		
		//System.out.println("name"+jpath.get("name").toString());
		
		String responseget = given().log().all().queryParam("id", 11)
				.when().get("/users")
				.then().log().all().assertThat().statusCode(200)
				.extract().response().asString();
		System.out.println("responseget:"+responseget);
		
		JsonPath path1 = new JsonPath(responseget);
		System.out.println("size:"+path1.getList("$").size());
		System.out.println("ID:"+path1.get("id").toString());
	}

}
