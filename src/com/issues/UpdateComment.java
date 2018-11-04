package com.issues;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public class UpdateComment {
	
	@Test
	public void verifyResponse() throws IOException {
		//Logging
		String requestBody = generateString("G:\\SeleniumProjects\\REST_WorkSpace\\RESTJiraApi\\Payloads\\JiraLogin.json");
		RestAssured.baseURI = "http://localhost:8090"; //converting date from payload to json
		Response res = given(). // we are getting overhere response and save it in response variable
				contentType(ContentType.JSON).
				body(requestBody).
				when().
				post("/rest/auth/1/session"). //resources
				then()
				.assertThat().statusCode(200).
						extract().response();
		String response = res.asString(); //converting respose to string
		JsonPath jsonRes = new JsonPath(response); // we are parsing to json
		String sessionId = jsonRes.getString("session.value"); //converting json to string
		System.out.println(sessionId);

		//Creation of issue
		String createBugBody = generateString("G:\\SeleniumProjects\\REST_WorkSpace\\RESTJiraApi\\Payloads\\CreateBug.json");
		RestAssured.baseURI = "http://localhost:8090"; //converting date from payload to json
		Response createResponse = given(). // we are getting overhere response and save it in response variable
				contentType(ContentType.JSON).
				header("cookie", "JSESSIONID =" + sessionId + "").
				//adding cookie
						body(createBugBody).
						when().
						post("/rest/api/2/issue/"). //resources
				then()
				.assertThat().statusCode(201).log().all().
						extract().response();

		JsonPath createResJson = new JsonPath(createResponse.asString()); // we are parsing to json
		String issueId = createResJson.getString("id"); //getting id
		System.out.println(issueId);

		//Add comment
		String createCmntBody = generateString("G:\\SeleniumProjects\\REST_WorkSpace\\RESTJiraApi\\Payloads\\AddCmnt.json");
		Response addCmntResponse = given(). // we are getting overhere response and save it in response variable
				contentType(ContentType.JSON).
				header("cookie", "JSESSIONID="+sessionId+"").
				//adding cookie
						body(createCmntBody).
						when().
						post("/rest/api/2/issue/"+issueId+"/comment"). //resources
						/*post("/rest/api/2/issue/RAT-9/comment"). //resources*/
				then()
				.assertThat().statusCode(201).log().all().
						extract().response();

		JsonPath addCmntResJson = new JsonPath(addCmntResponse.asString()); // we are parsing to json
		String cmntId = addCmntResJson.getString("id"); //getting id
		System.out.println(cmntId);


		//Update comment
		String UpdateCmntBody = generateString("G:\\SeleniumProjects\\REST_WorkSpace\\RESTJiraApi\\Payloads\\UpdateCmnt.json");
		Response updateCmntResponse = given(). // we are getting overhere response and save it in response variable
				contentType(ContentType.JSON).
				header("cookie", "JSESSIONID=" +sessionId+ "").
				//adding cookie
						body(UpdateCmntBody).
						when().
						put("/rest/api/2/issue/"+issueId+"/comment/" +cmntId+ "").
						then()
				.assertThat().statusCode(200).log().all().
						extract().response();
		/*JsonPath createResJsonAdd = new JsonPath(addCmntResponse.asString()); // we are parsing to json
		String issueId = createResJsonAdd.getString("id"); //getting id*/
		
		
		//System.out.println("Comment Added");
		//Update comment
		/*String updateCmntBody = generateString("G:\\SeleniumProjects\\REST_WorkSpace\\RESTJiraApi\\Payloads\\UpdateCmnt.json");
		RestAssured.baseURI = "http://localhost:8087"; //converting date from payload to json
		Response updateCmntResponse = given(). // we are getting overhere response and save it in response variable 
				contentType(ContentType.JSON). 
				header("cookie", "JSESSIONID="+sessionID+"").
				//adding cookie
				body(updateCmntBody).
		when().
			put("/rest/api/2/issue/RAT-1/comment/" +cmntID+ ""). //resources
		then()
			.assertThat().statusCode(200).
		extract().response();*/
				 
		/*JsonPath addCmntJsonResponse = new JsonPath(addCmntResponse.asString()); // we are parsing to json 
		String cmntId = jsonRes.getString("id");*/ //getting id
		
		
		//Deleting comment
		/*given(). // we are getting overhere response and save it in response variable
		contentType(ContentType.JSON). 
		header("cookie", "JSESSIONID="+sessionID+"").
		
		when().
		delete("/rest/api/2/issue/RAT-1/comment/" +cmntID+ ""). //resources
	then()
		.assertThat().statusCode(204).log().all(); //status cod we are getting from API doc*/
}
	
	
	
	public static String generateString(String filePath) throws IOException {
		
		return new String (Files.readAllBytes(Paths.get(filePath)));
		
	}
}
