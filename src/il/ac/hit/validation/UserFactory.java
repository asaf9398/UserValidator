package il.ac.hit.validation;

public class UserFactory {
    public static User create(String type, String username, String email, String password, int age) {
        return switch (type.toLowerCase()) {
            case "basic" -> new BasicUser(username, email, password, age);
            case "premium" -> new PremiumUser(username, email, password, age);
            case "platinum" -> new PlatinumUser(username, email, password, age);
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
}
