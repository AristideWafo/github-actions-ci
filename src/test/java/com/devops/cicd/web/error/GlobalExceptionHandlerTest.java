package com.devops.cicd.web.error;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devops.cicd.dto.ItemCreateRequest;
import com.devops.cicd.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class GlobalExceptionHandlerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private ItemService itemService;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void handleValidationExceptions_ShouldReturn400() throws Exception {
    // Given
    ItemCreateRequest request = new ItemCreateRequest();
    request.setName(""); // Invalid - blank name

    // When/Then
    mockMvc
        .perform(
            post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Validation Error"))
        .andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.path").value("/items"));
  }

  @Test
  void handleValidationExceptions_WithNullName_ShouldReturn400() throws Exception {
    // Given
    ItemCreateRequest request = new ItemCreateRequest();
    request.setName(null); // Invalid - null name

    // When/Then
    mockMvc
        .perform(
            post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Validation Error"))
        .andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.path").value("/items"));
  }

  @Test
  void handleValidationExceptions_WithTooLongName_ShouldReturn400() throws Exception {
    // Given
    ItemCreateRequest request = new ItemCreateRequest();
    request.setName("x".repeat(101)); // Invalid - too long

    // When/Then
    mockMvc
        .perform(
            post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Validation Error"))
        .andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.path").value("/items"));
  }

  @Test
  void handleIllegalArgument_ShouldReturn400() throws Exception {
    // Given
    ItemCreateRequest request = new ItemCreateRequest();
    request.setName("Valid Name");
    when(itemService.create(anyString()))
        .thenThrow(new IllegalArgumentException("Name cannot be blank after trimming"));

    // When/Then
    mockMvc
        .perform(
            post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Name cannot be blank after trimming"))
        .andExpect(jsonPath("$.path").value("/items"));
  }

  @Test
  void handleAllUncaughtException_ShouldReturn500() throws Exception {
    // Given
    ItemCreateRequest request = new ItemCreateRequest();
    request.setName("Valid Name");
    when(itemService.create(anyString())).thenThrow(new RuntimeException("Unexpected error"));

    // When/Then
    mockMvc
        .perform(
            post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.status").value(500))
        .andExpect(jsonPath("$.error").value("Internal Server Error"))
        .andExpect(jsonPath("$.message").value("Une erreur inattendue s'est produite"))
        .andExpect(jsonPath("$.path").value("/items"));
  }
}
