package fisCodingAssessment;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

import java.util.Map;

public class apiAutomation {
	
	@Test
	public void apiTest()
	{
		RestAssured.baseURI="https://api.coingecko.com/api/v3/coins/bitcoin";
		String response = given().header("Content-Type","application/json")
				//1.	Send the GET request
		.when().get()
		.then().assertThat().statusCode(200)
		// c. The price change percentage over the last 24 hours
		.body("market_data", hasKey("price_change_percentage_24h"))
		//b.	Each cryptocurrency has a  market cap, and total volume.
		.body("market_data", hasKey("market_cap"))
		.body("market_data", hasKey("total_volume"))
		.extract().response().asString();
		
		JsonPath js = new JsonPath(response);
		//2.	Verify the response contains-->There are 3 BPIs USD,GBP,EUR

		Map<String,Long> currentPrice = js.getMap("market_data.current_price");
		Map<String,Long> marketCap = js.getMap("market_data.market_cap");
		Map<String,Long> totalVolume = js.getMap("market_data.total_volume");
		String[] expectedBPI = {"USD","GBP","EUR"};
		for(String s:expectedBPI)
		{
			if(currentPrice.containsKey(s.toLowerCase()))
			{
				System.out.println(s+" is present in Current Price");
			}
			if(marketCap.containsKey(s.toLowerCase()))
			{
				System.out.println(s+" is present in Market cap");
			}
			if(totalVolume.containsKey(s.toLowerCase()))
			{
				System.out.println(s+" is present in Total Volume");
			}
			
		}
		
		//D.Verify homepage URL is not empty 
		if(js.getString("links.homepage") != "")
		{
			System.out.println("Verify homepage URL is not empty: "+js.getString("links.homepage"));
		}
		else
		{
			System.out.println("Verify homepage URL is empty "); 
		}
		
		
	}

}
