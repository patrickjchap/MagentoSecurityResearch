import static org.junit.Assert.*;


import java.io.File;
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

import util.*;

public class BasicPurchases {
	private UserUtilities utility = new UserUtilities();
	
	
	@Test
	public void test() {
		try {
			utility.login("roni_cost@example.com", "roni_cost3@example.com");
			utility.addItem("women", "//*[@id=\"maincontent\"]/div[4]/div[1]/div[1]/div[3]/div/div/ol/li[4]/div/div/strong/a", 1, "28", "Gray");
			int orderNum = utility.checkout(2, 2, "378282246310005", "05", "19", "9978");
			System.out.println(orderNum);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	

}
