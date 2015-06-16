package test;

import java.util.List;

import com.devsolutions.camelus.entities.OrderLine;
import com.devsolutions.camelus.entities.OrderLineTV;
import com.devsolutions.camelus.managers.OrderLineManager;

public class OrderLineManagerTest {

	public void insert() {
		OrderLine orderLine = new OrderLine();
		orderLine.setOrder_id(1);
		orderLine.setProduct_id(1);
		orderLine.setPrice(99.95);
		orderLine.setQuantity(10);
		orderLine.setModified_price(98.88);
		OrderLineManager.add(orderLine);
		System.out.println("Record Inserted");
	}

	public void getByOrderId() {
		List<OrderLineTV> orderLines = OrderLineManager.getByOrderId(1);
		for (OrderLineTV orderLine : orderLines) {
			System.out.println(orderLine.getPrice());
		}
	}

	public static void main(String[] args) {
		OrderLineManagerTest orderLineManagerTest = new OrderLineManagerTest();
		orderLineManagerTest.insert();
		orderLineManagerTest.getByOrderId();
	}

}
