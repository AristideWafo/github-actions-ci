package com.devops.cicd.service;

import com.devops.cicd.domain.Item;
import com.devops.cicd.dto.ItemResponse;
import com.devops.cicd.repository.ItemRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemService {
  private final ItemRepository itemRepository;

  public ItemService(ItemRepository itemRepository) {
    this.itemRepository = itemRepository;
  }

  public ItemResponse create(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Le nom ne peut pas être vide");
    }

    Item item = new Item(name.trim());
    item = itemRepository.save(item);
    return new ItemResponse(item.getId(), item.getName(), item.getCreatedAt());
  }

  @Transactional(readOnly = true)
  public List<ItemResponse> findAll() {
    return itemRepository.findAllByOrderByCreatedAtDesc().stream()
        .map(item -> new ItemResponse(item.getId(), item.getName(), item.getCreatedAt()))
        .collect(Collectors.toList());
  }
}
