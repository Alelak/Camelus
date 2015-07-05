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
	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = true)
	void add(Category category);

	@Update("UPDATE categories SET deleted = 1, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
	@Options(flushCache = true)
	void delete(int id);

	@Update("UPDATE categories SET description = #{description}, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
	@Options(flushCache = true)
	void update(Category category);
}
