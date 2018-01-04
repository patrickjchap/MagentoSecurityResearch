import static org.junit.Assert.*;

import org.junit.Test;

import util.*;

public class ComplexTest {
	private UserUtilities utility = new UserUtilities();

	@Test
	public void test() {
		try {
			utility.login("roni_cost@example.com", "roni_cost3@example.com");
			utility.searchAddItem("pink shirt", "//*[@id='maincontent']/div[3]/div[1]/div[2]/div[2]/ol/li[1]/div/div/strong/a", 1, "M", "Purple");
			utility.sectionAddItem("men/tops-men", "//*[@id='maincontent']/div[3]/div[1]/div[3]/ol/li[2]/div/div/strong/a", 3, "S", "Blue");
			utility.removeItem("1");
			utility.updateItem("1", 2);
			int orderNum = utility.checkout(2, 2, "378282246310005", "05", "19", "9978");
			System.out.println(orderNum);
			
		}catch(Exception e) {
			System.out.println(e.getStackTrace());
			fail(e.getMessage());
		}
	}

}
