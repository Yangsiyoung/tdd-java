package refactoring.technique.dto;

public class ChangeFunctionDeclarationCustomer {
    private final String name;
    private final String state;

    public ChangeFunctionDeclarationCustomer(String name, String state) {
        this.name = name;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }
}
