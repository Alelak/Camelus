package test;

import java.util.List;

import com.devsolutions.camelus.entities.Product;
import com.devsolutions.camelus.managers.ProductManager;

public class ProductManagerTest {

	public void insert() {

		Product product = new Product();

		product.setUpc(1234567890);
		product.setName("metal");
		product.setQuantity(20);
		product.setUnit_id(1);
		product.setDescription("du metal à vie!");
		product.setCategory_id(1);
		product.setImg_url("monDossier/image.png");

		ProductManager.add(product);
		System.out.println("Record inserted");
	}

	public void update() {
		Product product = new Product();
		product.setId(1);
		product.setUpc(1234567899);
		product.setName("Metal");
		product.setQuantity(50);
		product.setUnit_id(1);
		product.setDescription("une excellente qualité de metal!");
		product.setCategory_id(1);
		product.setImg_url("monDossier/image1.png");

		ProductManager.update(product);
		System.out.println("Record updated");
	}

	public void delete() {
		ProductManager.delete(1);
		System.out.println("Record deleted");
	}

	public void getAll() {
		List<Product> products = ProductManager.getAll();

		for (Product product : products) {
			System.out.println("id : " + product.getId());
			System.out.println("upc : " + product.getUpc());
			System.out.println("name : " + product.getName());
			System.out.println("quantity : " + product.getQuantity());
			System.out.println("unit_id : " + product.getUnit_id());
			System.out.println("description : " + product.getDescription());
			System.out.println("category : " + product.getCategory_id());
			System.out.println("img_url : " + product.getImg_url());
			System.out.println("updated_at : " + product.getUpdated_at());
			System.out.println("created_at : " + product.getCreated_at());
			System.out.println("deleted : " + product.getDeleted());
			System.out.println("************************");
		}
	}

	public void getById() {
		Product product = ProductManager.getById(1);

		System.out.println("id : " + product.getId());
		System.out.println("upc : " + product.getUpc());
		System.out.println("name : " + product.getName());
		System.out.println("quantity : " + product.getQuantity());
		System.out.println("unit_id : " + product.getUnit_id());
		System.out.println("description : " + product.getDescription());
		System.out.println("category : " + product.getCategory_id());
		System.out.println("img_url : " + product.getImg_url());
		System.out.println("updated_at : " + product.getUpdated_at());
		System.out.println("created_at : " + product.getCreated_at());
		System.out.println("deleted : " + product.getDeleted());
		System.out.println("************************");
	}

	public static void main(String[] args) {
		ProductManagerTest productManagerTest = new ProductManagerTest();
		productManagerTest.insert();
		productManagerTest.getAll();
		productManagerTest.update();
		productManagerTest.getById();
	//	productManagerTest.delete();

	}
}
