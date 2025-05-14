package com.example.tomatomall.repository;

import com.example.tomatomall.po.CartsOrdersRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartsOrdersRelationRepository extends JpaRepository<CartsOrdersRelation, Integer> {
    List<CartsOrdersRelation> findByOrderId(Integer orderId);
    List<CartsOrdersRelation> findByCartitemId(Integer cartItemId);
}