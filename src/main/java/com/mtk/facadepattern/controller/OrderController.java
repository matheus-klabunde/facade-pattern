package com.mtk.facadepattern.controller;

import com.mtk.facadepattern.dto.OrderRequest;
import com.mtk.facadepattern.facade.OrderFacade;
import com.mtk.facadepattern.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController
{
	private final OrderFacade orderFacade;

	@Autowired
	public OrderController(OrderFacade orderFacade)
	{
		this.orderFacade = orderFacade;
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody OrderRequest request)
	{
		try
		{
			Order order = orderFacade.createOrder(request);
			return ResponseEntity.ok(order);
		}
		catch (Exception e)
		{
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
