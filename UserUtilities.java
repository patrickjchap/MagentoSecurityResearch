package util;
import static org.junit.Assert.*;


import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.junit.Test;

public class UserUtilities {
	WebDriver driver;
	private String baseURL = "http://127.0.1/magento/";
	
	public UserUtilities() {
		buildDriver();
	}
	
	private void buildDriver() {
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		this.driver = new ChromeDriver();	
	}
	
	public WebDriver getDriver() {
		return driver;
	}
	
	public void loginUser(String email, String password) {
		driver.get(baseURL + "customer/account/login/");
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys(email);
		driver.findElement(By.id("pass")).clear();
		driver.findElement(By.id("pass")).sendKeys(password);
		driver.findElement(By.id("send2")).click();
		
	}
	
	/**
	 * Selenium utility for magento to add item to
	 * cart. Currently only uses basic sections (ex: men, women, etc...)
	 * 
	 * @param section, section to grab item from
	 * @param itemID, if the item has a unique ID else use xpath
	 * @param xpath, if using xpath
	 * @throws Exception, if invalid section is passed in
	 */
	public void addToCart(String section, String itemID, boolean xpath) throws Exception {
		String url = null;
		section = section.toLowerCase();
		if(section == "men") {
			url = baseURL + "men.html";
		}else if(section == "women") {
			url = baseURL + "women.html";
		}else if(section == "gear") {
			url = baseURL + "gear.html";
		}else if(section == "training") {
			url = baseURL + "training.html";
		}else if(section == "sale") {
			url = baseURL + "sale.html";
		}else {
			throw new Exception("Invalid section in magento utility!");
		}
		driver.get(url);
		if(!xpath) {
			driver.findElement(By.id(itemID)).click();
		}else {
			driver.findElement(By.xpath(itemID)).click();
		}
	}
	
	public void deleteCart() {
			String url = baseURL +"checkout/cart";
			driver.get(url);
			List<WebElement> elements = driver.findElements(By.className("action_action-delete"));
			for(WebElement element: elements) {
				element.click();
			}
	}
	
	
	/**
	 * 
	 * @param shipOption, 1 for bestway, 2 for flat rate
	 * @param paymentOption, 1 for cash on delivery, 2 for bank transfer, 3 for purchase order, 4 for check money order
	 * @param paymentInput, Info to be provided to payment method (currently not implemented)
	 * @return orderNumber, returns the number of your order
	 */
	public int checkoutCart(int shipOption, int paymentOption, String paymentInput) throws Exception {
		String url = baseURL + "checkout";
		driver.get(url);
		if(shipOption == 1) {
			driver.findElement(By.xpath("//input[@value='tablerate_bestway']")).click();
		}else if(shipOption == 2) {
			driver.findElement(By.xpath("//input[@value='flatrate_flatrate']")).click();	
		}else {
			throw new Exception("Invalid shipping option!");
		}
		
		driver.findElement(By.xpath("//*[@id=\'shipping-method-buttons-container\']/div/button")).click();
		
		if(paymentOption == 1) {
			driver.findElement(By.xpath("//input[@value='cashondelivery']")).click();
		}else if(paymentOption == 2) {
			driver.findElement(By.xpath("//input[@value='banktransfer']")).click();
		}else if(paymentOption == 3) {
			driver.findElement(By.xpath("//input[@value='purchaseorder']")).click();
		}else if(paymentOption == 4) {
			driver.findElement(By.xpath("//input[@value='checkmo']")).click();
		}else {
			throw new Exception("Invalid payment option!");
		}
		
		driver.findElement(By.xpath("//*[@id=\'checkout-payment-method-load\']/div/div/div[2]/div[2]/div[4]/div/button")).click();
		
		String orderNumString = driver.findElement(By.xpath("//*[@id=\'maincontent\']/div[3]/div/div[2]/p[1]/a/strong")).toString();
		int orderNum = Integer.parseInt(orderNumString);
		
		return orderNum;
		
	}
}
