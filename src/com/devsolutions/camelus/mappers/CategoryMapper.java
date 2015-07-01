package com.devsolutions.camelus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.devsolutions.camelus.entities.Category;

public interface CategoryMapper {

	@Select("SELECT * FROM categories WHERE deleted = 0")
	List<Category> getAll();

	@Select("SELECT * FROM categories WHERE id = #{id} AND  deleted = 0")
	Category getById(int id);

	@Insert("INSERT INTO categories (description) VALUE (#{description})")
	@Options(flushCache = true)
	void add(String description);

	@Update("UPDATE categories SET deleted = 1 WHERE id = #{id}")
	@Options(flushCache = true)
	void delete(int id);
}
