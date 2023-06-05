package de.rieckpil.courses.book.review;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
//@Testcontainers(disabledWithoutDocker = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReviewRepositoryNoInMemoryTest {

  //  @Container
  static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:12.3")
    .withDatabaseName("test")
    .withUsername("duke")
    .withPassword("s3cret")
    .withReuse(true); // reuse the container between tests

  // The lifecycle of container is managed by @Testcontainers and @Container annotations so we don't need to start/stop it manually
  // But we can do it if we want to by uncommenting the static block below (the singleton approach)  .. or by using @BeforeAll/@AfterAll
  static {
    container.start();
  }

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", container::getJdbcUrl);
    registry.add("spring.datasource.password", container::getPassword);
    registry.add("spring.datasource.username", container::getUsername);
  }

//  @BeforeEach
//  void beforeEach() {
//    assertEquals(0, cut.count());
//  }

  @Autowired
  private ReviewRepository cut;

  @Test
  @Sql(scripts = "/scripts/INIT_REVIEW_EACH_BOOK.sql")
  void shouldGetTwoReviewStatisticsWhenDatabaseContainsTwoBooksWithReview() {

    List<ReviewStatistic> result = cut.getReviewStatistics();

    assertEquals(3, cut.count());
    assertEquals(2, result.size());

    result.forEach(reviewStatistic -> {
      System.out.println("Review Statistics");
      System.out.println(reviewStatistic.getId());
      System.out.println(reviewStatistic.getAvg());
      System.out.println(reviewStatistic.getIsbn());
      System.out.println(reviewStatistic.getRatings());
      System.out.println();
    });

    assertEquals(2, result.get(0).getRatings());
    assertEquals(2, result.get(0).getId());
    assertEquals(new BigDecimal("3.00"), result.get(0).getAvg());
  }

  @Test
  void databaseShouldBeEmpty() {
    assertEquals(0, cut.count());
  }

}
