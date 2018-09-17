package com.issues;
import static io.restassured.RestAssured.given;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UpdateComment {
	
	@Test
	public void verifyResponse() throws IOException {
		String requestBody = generateString("G:\\SeleniumProjects\\REST_WorkSpace\\RESTJiraApi\\Payloads\\JiraLogin.json");
		
		// Login JIRA
		
		RestAssured.baseURI = "http://localhost:8087";
		Response  res = given().
				contentType(ContentType.JSON).
			body(requestBody).
			when().
				post("/rest/auth/1/session").
		then().assertThat().statusCode(200).
		extract().response();
				String respose = res.asString();
		
		JsonPath jsonRes = new JsonPath(respose);
		String sessionID = jsonRes.getString("session.value");
	
		
		
		// Add Comment
		String createCmntBody = generateString("G:\\SeleniumProjects\\REST_WorkSpace\\RESTJiraApi\\Payloads\\AddCmnt.json");
		Response  addCmntResponse = given().
				contentType(ContentType.JSON).
				header("cookie", "JSESSIONID=" + sessionID+"").
			body(createCmntBody).
			when().
				//post("/rest/api/2/issue/"+issueID+"/comment").
				post("/rest/api/2/issue/RAT-1/comment"). //resources
		 then().assertThat().statusCode(201).log().all().
		
		 extract().response();

		JsonPath addCmntResJson = new JsonPath(addCmntResponse.asString());
		String cmntID = addCmntResJson.getString("id");
		
		
		System.out.println("Comment Added");
		//Update comment
		String updateCmntBody = generateString("G:\\SeleniumProjects\\REST_WorkSpace\\RESTJiraApi\\Payloads\\UpdateCmnt.json");
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
		extract().response();
				 
		/*JsonPath addCmntJsonResponse = new JsonPath(addCmntResponse.asString()); // we are parsing to json 
		String cmntId = jsonRes.getString("id");*/ //getting id
		
		
		//Deleting comment
		given(). // we are getting overhere response and save it in response variable 
		contentType(ContentType.JSON). 
		header("cookie", "JSESSIONID="+sessionID+"").
		
		when().
		delete("/rest/api/2/issue/RAT-1/comment/" +cmntID+ ""). //resources
	then()
		.assertThat().statusCode(204).log().all(); //status cod we are getting from API doc
}
	
	
	
	public static String generateString(String filePath) throws IOException {
		
		return new String (Files.readAllBytes(Paths.get(filePath)));
		
	}
}
