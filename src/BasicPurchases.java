import static org.junit.Assert.*;
import org.junit.Test;


import util.*;

public class BasicPurchases {
	private UserUtilities utility = new UserUtilities();
	
	
	@Test
	public void test() {
		try {
			utility.login("roni_cost@example.com", "roni_cost3@example.com");
			utility.sectionAddItem("women", "//*[@id=\"maincontent\"]/div[4]/div[1]/div[1]/div[3]/div/div/ol/li[4]/div/div/strong/a", 1, "28", "Gray");
			int orderNum = utility.checkout(2, 2, "378282246310005", "05", "19", "9978");
			System.out.println(orderNum);
		}catch(Exception e) {
			fail(e.getMessage());
		}
	}
	
	

}
