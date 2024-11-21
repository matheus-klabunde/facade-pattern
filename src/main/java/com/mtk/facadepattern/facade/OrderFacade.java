package com.mtk.facadepattern.facade;

import com.mtk.facadepattern.dto.OrderRequest;
import com.mtk.facadepattern.model.Client;
import com.mtk.facadepattern.model.Order;
import com.mtk.facadepattern.model.Product;
import com.mtk.facadepattern.repository.ClientRepository;
import com.mtk.facadepattern.repository.OrderRepository;
import com.mtk.facadepattern.repository.ProductRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderFacade
{
	private final ClientRepository clientRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;

	@Autowired
	public OrderFacade(ClientRepository clientRepository, ProductRepository productRepository,
		OrderRepository orderRepository)
	{
		this.clientRepository = clientRepository;
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
	}

	@Transactional
	public Order createOrder(OrderRequest request) throws Exception
	{
		Client client = clientRepository.findById(request.clientId())
			.orElseThrow(() -> new Exception("Client not found"));

		List<Product> products = productRepository.findAllById(request.productIds());
		if (products.isEmpty())
		{
			throw new Exception("No products found for the provided IDs");
		}

		Double total = products.stream().mapToDouble(Product::getPrice).sum();

		Order order = new Order();
		order.setClient(client);
		order.setProducts(products);
		order.setTotal(total);

		return orderRepository.save(order);
	}
}
