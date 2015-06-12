package test;


import java.util.Date;
import java.util.List;

import com.devsolutions.camelus.entities.Admin;
import com.devsolutions.camelus.managers.AdminManager;

public class AdminManagerTest {
	public void insert() {

		Admin admin = new Admin();

		admin.setLogin("megamassi1");
		admin.setPassword("1234");
		admin.setFname("massi");
		admin.setLname("aberbache");
		admin.setSin(123456789);
		
		Date date = new Date();
		admin.setHire_date(date);
		

		AdminManager.add(admin);
		System.out.println("Record inserted");
	}
	
	public void update() {
		Admin admin = new Admin();

		admin.setId(1);
		admin.setLogin("megamassi1");
		admin.setPassword("123456");
		admin.setFname("massi");
		admin.setLname("aberbache");
		admin.setSin(123456789);
		
		Date date = new Date();
		admin.setHire_date(date);

		AdminManager.update(admin);
		System.out.println("Record updated");
	}
	
	public void delete() {
		AdminManager.delete(1);
		System.out.println("Record deleted");
	}
	
	public void getByUsername() {
		Admin admin = AdminManager.getByUserName("megamassi");
		System.out.println(admin.getLname());
	}
	
	public void getAll()
	{
		List<Admin> admins = AdminManager.getAll();
		
		for(Admin admin : admins){
			System.out.println(admin.getLname());
		}
	}
	
	public static void main(String[] args) {
		AdminManagerTest adminManagerTest = new AdminManagerTest();
		adminManagerTest.insert();
		adminManagerTest.update();
		adminManagerTest.getByUsername();
		adminManagerTest.delete();
		adminManagerTest.getAll();
	}
}
