package com.devops.cicd.web;

import com.devops.cicd.dto.ItemCreateRequest;
import com.devops.cicd.dto.ItemResponse;
import com.devops.cicd.service.ItemService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/items")
public class ItemController {
  private final ItemService itemService;

  public ItemController(ItemService itemService) {
    this.itemService = itemService;
  }

  @PostMapping
  public ResponseEntity<ItemResponse> createItem(@Valid @RequestBody ItemCreateRequest request) {
    ItemResponse response = itemService.create(request.getName());
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.getId())
            .toUri();
    return ResponseEntity.created(location).body(response);
  }

  @GetMapping
  public ResponseEntity<List<ItemResponse>> getAllItems() {
    return ResponseEntity.ok(itemService.findAll());
  }
}
