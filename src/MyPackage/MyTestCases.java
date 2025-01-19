package MyPackage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class MyTestCases extends Parametes {

	String username;

	@BeforeTest
	public void mySetUp() throws SQLException {

		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels", "root", "obada");

		driver.manage().window().maximize();
		driver.get(Website);

	}

	@Test(priority = 1, enabled = true)
	public void AddNewCustomer() throws SQLException {

		stmt = con.createStatement();
		int effectedRow = stmt.executeUpdate(QueryToAdd);
		Assert.assertEquals(effectedRow, 1);

	}

	@Test(priority = 2, enabled = true)
	public void UpdateCustomerInfo() throws SQLException {

		stmt = con.createStatement();
		int effectedRow = stmt.executeUpdate(QueryToUpdate);
		Assert.assertEquals(effectedRow, 1);

	}

	@Test(priority = 3, enabled = true)
	public void ReadTheUpdatedData() throws SQLException, InterruptedException, IOException {

		stmt = con.createStatement();

		rs = stmt.executeQuery(ReadQuery);

		while (rs.next()) {

			String contactFirstName = rs.getString("contactFirstName");

			username = contactFirstName;

			String contactlastName = rs.getString("contactLastName");

			String randemailID = Integer.toString(rand.nextInt(999));

			String CityOfTheCustomer = rs.getString("city");

			Assert.assertEquals(contactFirstName.length() > 0, true);
			Assert.assertEquals(CityOfTheCustomer.length() > 0, true);

			int contactId = Integer.parseInt(rs.getString("customerNumber"));

			driver.findElement(By.id("customer[first_name]")).sendKeys(contactFirstName);
			driver.findElement(By.id("customer[last_name]")).sendKeys(contactlastName);
			driver.findElement(By.id("customer[email]"))
					.sendKeys(contactFirstName + contactlastName + randemailID + "@gmail.com");
			driver.findElement(By.id("customer[password]")).sendKeys(password);
			Thread.sleep(2000);
			driver.findElement(By.cssSelector(".form__submit.button.button--primary.button--full")).click();

		}
		Thread.sleep(2000);

		String WelcomeMessage = driver
				.findElement(By.cssSelector(".header__action-item-title.hidden-pocket.hidden-lap")).getText();

		Assert.assertEquals(WelcomeMessage.contains(username), true);

		System.out.println(username);
		System.out.println(WelcomeMessage);

		TakeScreenShot();

	}

	@Test(priority = 4, enabled = true)
	public void DeleteCustomer() throws SQLException {

		stmt = con.createStatement();
		int effectedRow = stmt.executeUpdate(DeleteQuery);

		System.out.println(effectedRow);
		Assert.assertEquals(effectedRow, 1);

	}

}
