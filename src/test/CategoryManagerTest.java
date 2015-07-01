package test;

import java.util.List;

import com.devsolutions.camelus.entities.Category;
import com.devsolutions.camelus.managers.CategoryManager;

public class CategoryManagerTest {

	public void add() {
		CategoryManager.add("Electronics");
		System.out.println("Record inserted");
	}

	public void getAll() {
		List<Category> categories = CategoryManager.getAll();
		for (Category c : categories) {
			System.out.println(c.getDescription());
		}

	}

	public void getById() {
		System.out.println(CategoryManager.getById(1).getDescription());
	}

	public void delete() {
		CategoryManager.delete(1);
	}

	public static void main(String args[]) {
		CategoryManagerTest categoryManagerTest = new CategoryManagerTest();
		categoryManagerTest.add();
		categoryManagerTest.getById();
		categoryManagerTest.getAll();
		categoryManagerTest.delete();
	}

}
