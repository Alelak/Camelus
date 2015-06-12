package test;

import java.util.List;

import com.devsolutions.camelus.entities.Commission;
import com.devsolutions.camelus.managers.CommissionManager;

public class CommissionManagerTest {

	public void insert() {
		Commission commission = new Commission();
		commission.setType(0);
		commission.setRate(10);
		commission.setMcondition(100);
		CommissionManager.add(commission);
		System.out.println("Record Inserted");
	}

	public void getAll() {

		List<Commission> commissions = CommissionManager.getAll();
		for (Commission c : commissions) {
			System.out.println(c.getRate());
		}
	}

	public static void main(String[] args) {
		CommissionManagerTest commissionManagerTest = new CommissionManagerTest();
		commissionManagerTest.insert();
		commissionManagerTest.getAll();
	}

}
