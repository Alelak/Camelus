package com.devsolutions.camelus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.devsolutions.camelus.entities.Unit;

public interface UnitMapper {

	@Select("SELECT * FROM units WHERE deleted = 0")
	List<Unit> getAll();

	@Select("SELECT * FROM units WHERE id = #{id} AND deleted = 0")
	Unit getById(int id);

	@Insert("INSERT INTO units (description) VALUE (#{description})")
	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = true)
	void add(Unit unit);

	@Update("UPDATE units SET deleted = 1, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
	@Options(flushCache = true)
	void delete(int id);

	@Update("UPDATE units SET description = #{description}, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
	@Options(flushCache = true)
	void update(Unit unit);
}
