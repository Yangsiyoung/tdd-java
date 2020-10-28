package refactoring.technique;

import refactoring.technique.dto.ChangeFunctionDeclarationCustomer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChangeFunctionDeclaration {

    private List<String> reservations = new ArrayList<>();
    private List<ChangeFunctionDeclarationCustomer> customers = new ArrayList<>();
    private List<ChangeFunctionDeclarationCustomer> newEnglanders =  customers.stream().filter((customer) -> isNewEngland(customer.getState())).collect(Collectors.toList());

    public double circum(int radius) {
        return circumference(radius);
    }

    public double circumference(int radius) {
        return 2 * Math.PI * radius;
    }

    public void addReservation(String customer) {
        addReservationWithPriority(customer, false);
    }

    public void addReservationWithPriority(String customer, boolean priority) {
        reservations.add(customer);
    }

    public boolean isNewEngland(String stateCode) {
        return Stream.builder().add("MA").add("CT").add("ME").add("VT").add("NH").add("RI").build().anyMatch((state) -> state.equals(stateCode));
    }
}
