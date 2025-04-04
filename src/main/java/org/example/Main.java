package org.example;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

  public static void main(String[] args) {
    List<BlogPost> posts = BlogPostService.createSampleBlogPosts();

    System.out.println("============ Find all posy by category =============");
    postsByCategory(posts, "Java");

    System.out.println("============ Before JDK 24: Group By Category =============");
    System.out.println("============ Example Method 1: Nested Collections =============");
    nestedCollectors(posts);
    System.out.println("============ Example Method 2: Map then Transform =============");
    mapThenTransform(posts);


  }

  // Prior to JDK 24 :: How to Group By Category, order by publishedDate and limit to 3 most recent posts

  public static void nestedCollectors(List<BlogPost> posts) {
    Map<String, List<BlogPost>> recentPostsByCategory = posts.stream()
        // First, group all posts by category
        .collect(Collectors.groupingBy(
            BlogPost::category,
            Collectors.collectingAndThen(
                // Collect posts into a list
                Collectors.toList(),
                // Then transform each list by sorting and limiting
                categoryPosts -> categoryPosts.stream()
                    .sorted(Comparator.comparing(BlogPost::publishedDate).reversed())
                    .limit(3)
                    .toList()
            )
        ));

    printRecentPostsByCategory(recentPostsByCategory);
  }

  // Prior to JDK 24 :: How to Group By Category, order by publishedDate and limit to 3 most recent posts

  public static void mapThenTransform(List<BlogPost> posts) {
    Map<String, List<BlogPost>> recentPostsByCategory = posts.stream()
        // Group by category
        .collect(Collectors.groupingBy(BlogPost::category))
        // Convert to a stream of map entries
        .entrySet().stream()
        // Convert each entry to a new entry with sorted and limited values
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> entry.getValue().stream()
                .sorted(Comparator.comparing(BlogPost::publishedDate).reversed())
                .limit(3)
                .collect(Collectors.toList())
        ));

    printRecentPostsByCategory(recentPostsByCategory);
  }

  // Prior to JDK 24 :: Find All Posts By Category
  public static void postsByCategory(List<BlogPost> posts,String category) {
    List<BlogPost> postsByCategory = posts.stream()
        .filter(p -> p.category().equals(category))
        .sorted(Comparator.comparing(BlogPost::publishedDate).reversed())
        .limit(3)
        .toList();

    System.out.println("\nPosts by Category: " + category);
    postsByCategory.forEach(System.out::println);
  }

  private static void printRecentPostsByCategory(Map<String, List<BlogPost>> recentPostsByCategory){
    System.out.println("Recent Posts By Category:");
    recentPostsByCategory.forEach((category, categoryPosts) -> {
      System.out.println("\nCategory: " + category);
      categoryPosts.forEach(post -> System.out.println("  - " + post.title() + " (Published: " + post.publishedDate() + ")"));
    });
  }
}