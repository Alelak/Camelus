package com.devsolutions.camelus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.devsolutions.camelus.entities.Category;

public interface CategoryMapper {

	@Select("SELECT * FROM categories")
	List<Category> getAll();

	@Select("SELECT * FROM categories WHERE id = #{id}")
	Category getById(int id);

	@Insert("INSERT INTO categories (description) VALUE (#{description})")
	@Options(flushCache = true)
	void add(String description);
}
