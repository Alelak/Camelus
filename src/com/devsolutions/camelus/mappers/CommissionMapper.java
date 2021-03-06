package com.devsolutions.camelus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.devsolutions.camelus.entities.Commission;

public interface CommissionMapper {

	@Insert("INSERT INTO commissions (type,rate,mcondition) VALUES (#{type},#{rate},#{mcondition})")
	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = true)
	void add(Commission commission);

	@Select("SELECT * FROM commissions WHERE deleted = 0")
	List<Commission> getAll();

	@Select("SELECT * FROM commissions WHERE id = #{id} AND deleted = 0")
	Commission getById(int id);

	@Update("UPDATE commissions SET deleted = 1, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
	@Options(flushCache = true)
	void delete(int id);

	@Update("UPDATE commissions SET type = #{type} , rate = #{rate} , mcondition = #{mcondition}, updated_at = CURRENT_TIMESTAMP   WHERE id = #{id}")
	@Options(flushCache = true)
	void update(Commission commission);
}
