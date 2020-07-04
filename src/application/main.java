package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class main {

	private static Scanner sc = new Scanner(System.in);
	private static Connection conn;
	String order = "-1";

	private static int customerId = 0;
	private static int restaurantId = 0;


	public static void main(String[] args) { // within this method, connection to the database is made
		System.out.println("Hello World");


		final String connectionStr = "jdbc:mysql://127.0.0.1:3306/dominoesDB?serverTimezone=UTC";

		try {
			conn = DriverManager.getConnection(connectionStr, "root", "Mango1279!");
			System.out.println("Succesfully connected!");


		} catch (SQLException e) {
			System.out.println("Unable to connect to database");
			e.printStackTrace();
		}

		System.out.println("\n");
		System.out.println("Welcome!");

		menu.showOptions();
	}

	public static void createProfile(String customer_name, String address, String phone_number) {
		// this method creates a profile for the customer. A customer id is
		// auto-generated. The customer will need to
		// enter their data(name address, phone number)

		final String createProfileQuery = "INSERT INTO customer (customer_name, address, phone_number) VALUES (?, ?, ?)";

		try {
			PreparedStatement ps = conn.prepareStatement(createProfileQuery);
			ps.setString(1, customer_name);
			ps.setString(2, address);
			ps.setString(3, phone_number);

			ps.executeUpdate();

			System.out.println("Profile created successfully!");
			getCustomerId(customer_name);

		} catch (SQLException e) {
			System.out.println("Error in createProfile query");
			e.printStackTrace();
		}

		menu.showOptions();
	}

	public static int getCustomerId(String customer_name) { // this method will get a customer's id #.
		// The customer can input their name and the method will return the customer Id
		// so that they can
		// place their order.
		final String getCustomerIdQuery = "Select id FROM customer WHERE customer_name = ?";
		// int customer_id = 0;

		try {
			PreparedStatement statement = conn.prepareStatement(getCustomerIdQuery);
			statement.setString(1, customer_name);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				customerId = rs.getInt("id");
				System.out.println("Your customer ID is: " + customerId);
			}

		} catch (SQLException e) {
			System.out.println("Error in getCustomerId query");
			e.printStackTrace();
		}
		System.out.println("\n");
		menu.showOptions();
		return customerId;

	}

	public static int getRestaurantId(int id) { // this method will get a restaurants's id #.
		// The customer can input their name and the method will return the customer Id
		// so that they can
		// place their order.
		final String getRestaurantIdQuery = "Select id FROM restaurant WHERE id = ?";

		try {
			PreparedStatement statement = conn.prepareStatement(getRestaurantIdQuery);
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				restaurantId = rs.getInt("id");
				System.out.println("Restaurant location: " + restaurantId);

			}

		} catch (SQLException e) {
			System.out.println("Error in getRestaurantId query");
			e.printStackTrace();
		}
		System.out.println("\n");
		menu.showOptions();
		return restaurantId;

	}

	public static void showMenu() {// this method show the menu
		final String showMenuQuery = "Select item, description, price FROM items";

		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(showMenuQuery);

			while (rs.next()) {
				System.out.println(
						rs.getString("item") + " " + rs.getString("description") + " " + rs.getDouble("price"));
			}

		} catch (SQLException e) {
			System.out.println("Error in showMenu query");
			e.printStackTrace();
		}
		System.out.println("\n");
		menu.showOptions();
	}

	public static void addItem(String item, int quantity) {
		// This method will add items to the order. This should be on a continuous loop
		// until customer
		// states order is complete.
		// customerId = customer_id;
		// restaurantId = restaurant_id;

		final String addItemQuery = "INSERT INTO customerOrder(customer_id, restaurant_id,item_name, quantity) VALUE (?,?,?,?);";
		try {
			PreparedStatement ps = conn.prepareStatement(addItemQuery);
			ps.setInt(1, customerId);
			ps.setInt(2, restaurantId);
			ps.setString(3, item);
			ps.setInt(4, quantity);


			ps.executeUpdate();

			System.out.println(quantity + " " + item + "('s) added successfully!");

		} catch (SQLException e) {
			System.out.println("Error in add item query");
			e.printStackTrace();
		}
		System.out.println("\n");
		menu.showOptions();

	}

	public static void removeItem(String item) {
		// This method will remove items from the order.
		final String removeItemQuery = "delete from customerOrder where customer_id = ? and item_name = ?";

		try {
			PreparedStatement ps = conn.prepareStatement(removeItemQuery);
			ps.setInt(1, customerId);
			ps.setString(2, item);

			ps.executeUpdate();

			System.out.println("item:" + item + " item removed successfully");

		} catch (SQLException e) {
			System.out.println("Error in remove item query");
			e.printStackTrace();
		}
		System.out.println("\n");
		menu.showOptions();
	}

	public static void updateQuantity(String item, int quantity) { // This method will update the quantity of items
		// in an order.
		final String updateQuantityQuery = "update customerOrder set quantity = ? where customer_id = ? and item_name = ?;";

		try {
			PreparedStatement ps = conn.prepareStatement(updateQuantityQuery);
			ps.setInt(1, quantity);
			ps.setInt(2, customerId);
			ps.setString(3, item);

			ps.executeUpdate();

			System.out.println(quantity + " " + item + "('s) updated successfully!");

		} catch (SQLException e) {
			System.out.println("Error in update query");
			e.printStackTrace();
		}
		System.out.println("\n");
		menu.showOptions();
	}

	public static void invalidSelection() { // this method tells the user the option selected is invalid from the main
		// menu.
		System.out.println("Not a valid choice. Please try again");
		menu.showOptions();
	}

	/*
	 * public static void orderTotal(int id) { final String orderTotalQuery =
	 * "select sum(quantity*price) AS total FROM customerOrder where customer_id =? ;"
	 * ; Double total = 0.00; try { PreparedStatement ps =
	 * conn.prepareStatement(orderTotalQuery); ps.setInt(1, customerId); ResultSet
	 * rs = ps.executeQuery();
	 *
	 * while (rs.next()) { System.out.println("The total of your is:" + "$" +
	 * rs.getDouble(total + "('s)")); } } catch (SQLException e) {
	 * System.out.println("Error in totalOrder query"); e.printStackTrace(); }
	 *
	 * }
	 */

	public static void showCustomerOrder(int id) { // This method displays the customer's order
		final String showCustomerOrderQuery = "select co.item_name, co.quantity from customerOrder co where customer_id = ?;";

		try {
			PreparedStatement ps = conn.prepareStatement(showCustomerOrderQuery);
			ps.setInt(1, customerId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				System.out.println(rs.getString("item_name") + " " + rs.getInt("quantity"));
			}

		} catch (SQLException e) {
			System.out.println("Error in showCustomerOrder query");
			e.printStackTrace();
		}
		System.out.println("\n");

		menu.showOptions();
	}

	public static void createMenuAdd() { // this method displays the restaurant locations
		System.out.println("Select location: \n 1)Uptown \n 2)Downtown \n 3)Midtown");
		System.out.println("Enter id of restaurant location");
		int restaurant_id = sc.nextInt();

	}
}
