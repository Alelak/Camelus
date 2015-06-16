package test;

import java.util.List;

import com.devsolutions.camelus.entities.Order;
import com.devsolutions.camelus.entities.OrderTV;
import com.devsolutions.camelus.managers.OrderManager;

public class OrderManagerTest {

	public void insert() {
		Order o = new Order();
		o.setClient_id(1);
		o.setVendor_id(1);
		o.setComment("hello");
		OrderManager.add(o);
		System.out.println("Record Inserted");
	}

	public void getByVendorId() {
		List<OrderTV> orders = OrderManager.getByVendorId(1);
		for (OrderTV order : orders) {
			System.out.println(order.getComment());
		}
	}

	public void getByClientId() {
		List<Order> orders = OrderManager.getByClientId(1);
		for (Order order : orders) {
			System.out.println(order.getVendor_id());
		}
	}

	public void getAll() {
		List<Order> orders = OrderManager.getAll();
		for (Order order : orders) {
			System.out.println(order.getClient_id());
		}
	}

	public void getById() {

		System.out.println(OrderManager.getById(1).getClient_id());

	}

	public static void main(String[] args) {
		OrderManagerTest orderManagerTest = new OrderManagerTest();
		orderManagerTest.insert();
		orderManagerTest.getByVendorId();
		orderManagerTest.getByClientId();
		orderManagerTest.getAll();
		orderManagerTest.getById();
	}

}
