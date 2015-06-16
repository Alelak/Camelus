package com.devsolutions.camelus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.devsolutions.camelus.entities.OrderLine;

public interface OrderLineMapper {

	@Select("SELECT * FROM order_lines WHERE order_id = #{order_id}")
	List<OrderLine> getByOrderId(long order_id);

	@Select("SELECT * FROM order_lines WHERE product_id = #{product_id}")
	List<OrderLine> getByProductId(long product_id);

	@Insert("INSERT INTO order_lines (product_id,order_id,price,modified_price,quantity) VALUES (#{product_id},#{order_id},#{price},#{modified_price},#{quantity})")
	@Options(flushCache = true)
	void add(OrderLine orderLine);

}