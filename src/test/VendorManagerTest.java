package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.managers.VendorManager;

public class VendorManagerTest {
	public void insert() {

		Vendor vendor = new Vendor();

		vendor.setLogin("megamassi1");
		vendor.setPassword("1234");
		vendor.setFname("massi");
		vendor.setLname("aberbache");
		vendor.setSin("123456789");
		vendor.setCommission_id(1);
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
		
		Date date = new Date();
		try {
			date = ft.parse("2015-06-01");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		vendor.setHire_date(date);
		

		VendorManager.add(vendor);
		System.out.println("Record inserted");
	}
	
	public void update() {
		Vendor vendor = new Vendor();

		vendor.setId(1);
		vendor.setLogin("megamassi");
		vendor.setPassword("123456");
		vendor.setFname("massinissa");
		vendor.setLname("Aberbache");
		vendor.setSin("1234567890");
		vendor.setCommission_id(1);
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
		
		Date date = new Date();
		try {
			date = ft.parse("2015-06-02");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		vendor.setHire_date(date);

		VendorManager.update(vendor);
		System.out.println("Record updated");
	}
	
	public void delete() {
		VendorManager.delete(1);
		System.out.println("Record deleted");
	}
	
	public void getByUsername() {
		Vendor vendor = VendorManager.getByUserName("megamassi1");
		System.out.println(vendor.getLname());
	}
	
	
	
	public void getAll() {
		List<Vendor> vendors = VendorManager.getAll();
		
		for(Vendor vendor : vendors){
			System.out.println("lname : " + vendor.getLname());
			System.out.println("fname : " + vendor.getFname());
			System.out.println("commission_id : " + vendor.getCommission_id());
			System.out.println("deleted : " + vendor.getDeleted());
			System.out.println("id : " + vendor.getId());
			System.out.println("login : " + vendor.getLogin());
			System.out.println("pw : " + vendor.getPassword());
			System.out.println("sin : " + vendor.getSin());
			System.out.println("updated_at : " + vendor.getUpdated_at());
			System.out.println("created_at : " + vendor.getCreated_at());
			System.out.println("hire_date : " + vendor.getHire_date());
			System.out.println("***************************");
		}
	}
	
	public static void main(String[] args) {
		VendorManagerTest vendorManagerTest = new VendorManagerTest();
		vendorManagerTest.insert();
		//vendorManagerTest.update();
		//vendorManagerTest.delete();
		//vendorManagerTest.getByUsername();
		//vendorManagerTest.getByName();
		//vendorManagerTest.getAll();
	}
}
