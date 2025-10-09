package com.devops.cicd.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.devops.cicd.domain.Item;
import com.devops.cicd.dto.ItemResponse;
import com.devops.cicd.repository.ItemRepository;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

  @Mock private ItemRepository itemRepository;

  private ItemService itemService;

  @BeforeEach
  void setUp() {
    itemService = new ItemService(itemRepository);
  }

  @Test
  void create_WithValidName_ShouldReturnItemResponse() {
    // Given
    String name = "Test Item";
    Item savedItem = new Item(name);
    savedItem.setId(1L);
    savedItem.setCreatedAt(Instant.now());
    when(itemRepository.save(any(Item.class))).thenReturn(savedItem);

    // When
    ItemResponse response = itemService.create(name);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(1L);
    assertThat(response.getName()).isEqualTo(name);
    assertThat(response.getCreatedAt()).isNotNull();
    verify(itemRepository).save(any(Item.class));
  }

  @Test
  void create_WithBlankName_ShouldThrowException() {
    // Given
    String name = "   ";

    // When/Then
    assertThrows(IllegalArgumentException.class, () -> itemService.create(name));
    verify(itemRepository, never()).save(any());
  }

  @Test
  void findAll_ShouldReturnSortedList() {
    // Given
    Instant now = Instant.now();
    Instant earlier = now.minusSeconds(3600);

    Item item1 = new Item("Item 1");
    item1.setId(1L);
    item1.setCreatedAt(earlier);

    Item item2 = new Item("Item 2");
    item2.setId(2L);
    item2.setCreatedAt(now);

    when(itemRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(item2, item1));

    // When
    List<ItemResponse> items = itemService.findAll();

    // Then
    assertThat(items).hasSize(2);
    assertThat(items.get(0).getId()).isEqualTo(2L);
    assertThat(items.get(0).getName()).isEqualTo("Item 2");
    assertThat(items.get(0).getCreatedAt()).isEqualTo(now);

    assertThat(items.get(1).getId()).isEqualTo(1L);
    assertThat(items.get(1).getName()).isEqualTo("Item 1");
    assertThat(items.get(1).getCreatedAt()).isEqualTo(earlier);

    verify(itemRepository).findAllByOrderByCreatedAtDesc();
  }
}
