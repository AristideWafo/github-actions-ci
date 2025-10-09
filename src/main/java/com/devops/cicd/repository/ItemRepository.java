package com.devops.cicd.repository;

import com.devops.cicd.domain.Item;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
  List<Item> findAllByOrderByCreatedAtDesc();
}
