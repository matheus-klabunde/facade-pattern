package com.mtk.facadepattern.repository;

import com.mtk.facadepattern.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>
{
}
