package com.devsolutions.camelus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.devsolutions.camelus.entities.Product;

public interface ProductMapper {
	@Select("SELECT * FROM products")
	List<Product> getAll();
	
	@Select("SELECT * FROM products WHERE id = #{id} ")
	Product getById(int id);
	
	@Insert("INSERT INTO products (upc, name, quantity, unit_id, description, id_category, img_url)"
			+ " VALUES (#{upc}, #{name}, #{quantity}, #{unit_id}, #{description}, #{id_category}, #{img_url})")
	@Options(flushCache = true)
	void add(Product product);
	
	@Update("UPDATE products SET upc = #{upc}, name = #{name}, quantity = #{quantity}, unit_id = #{unit_id}, description = #{description}, id_category = #{id_category}, img_url = #{img_url}, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
	@Options(flushCache = true)
	void update(Product product);
	
	@Update("UPDATE  products SET deleted = 1 WHERE id = #{id}")
	@Options(flushCache = true)
	void delete(int id);
}
