package util;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


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

	/**
	 * Logs a Magento user into the website
	 * 
	 * @param email,
	 *            user login email
	 * @param password,
	 *            user login password
	 * @throws InterruptedException
	 */
	public void login(String email, String password) throws InterruptedException {
		driver.get(baseURL + "customer/account/login/");
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys(email);
		driver.findElement(By.id("pass")).clear();
		driver.findElement(By.id("pass")).sendKeys(password);
		driver.findElement(By.id("send2")).click();
		Thread.sleep(500);

	}
	
	public void logout() {
		driver.get(baseURL);
		driver.findElement(By.xpath("/html/body/div[1]/header/div[1]/div/ul/li[2]/div/ul/li[3]/a"));
	}

	/**
	 * Adds item to a Mangento user's cart
	 * 
	 * @param section,
	 *            section that the item is located
	 * @param itemID,
	 *            the ID of an item, in this case XPATH of the item object
	 * @param quantity,
	 *            quantity that will be added to the cart
	 * @throws Exception
	 */
	public void addItem(String section, String itemID, int quantity, String ariaLabelSize, String ariaLabelColor) throws Exception {
		String url = null;
		section = section.toLowerCase();
		if (section == "men") {
			url = baseURL + "men.html";
		} else if (section == "women") {
			url = baseURL + "women.html";
		} else if (section == "gear") {
			url = baseURL + "gear.html";
		} else if (section == "training") {
			url = baseURL + "training.html";
		} else if (section == "sale") {
			url = baseURL + "sale.html";
		} else {
			throw new Exception("Invalid section in magento utility!");
		}
		driver.get(url);
		driver.findElement(By.xpath(itemID)).click();
		Thread.sleep(3000);
		if(ariaLabelSize != null) {
			(new WebDriverWait(driver, 10))
			.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-label=\"" + ariaLabelSize + "\"]"))).click();
		}
		if(ariaLabelColor != null) {
			(new WebDriverWait(driver, 10))
			.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-label=\"" + ariaLabelColor + "\"]"))).click();
		}
	
		
		
		Thread.sleep(500);
	}

	/**
	 * Removes an item from the cart
	 * 
	 * @param itemID,
	 *            the ID of the item to be removed, in this case the position in the
	 *            cart
	 * @param quantity,
	 *            the specific quantity in the cart of that item
	 */
	public void removeItem(String itemID, int quantity) {
		driver.get(baseURL);
		driver.findElement(By.xpath("/html/body/div[1]/header/div[2]/div[1]/a")).click();
		driver.findElement(By.xpath("//*[@id=\"mini-cart\"]/li[" + itemID + "]/div/div/div[3]/div[2]/a")).click();
		driver.findElement(By.xpath("/html/body/div[2]/aside[2]/div[2]/footer/button[2]")).click();
	}

	/**
	 * Updates the amount of an item within a cart
	 * 
	 * @param itemID,
	 *            the ID of the item to be removed, in this case the position in the
	 *            cart
	 * @param oldQuantity,
	 *            the old quantity of a specific item in a cart
	 * @param newQuantity,
	 *            the new quantity of a specific item in a cart
	 */
	public void updateItem(String itemID, int oldQuantity, int newQuantity) {

	}

	/**
	 * Removes an item completely from the cart through the update method
	 * 
	 * @param itemID,
	 *            ID of the item to be removed, index in the cart
	 * @param oldQuantity,
	 *            the current amount of a specific item in a cart
	 */
	public void updateRemove(String itemID, int oldQuantity) {
		updateItem(itemID, oldQuantity, 0);
	}

	/**
	 * Delete all the items from a cart
	 * 
	 * @throws InterruptedException
	 */
	public void deleteCart() throws InterruptedException {
		String url = baseURL + "checkout/cart";
		driver.get(url);
		List<WebElement> elements = (new WebDriverWait(driver, 10))
					.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("action_action-delete")));
		for (WebElement element : elements) {
			element.click();
		}
		Thread.sleep(500);
	}

	/**
	 * Performs the checkout operation for a Magento user
	 * @param shipOption, 0 for digital only cart, 1 for Bestway with at least 1 physical item, 2 for standard with at least 1 physical item
	 * @param paymentOption, 1 paypal, 2 credit card, 3 cash, 4 bank transfer, 5 purchase order, 6 money order
	 * @param ccNum, credit card number
	 * @param expMonth, credit card expiration month TWO characters
	 * @param expYear, credit card expiration year TWO characters
	 * @param verNum, verification number for credit card
	 * @return the order number
	 * @throws Exception
	 */
	public int checkout(int shipOption, int paymentOption, String ccNum, String expMonth, String expYear, String verNum)
			throws Exception {
		String url = baseURL + "checkout";
		driver.get(url);
		
		//options 1 and 2 must have at least 1 physical item in cart
		//option 0 is for a cart with digital items only
		try {
			if (shipOption == 1) {
				WebElement shipElement = (new WebDriverWait(driver, 10))
										.until(ExpectedConditions.presenceOfElementLocated(
										By.xpath("//*[@id=\"checkout-shipping-method-load\"]/table/tbody/tr[1]/td[1]/input")));
				if (!shipElement.isSelected()) {
					shipElement.click();
				}
				driver.findElement(By.xpath("//*[@id=\"shipping-method-buttons-container\"]/div/button")).click();
			} else if (shipOption == 2) {
				WebElement shipElement = (new WebDriverWait(driver, 10))
										.until(ExpectedConditions.presenceOfElementLocated(
										By.xpath("//*[@id=\"checkout-shipping-method-load\"]/table/tbody/tr[2]/td[1]/input")));
				if (!shipElement.isSelected()) {
					shipElement.click();
				}
				driver.findElement(By.xpath("//*[@id=\"shipping-method-buttons-container\"]/div/button")).click();
			} else if (shipOption == 0) {
				System.out.println("Only digital items allowed in cart for shipping option 0");
			} else {
				throw new Exception("Invalid shipping option!");
			}

		} catch (Exception e) {
			System.out.println("Item is probably digital!");
		}
		
		
		WebElement payment;
		if (paymentOption == 1) {
			payment = (new WebDriverWait(driver, 30))
					.until(ExpectedConditions.presenceOfElementLocated(By.id("braintree_paypal")));
		} else if (paymentOption == 2) {
			payment = (new WebDriverWait(driver, 30))
					.until(ExpectedConditions.presenceOfElementLocated(By.id("braintree")));
		} else if (paymentOption == 3) {
			payment = (new WebDriverWait(driver, 30))
					.until(ExpectedConditions.presenceOfElementLocated(By.id("cashondelivery")));
		} else if (paymentOption == 4) {
			payment = (new WebDriverWait(driver, 30))
					.until(ExpectedConditions.presenceOfElementLocated(By.id("banktransfer")));
		}else if (paymentOption == 5){
			payment = (new WebDriverWait(driver, 30))
					.until(ExpectedConditions.presenceOfElementLocated(By.id("purchaseorder")));
		}else if (paymentOption == 6){
			payment = (new WebDriverWait(driver, 30))
					.until(ExpectedConditions.presenceOfElementLocated(By.id("checkmo")));
		}else {
			throw new Exception("Invalid payment option!");
		}
		if (!payment.isSelected()) {
			payment.click();
		}
		
		if(paymentOption == 2) {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"braintree-hosted-field-number\"]")));
			(new WebDriverWait(driver, 10))
			.until(ExpectedConditions.presenceOfElementLocated(By.id("credit-card-number"))).clear();
			(new WebDriverWait(driver, 10))
			.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"credit-card-number\"]"))).sendKeys(ccNum);
			driver.switchTo().defaultContent();
			driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"braintree-hosted-field-expirationMonth\"]")));
			(new WebDriverWait(driver, 10))
			.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"expiration-month\"]"))).clear();
			(new WebDriverWait(driver, 10))
			.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"expiration-month\"]"))).sendKeys(expMonth);
			driver.switchTo().defaultContent();
			driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"braintree-hosted-field-expirationYear\"]")));
			(new WebDriverWait(driver, 10))
			.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"expiration-year\"]"))).clear();
			(new WebDriverWait(driver, 10))
			.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"expiration-year\"]"))).sendKeys(expYear);
			driver.switchTo().defaultContent();
			driver.switchTo().frame(driver.findElement(By.xpath("//*[@id=\"braintree-hosted-field-cvv\"]")));
			(new WebDriverWait(driver, 10))
			.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"cvv\"]"))).clear();
			(new WebDriverWait(driver, 10))
			.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"cvv\"]"))).sendKeys(verNum);
			driver.switchTo().defaultContent();
		}
	
		(new WebDriverWait(driver, 10))
		.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"checkout-payment-method-load\"]/div/div/div[3]/div[2]/div[4]/div/button"))).click();

//		String orderNumString = driver.findElement(By.xpath("//*[@id=\'maincontent\']/div[3]/div/div[2]/p[1]/a/strong"))
//				.toString();
//		int orderNum = Integer.parseInt(orderNumString);
//
//		return orderNum;
		return 0;

	}

	
	
	public boolean logged(String userID){
		try {
			driver.findElement(By.xpath("/html/body/div[1]/header/div[1]/div/ul/li[2]/span/span"));
			return true;
		}catch(Exception e) {
			return false;
		}
	}
}
