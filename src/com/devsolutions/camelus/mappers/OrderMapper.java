package com.devsolutions.camelus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.devsolutions.camelus.entities.Order;

public interface OrderMapper {

	@Select("SELECT * FROM orders")
	List<Order> getAll();

	@Select("SELECT * FROM orders WHERE vendor_id = #{vendor_id}")
	List<Order> getByVendorId(int vendor_id);

	@Select("SELECT * FROM orders WHERE client_id = #{client_id}")
	List<Order> getByClientId(long client_id);

	@Select("SELECT * FROM orders WHERE id = #{id}")
	Order getById(long id);

	@Insert("INSERT INTO orders (vendor_id,client_id,comment) VALUES (#{vendor_id},#{client_id},#{comment})")
	@Options(flushCache = true)
	void add(Order order);
}
