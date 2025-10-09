package com.devops.cicd.validation;

import static org.assertj.core.api.Assertions.assertThat;

import com.devops.cicd.dto.ItemCreateRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemCreateRequestValidationTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void whenNameIsNull_thenValidationFails() {
    ItemCreateRequest request = new ItemCreateRequest();
    request.setName(null);

    var violations = validator.validate(request);
    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("name");
  }

  @Test
  void whenNameIsEmpty_thenValidationFails() {
    ItemCreateRequest request = new ItemCreateRequest();
    request.setName("");

    var violations = validator.validate(request);
    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("name");
  }

  @Test
  void whenNameIsTooLong_thenValidationFails() {
    ItemCreateRequest request = new ItemCreateRequest();
    request.setName("x".repeat(101));

    var violations = validator.validate(request);
    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("name");
  }

  @Test
  void whenNameIsValid_thenValidationPasses() {
    ItemCreateRequest request = new ItemCreateRequest();
    request.setName("Ok");

    var violations = validator.validate(request);
    assertThat(violations).isEmpty();
  }
}
