package com.devops.cicd;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(
    properties = {
      "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
      "spring.datasource.username=sa",
      "spring.datasource.password=",
      "spring.datasource.driver-class-name=org.h2.Driver",
      "spring.jpa.hibernate.ddl-auto=create-drop",
      "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
    })
class CicdApplicationMainTest {

  @Test
  void main_ShouldStartApplication() {
    try (MockedStatic<SpringApplication> springApp = mockStatic(SpringApplication.class)) {
      // Given
      String[] args = new String[] {"--server.port=0"};

      // When
      CicdApplication.main(args);

      // Then
      springApp.verify(() -> SpringApplication.run(eq(CicdApplication.class), eq(args)));
    }
  }
}
