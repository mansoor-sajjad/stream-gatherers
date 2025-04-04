package org.example;

import java.util.List;
import java.util.Map;
import java.util.stream.Gatherers;

public class BuildInBlogGatherers {

  // After JDK 24 :: Gatherers (java.util.stream.Gatherers) that provide useful intermediate operations

  public static void fixedWindowExample(List<BlogPost> posts) {
    // Group posts into batches of 3
    System.out.println("Posts in batches of 3:");
    posts.stream()
        .limit(9) // Limit to 9 posts for clarity
        .gather(Gatherers.windowFixed(3))
        .forEach(batch -> {
          System.out.println("\nBatch:");
          batch.forEach(post -> System.out.println("  - " + post.title()));
        });
  }

  public static void slidingWindowExample(List<BlogPost> posts) {
    // Show posts in sliding windows of size 2
    System.out.println("Posts in sliding windows of size 2:");
    posts.stream()
        .limit(5) // Limit to 5 posts for clarity
        .gather(Gatherers.windowSliding(2))
        .forEach(window -> {
          System.out.println("\nWindow:");
          window.forEach(post -> System.out.println("  - " + post.title()));
        });
  }

  public static void foldExample(List<BlogPost> posts) {
    // Concatenate all blog post titles
    posts.stream()
        .limit(5) // Limit to 5 posts for clarity
        .gather(Gatherers.fold(
            () -> "All titles: ",
            (result, post) -> result + post.title() + ", "
        ))
        .findFirst()
        .ifPresent(System.out::println);
  }

  public static void foldExampleList(List<BlogPost> posts) {
    // Concatenate all blog post titles
    posts.stream()
        .limit(5) // Limit to 5 posts for clarity
        .gather(Gatherers.fold(
            () -> "All titles: ",
            (result, post) -> result + post.title() + ", "
        ))
        .findFirst()
        .ifPresent(System.out::println);
  }

  public static void scanExample(List<BlogPost> posts) {
    // Build a progressive summary of post titles
    System.out.println("Progressive title concatenation:");
    posts.stream()
        .limit(5) // Limit to 5 posts for clarity
        .gather(Gatherers.scan(
            () -> "Titles so far: ",
            (result, post) -> result + post.title() + ", "
        ))
        .forEach(System.out::println);
  }

  public static void mapConcurrentExample(List<BlogPost> posts) {
    // Process posts concurrently to calculate title lengths
    System.out.println("Title lengths (processed concurrently):");
    posts.stream()
        .limit(10) // Limit to 10 posts for clarity
        .gather(Gatherers.mapConcurrent(
            4, // 4 concurrent operations
            post -> {
              // Simulate some time-consuming processing
              try {
                Thread.sleep(100);
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
              }
              return Map.entry(post.title(), post.title().length());
            }
        ))
        .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue() + " chars"));
  }
}
