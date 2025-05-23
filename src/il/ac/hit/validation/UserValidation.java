package il.ac.hit.validation;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Combinator Design Pattern Implementation:
 * This interface allows validation functions on User objects to be composed
 * using logical combinators: and(), or(), xor(), as well as all() and none().
 */
public interface UserValidation extends Function<User, ValidationResult> {

    /**
     * Creates a UserValidation instance from a predicate and a failure reason.
     * This method defers validation execution until apply(user) is called.
     */
    static UserValidation check(Predicate<User> predicate, String reason) {
        return user -> predicate.test(user)? new Valid(): new Invalid(reason);
    }

    /**
     * Logical AND combinator between two validations.
     * Fails early if the first condition is not valid.
     */
    default UserValidation and(UserValidation other) {
        return user -> {
            ValidationResult currValidationResult = this.apply(user);
            if (!currValidationResult.isValid())
            {
                return currValidationResult;
            }

            return other.apply(user);
        };
    }

    /**
     * Logical OR combinator. Returns valid if at least one validation passes.
     */
    default UserValidation or(UserValidation other) {
        return user -> {
            ValidationResult firstValidationResult = this.apply(user);
            ValidationResult secondValidationResult = other.apply(user);
            if (firstValidationResult.isValid() || secondValidationResult.isValid()) return new Valid();
            return new Invalid(firstValidationResult.getReason().orElse("Unknown") +
                    " or " + secondValidationResult.getReason().orElse("Unknown"));
        };
    }

    /**
     * Logical XOR: returns valid if only one of the two validations passes.
     */
    default UserValidation xor(UserValidation other) {
        return user -> {
            boolean firstValidationResult = this.apply(user).isValid();
            boolean secondValidationResult = other.apply(user).isValid();
            return (firstValidationResult ^ secondValidationResult) ? new Valid(): new Invalid("XOR failed: both validations were " + (firstValidationResult ? "true" : "false"));
        };
    }

    /**
     * Returns a validation that passes only if all validations pass.
     */
    static UserValidation all(UserValidation... validations) {
        return user -> {
            for (UserValidation validation : validations) {
                ValidationResult result = validation.apply(user);
                if (!result.isValid())
                {
                    return result;
                }
            }
            return new Valid();
        };
    }

    /**
     * Returns a validation that passes only if none of the validations pass.
     */
    static UserValidation none(UserValidation... validations) {
        return user -> {
            for (UserValidation validation : validations) {
                if (validation.apply(user).isValid())
                {
                    return new Invalid("At least one validation passed, expected none.");
                }
            }
            return new Valid();
        };
    }

    // === Concrete validation rules ===

    static UserValidation emailEndsWithIL() {
        return check(user -> user.getEmail().endsWith(".il"), "Email must end with .il");
    }

    static UserValidation emailLengthBiggerThan10() {
        return check(user -> user.getEmail().length() > 10, "Email must be longer than 10 characters");
    }

    static UserValidation passwordLengthBiggerThan8() {
        return check(user -> user.getPassword().length() > 8, "Password must be longer than 8 characters");
    }

    static UserValidation passwordIncludesLettersNumbersOnly() {
        return check(user -> Pattern.matches("[a-zA-Z0-9]+", user.getPassword()),
                "Password must contain only letters and numbers");
    }

    static UserValidation passwordIncludesDollarSign() {
        return check(user -> user.getPassword().contains("$"), "Password must include $ character");
    }

    static UserValidation passwordIsDifferentFromUsername() {
        return check(user -> !user.getPassword().equals(user.getUsername()),
                "Password must be different from username");
    }

    static UserValidation ageBiggerThan18() {
        return check(user -> user.getAge() > 18, "User must be older than 18");
    }

    static UserValidation usernameLengthBiggerThan8() {
        return check(user -> user.getUsername().length() > 8,
                "Username must be longer than 8 characters");
    }
}
