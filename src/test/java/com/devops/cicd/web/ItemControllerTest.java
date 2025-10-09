package com.devops.cicd.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devops.cicd.dto.ItemCreateRequest;
import com.devops.cicd.dto.ItemResponse;
import com.devops.cicd.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private ItemService itemService;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void createItem_WithValidRequest_ShouldReturnCreated() throws Exception {
    // Given
    ItemCreateRequest request = new ItemCreateRequest();
    request.setName("Test Item");

    ItemResponse response = new ItemResponse(1L, "Test Item", Instant.now());
    when(itemService.create("Test Item")).thenReturn(response);

    // When/Then
    mockMvc
        .perform(
            post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(header().string("Location", "http://localhost/items/1"))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Test Item"))
        .andExpect(jsonPath("$.createdAt").exists());
  }

  @Test
  void createItem_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
    // Given
    ItemCreateRequest request = new ItemCreateRequest();
    request.setName("");

    // When/Then
    mockMvc
        .perform(
            post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createItem_WithNullName_ShouldReturnBadRequest() throws Exception {
    // Given
    ItemCreateRequest request = new ItemCreateRequest();
    request.setName(null);

    // When/Then
    mockMvc
        .perform(
            post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createItem_WithNameTooLong_ShouldReturnBadRequest() throws Exception {
    // Given
    ItemCreateRequest request = new ItemCreateRequest();
    request.setName("x".repeat(101));

    // When/Then
    mockMvc
        .perform(
            post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getAllItems_ShouldReturnOkWithItemsList() throws Exception {
    // Given
    List<ItemResponse> items =
        Arrays.asList(
            new ItemResponse(1L, "Item 1", Instant.now()),
            new ItemResponse(2L, "Item 2", Instant.now()));
    when(itemService.findAll()).thenReturn(items);

    // When/Then
    mockMvc
        .perform(get("/items"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].name").value("Item 1"))
        .andExpect(jsonPath("$[1].id").value(2))
        .andExpect(jsonPath("$[1].name").value("Item 2"));
  }

  @Test
  void getAllItems_WhenEmpty_ShouldReturnEmptyList() throws Exception {
    // Given
    when(itemService.findAll()).thenReturn(Arrays.asList());

    // When/Then
    mockMvc
        .perform(get("/items"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(0));
  }
}
