package il.ac.hit.validation;

public class User {
    protected String username;
    protected String email;
    protected String password;
    protected int age;

    public User(String username, String email, String password, int age) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    // Getters
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public int getAge() { return age; }
}
