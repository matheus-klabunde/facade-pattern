package com.mtk.facadepattern.facade;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.mtk.facadepattern.dto.OrderRequest;
import com.mtk.facadepattern.model.Client;
import com.mtk.facadepattern.model.Order;
import com.mtk.facadepattern.model.Product;
import com.mtk.facadepattern.repository.ClientRepository;
import com.mtk.facadepattern.repository.OrderRepository;
import com.mtk.facadepattern.repository.ProductRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderFacadeTest
{

	@Mock
	private ClientRepository clientRepository;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private OrderFacade orderFacade;

	private Client mockClient;
	private Product mockProduct1;
	private Product mockProduct2;
	private Order mockOrder;
	private OrderRequest orderRequest;

	@BeforeEach
	public void setup()
	{
		orderRequest = new OrderRequest(1L, Arrays.asList(1L, 2L));
		mockClient = new Client(1L, "John Doe", "john.doe@example.com");
		mockProduct1 = new Product(1L, "Product A", 100.0);
		mockProduct2 = new Product(2L, "Product B", 150.0);
		mockOrder = new Order();
		mockOrder.setId(1L);
		mockOrder.setClient(mockClient);
		mockOrder.setProducts(Arrays.asList(mockProduct1, mockProduct2));
		mockOrder.setTotal(250.0);
	}

	@Test
	void givenCorrectRequestWhenCreateOrderThenSuccess() throws Exception
	{
		when(clientRepository.findById(1L)).thenReturn(Optional.of(mockClient));
		when(productRepository.findAllById(Arrays.asList(1L, 2L))).thenReturn(
			Arrays.asList(mockProduct1, mockProduct2));
		when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

		Order createdOrder = orderFacade.createOrder(orderRequest);

		assertNotNull(createdOrder);
		assertEquals(1L, createdOrder.getId());
		assertEquals(mockClient, createdOrder.getClient());
		assertEquals(2, createdOrder.getProducts().size());
		assertEquals(250.0, createdOrder.getTotal());

		verify(clientRepository, times(1)).findById(1L);
		verify(productRepository, times(1)).findAllById(Arrays.asList(1L, 2L));
		verify(orderRepository, times(1)).save(any(Order.class));
	}

	@Test
	void givenInvalidClientIdWhenCreateOrderThenThrowError()
	{
		when(clientRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(Exception.class, () -> {
			orderFacade.createOrder(orderRequest);
		});

		assertEquals("Client not found", exception.getMessage());

		verify(clientRepository, times(1)).findById(1L);
		verify(productRepository, never()).findAllById(anyList());
		verify(orderRepository, never()).save(any(Order.class));
	}

	@Test
	void givenInvalidProductIdsWhenCreateOrderThenThrowError()
	{
		when(clientRepository.findById(1L)).thenReturn(Optional.of(mockClient));
		when(productRepository.findAllById(Arrays.asList(1L, 2L))).thenReturn(List.of());

		Exception exception = assertThrows(Exception.class, () -> {
			orderFacade.createOrder(orderRequest);
		});

		assertEquals("No products found for the provided IDs", exception.getMessage());

		verify(clientRepository, times(1)).findById(1L);
		verify(productRepository, times(1)).findAllById(Arrays.asList(1L, 2L));
		verify(orderRepository, never()).save(any(Order.class));
	}
}
