package test;

import com.devsolutions.camelus.entities.Client;
import com.devsolutions.camelus.managers.ClientManager;

public class ClientManagerTest {

	public void insert() {

		Client c = new Client();

		c.setEnterprise_name("Massico");
		c.setAddress("Alger la blanche");
		c.setAssociated_vendor(1);
		c.setContact_name("Alaa");
		c.setContact_email("alaa@massico.co");
		c.setContact_tel("111111111");
		c.setDescription("Best Co in the world!");

		ClientManager.add(c);
		System.out.println("Record inserted");
	}

	public void update() {
		Client c = new Client();

		c.setEnterprise_name("Massico");
		c.setAddress("Alger la blanche");
		c.setAssociated_vendor(1);
		c.setContact_name("Alaa");
		c.setContact_email("alaa@massico.co");
		c.setContact_tel("235662777");
		c.setDescription("Best Co in the world!");

		ClientManager.update(c);
		System.out.println("Record updated");
	}

	public void getById() {

		System.out.println(ClientManager.getById(1).getEnterprise_name());
	}

	public void delete() {
		ClientManager.delete(1);
		System.out.println("Record deleted");
	}

	public static void main(String[] args) {
		ClientManagerTest clientManagerTest = new ClientManagerTest();
		clientManagerTest.insert();
		clientManagerTest.update();
		clientManagerTest.getById();
		clientManagerTest.delete();
	}
}
