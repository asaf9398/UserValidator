package il.ac.hit.validation;

import java.util.Arrays;
import java.util.Comparator;

public class UserUtils {
    // This method is an example of the Template Method pattern
    // as it provides a fixed algorithm (sorting) while delegating the comparison logic.
    public static void sort(User[] users, Comparator<User> comparator) {
        Arrays.sort(users, comparator);
    }
}
