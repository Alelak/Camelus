package test;

import java.util.List;

import com.devsolutions.camelus.entities.Unit;
import com.devsolutions.camelus.managers.UnitManager;

public class UnitManagerTest {

	public void insert() {

		UnitManager.add("kg");
		System.out.println("Record inserted");
	}

	public void getAll() {
		List<Unit> units = UnitManager.getAll();
		for (Unit u : units) {
			System.out.println(u.getDescription());
		}
	}

	public void delete() {
		UnitManager.delete(1);
	}

	public static void main(String[] args) {
		UnitManagerTest unitManagerTest = new UnitManagerTest();
		unitManagerTest.insert();
		unitManagerTest.getAll();
		unitManagerTest.delete();

	}

}
