package com.devsolutions.camelus.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.devsolutions.camelus.entities.OrderLine;
import com.devsolutions.camelus.entities.OrderLineTV;

public interface OrderLineMapper {
	@Select("SELECT upc, name, selling_price, modified_price, order_lines.quantity"
			+ " FROM order_lines INNER JOIN products ON order_lines.product_id = products.id"
			+ " WHERE order_id = #{order_id}")
	List<OrderLineTV> getByOrderId(long order_id);

	@Select("SELECT upc, name, selling_price, modified_price, order_lines.quantity"
			+ " FROM order_lines INNER JOIN products ON order_lines.product_id = products.id"
			+ " INNER JOIN orders ON orders.id = order_id")
	List<OrderLineTV> getAll();

	@Select("SELECT * FROM order_lines WHERE product_id = #{product_id}")
	List<OrderLine> getByProductId(long product_id);

	@Insert("INSERT INTO order_lines (product_id,order_id,modified_price,quantity) VALUES (#{product_id},#{order_id},#{modified_price},#{quantity})")
	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = true)
	void add(OrderLine orderLine);

}
