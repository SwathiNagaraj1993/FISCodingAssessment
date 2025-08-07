package fisCodingAssessment;

import java.time.Duration;
import java.util.List;
import java.util.Set;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;


public class uiAutomation {
	
	@Test
	public void UITest()
	{
		//1.	Open browser
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\muttugadahalli.prade\\eclipse-workspace\\fisCodingAssessment\\src\\fisCodingAssessment\\chromedriver-win64\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		//2.	Navigate to ebay.com
		driver.get("https://www.ebay.com/");
		//3.	Search for ‘book’
		driver.findElement(By.id("gh-ac")).sendKeys("book");
		driver.findElement(By.id("gh-search-btn")).click();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
		wait.until(ExpectedConditions.titleIs("Book for sale | eBay"));
		List<WebElement> listOfBooks = driver.findElements(By.xpath("//ul[@class='srp-results srp-list clearfix']//*[contains(@class,'image-treatment')]"));
		String parentWindow = driver.getWindowHandle();
		//4.	Click on the first book in the list
		listOfBooks.get(0).click();
		Set<String> windows = driver.getWindowHandles();
		for(String w:windows)
			{
			if(!w.equals(parentWindow))
			{
				String[] initialItemCntArray= driver.findElement(By.xpath("//*[@class='gh-cart__icon']")).getAttribute("aria-label").split("contains");
				String[] initialItemCnt = initialItemCntArray[1].trim().split(" ");
				int intialItemCount = Integer.parseInt(initialItemCnt[0].trim());
				driver.switchTo().window(w);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='atcBtn_btn_1']")));
				JavascriptExecutor js = (JavascriptExecutor) driver;
				//5.	In the item listing page, click on ‘Add to cart’
				WebElement addCartBtn = driver.findElement(By.xpath("//*[@id='atcBtn_btn_1']"));
				js.executeScript("arguments[0].scrollIntoView(true);", addCartBtn);
				addCartBtn.click();
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='ux-textspans' and text()='Added to cart']")));
				Assert.assertEquals("Added to cart", driver.findElement(By.xpath("//*[@class='ux-textspans' and text()='Added to cart']")).getText());
				driver.findElement(By.xpath("//*[@class='ux-textspans' and text()='Added to cart']/../../..//button[@class='icon-btn lightbox-dialog__close']")).click();
				String[] finalItemCntArray= driver.findElement(By.xpath("//*[@class='gh-cart__icon']")).getAttribute("aria-label").split("contains");
				String[] finalItemCnt = finalItemCntArray[1].trim().split(" ");
				//6.	verify the cart has been updated and displays the number of items in the cart as shown below in yellow.
				int finaItemCount = Integer.parseInt(finalItemCnt[0].trim());
				if(intialItemCount+1 == finaItemCount)
				{
					System.out.println("Cart is updated");
					wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@class='gh-cart__icon']"))));
					js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//*[@class='badge gh-badge']")));
					if((finaItemCount == Integer.parseInt(driver.findElement(By.xpath("//*[@class='badge gh-badge']")).getText()) && driver.findElement(By.xpath("//*[@class='badge gh-badge']")).isDisplayed()))
					{
						System.out.println("The number of items in the cart is same and is displayed");
					}
					else{
						System.out.println("The number of items in the cart is not same and not displayed");
					}
				}
				else
				{
					System.out.println("Cart is not updated");
				}
				
			}
		
			}
		driver.quit();
		

	}

}
