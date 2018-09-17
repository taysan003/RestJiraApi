package com.issues;
import static io.restassured.RestAssured.given;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

public class CreateBug {
	
	@Test
	public void verifyResponse() throws IOException {
		//Logging
		String requestBody = generateString("G:\\SeleniumProjects\\REST_WorkSpace\\RESTJiraApi\\Payloads\\JiraLogin.json");
		RestAssured.baseURI = "http://localhost:8087"; //converting date from payload to json
		Response res= given(). // we are getting overhere response and save it in response variable 
				contentType(ContentType.JSON). 
				body(requestBody).
		when().
			post("/rest/auth/1/session"). //resources
		then()
			.assertThat().statusCode(200).and().
		extract().response();
		String response = res.asString(); //converting respose to string  
		JsonPath jsonRes = new JsonPath(response); // we are parsing to json 
		String sessionId = jsonRes.getString("session.value"); //converting json to string
		System.out.println(sessionId);
		
		//Creation of issue
		String createBugBody = generateString("G:\\SeleniumProjects\\REST_WorkSpace\\RESTJiraApi\\Payloads\\CreateBug.json");
		RestAssured.baseURI = "http://localhost:8087"; //converting date from payload to json
		Response createResponse = given(). // we are getting overhere response and save it in response variable 
				contentType(ContentType.JSON). 
				header("cookie", "JSESSIONID="+sessionId+"").
				//adding cookie
				body(createBugBody).
		when().
			post("/rest/api/2/issue"). //resources
		then()
			.assertThat().statusCode(201).log().all().
		extract().response();
				 
		JsonPath createResJson = new JsonPath(createResponse.asString()); // we are parsing to json 
		String issueId = createResJson.getString("id"); //getting id
		
		
		//Update comment
		String createCmntBody = generateString("G:\\SeleniumProjects\\REST_WorkSpace\\RESTJiraApi\\Payloads\\AddCmnt.json");
		RestAssured.baseURI = "http://localhost:8087"; //converting date from payload to json
		Response addCmntResponse = given(). // we are getting overhere response and save it in response variable 
				contentType(ContentType.JSON). 
				header("cookie", "JSESSIONID="+sessionId+"").
				//adding cookie
				body(createCmntBody).
		when().
			post("/rest/api/2/issue/"+issueId+"/comment"). //resources
		then()
			.assertThat().statusCode(201).log().all().
		extract().response();
				 
		/*JsonPath createResJsonAdd = new JsonPath(addCmntResponse.asString()); // we are parsing to json 
		String issueId = createResJsonAdd.getString("id");*/ //getting id
		
}
	public static String generateString(String filePath) throws IOException {
		
		return new String (Files.readAllBytes(Paths.get(filePath)));
		
	}
}
