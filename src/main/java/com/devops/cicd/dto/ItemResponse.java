package com.devops.cicd.dto;

import java.time.Instant;

public class ItemResponse {
  private Long id;
  private String name;
  private Instant createdAt;

  public ItemResponse(Long id, String name, Instant createdAt) {
    this.id = id;
    this.name = name;
    this.createdAt = createdAt;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
