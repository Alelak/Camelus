package com.devsolutions.camelus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.devsolutions.camelus.entities.Product;
import com.devsolutions.camelus.entities.ProductTV;

public interface ProductMapper {
	public static final String DELETED_CONDITION = "deleted = 0";

	@Select("SELECT * FROM products WHERE " + DELETED_CONDITION)
	List<Product> getAll();

	@Select("SELECT * FROM products WHERE category_id = #{category_id} AND "
			+ DELETED_CONDITION)
	List<Product> getByCategory(int category_id);

	@Select("SELECT * FROM products WHERE unit_id = #{unit_id} AND "
			+ DELETED_CONDITION)
	List<Product> getByUnit(int unit_id);

	@Select("SELECT products.id,products.upc,products.name,products.quantity,products.selling_price,categories.description FROM products inner join categories ON products.category_id=categories.id WHERE products."
			+ DELETED_CONDITION)
	List<ProductTV> getAllProductTableView();

	@Select("SELECT * FROM products WHERE id = #{id} ")
	Product getById(long id);

	@Insert("INSERT INTO products (upc, name, quantity, unit_id, cost_price, selling_price, description, category_id, img)"
			+ " VALUES (#{upc}, #{name}, #{quantity}, #{unit_id},#{cost_price},#{selling_price}, #{description}, #{category_id}, #{img})")
	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = true)
	void add(Product product);

	@Update("UPDATE products SET upc = #{upc}, name = #{name}, quantity = #{quantity}, unit_id = #{unit_id}, cost_price = #{cost_price}, selling_price = #{selling_price}, description = #{description}, category_id = #{category_id}, img = #{img}, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
	@Options(flushCache = true)
	void update(Product product);

	@Update("UPDATE  products SET deleted = 1 WHERE id = #{id}")
	@Options(flushCache = true)
	void delete(long id);

	@Update("UPDATE products SET quantity = quantity - #{quantity} WHERE id = #{id}")
	void decrementQuantity(@Param("quantity") int quantity, @Param("id") long id);

	@Update("UPDATE products SET quantity = quantity + #{quantity} WHERE id = #{id}")
	void incrementQuantity(@Param("quantity") int quantity, @Param("id") long id);
}
