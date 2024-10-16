package com.esPublico.app.domain.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.esPublico.app.domain.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

	List<Order> findAllByOrderByIdAsc();

}
