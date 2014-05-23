package example.akka.model;

public class RegisterData {
    private final int id;

    private final String name;

    private final String street;

    public RegisterData(int id, String name, String street) {
        this.id = id;
        this.name = name;
        this.street = street;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStreet() {
        return street;
    }

    public static RegisterData of(int id, String name, String street) {
        return new RegisterData(id, name, street);
    }
}
