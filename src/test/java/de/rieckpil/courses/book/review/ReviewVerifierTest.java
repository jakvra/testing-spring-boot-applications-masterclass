package de.rieckpil.courses.book.review;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

import static de.rieckpil.courses.book.review.RandomReviewParameterResolverExtension.RandomReview;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(RandomReviewParameterResolverExtension.class)
class ReviewVerifierTest {

  private ReviewVerifier reviewVerifier;

  @BeforeEach
  void setup() {
    reviewVerifier = new ReviewVerifier();
  }

  @Test
  void shouldFailWhenReviewContainsSwearWord() {
    String review = "This book is shit";
    System.out.println("Testing a review");

    boolean result = reviewVerifier.doesMeetQualityStandards(review);
    assertFalse(result, "ReviewVerifier did not detect swear word");
  }

  @Test
  @DisplayName("Should fail when review contains 'lorem ipsum'")
  void testLoremIpsum() {
    String review = "Lorem ipsum is simply dummy text of the printing and typesetting industry.";

    boolean result = reviewVerifier.doesMeetQualityStandards(review);
    assertFalse(result, "ReviewVerifier did not detect lorem ipsum");

  }

  @ParameterizedTest
  @CsvFileSource(resources = "/badReview.csv")
  void shouldFailWhenReviewIsOfBadQuality(String review) throws InterruptedException {
    boolean result = reviewVerifier.doesMeetQualityStandards(review);
    assertFalse(result, "ReviewVerifier did not detect bad review");
  }

  @RepeatedTest(5)
  void shouldFailWhenRandomReviewQualityIsBad(@RandomReview String review) {
    boolean result = reviewVerifier.doesMeetQualityStandards(review);
    assertFalse(result, "ReviewVerifier did not detect random bad review");
  }

  @Test
  void shouldPassWhenReviewIsGood() {
    String review = "I can totally recommend this book who is interested in learning how to write Java code";

    boolean result = reviewVerifier.doesMeetQualityStandards(review);
    assertTrue(result, "ReviewVerifier did pass a good review");
  }

  @Test
  void shouldPassWhenReviewIsGoodHamcrest() {
    String review = "I can totally recommend this book who is interested in learning how to write Java code";

    boolean result = reviewVerifier.doesMeetQualityStandards(review);
//    assertTrue(result, "ReviewVerifier did pass a good review");  // JUnit 5
    MatcherAssert.assertThat("ReviewVerifier did pass a good review", result, Matchers.equalTo(true));
    MatcherAssert.assertThat("Lorem Ipsum", Matchers.endsWith("Ipsum"));
    MatcherAssert.assertThat(List.of(1, 2, 3, 4, 5), Matchers.hasSize(5));
    MatcherAssert.assertThat(List.of(1, 2, 3, 4, 5), Matchers.anyOf(Matchers.hasSize(5), Matchers.emptyIterable()));
//    MatcherAssert.assertThat(List.of(1, 2, 3, 4, 5), Matchers.allOf(Matchers.hasSize(5), Matchers.emptyIterable()));
  }

  @Test
  void shouldPassWhenReviewIsGoodAssertJ() {
    String review = "I can totally recommend this book who is interested in learning how to write Java code";

    boolean result = reviewVerifier.doesMeetQualityStandards(review);
//    assertTrue(result, "ReviewVerifier did pass a good review");  // JUnit 5
    org.assertj.core.api.Assertions.assertThat(result)
      .withFailMessage("ReviewVerifier did not pass a good review")
      .isEqualTo(true)
      .isTrue();

    org.assertj.core.api.Assertions.assertThat(List.of(1, 2, 3, 4, 5)).hasSizeBetween(1, 10);
    org.assertj.core.api.Assertions.assertThat(List.of(1, 2, 3, 4, 5)).contains(3).isNotEmpty();
  }
}
