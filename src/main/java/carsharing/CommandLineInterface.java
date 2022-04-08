package carsharing;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

@Component
@AllArgsConstructor
public class CommandLineInterface {

    private final PrintStream out = System.out;
    private final Scanner in = new Scanner(System.in);
    private final CompanyRepository companyRepository;
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;

    public void run() {
        main:
        while (true) {
            printMainMenu();
            int option = in.nextInt();
            out.println();
            switch (option) {
                case 0 -> {
                    break main;
                }
                case 1 -> enterManagerLoop();
                case 2 -> enterCustomersLoop();
                case 3 -> {
                    out.println("Enter the customer name:");
                    in.nextLine();
                    String customerName = in.nextLine();
                    Customer customer = new Customer();
                    customer.setName(customerName);
                    customerRepository.insert(customer);
                    out.println("The customer was added!");
                    out.println();
                }
                default -> throw new RuntimeException("Wrong option: " + option);
            }
        }
    }

    private void printMainMenu() {
        out.println("1. Log in as a manager");
        out.println("2. Log in as a customer");
        out.println("3. Create a customer");
        out.println("0. Exit");
    }

    private void enterManagerLoop() {
        main:
        while (true) {
            printManagerMenu();
            int option = in.nextInt();
            out.println("");
            switch (option) {
                case 0 -> {
                    break main;
                }
                case 1 -> enterCompaniesLoop();
                case 2 -> {
                    out.println("Enter the company name: ");
                    in.nextLine();
                    String companyName = in.nextLine();
                    Company company = new Company();
                    company.setName(companyName);
                    companyRepository.insert(company);
                    out.println("The company was created!");
                    out.println();
                }
                default -> throw new RuntimeException("Wrong option: " + option);
            }
        }
    }

    private void printManagerMenu() {
        out.println("1. Company list");
        out.println("2. Create a company");
        out.println("0. Back");
    }

    private void enterCompaniesLoop() {
        List<Company> companies = getCompanies();
        if (companies.isEmpty()) {
            out.println("The company list is empty!");
            out.println("");
        } else {
            printCompaniesList(companies);
            int option = in.nextInt();
            out.println("");
            if (option > 0) {
                enterCompanyLoop(companies.get(option - 1));
            }
        }
    }

    private List<Company> getCompanies() {
        return companyRepository.getAllCompanies().stream()
                .sorted(Comparator.comparingInt(Company::getId))
                .toList();
    }

    private void printCompaniesList(List<Company> companies) {
        out.println("Choose the company:");
        for (int i = 0; i < companies.size(); i++) {
            Company company = companies.get(i);
            out.printf("%d. %s%n", i + 1, company.getName());
        }
        out.println("0. Back");
    }

    private void enterCompanyLoop(Company company) {
        while (true) {
            printCompanyMenu(company);
            int option = in.nextInt();
            out.println("");
            if (option == 0) {
                break;
            }
            if (option == 1) {
                printCarList(getCompanyCars(company));
                out.println("");
            } else if (option == 2) {
                out.println("Enter the car name:");
                in.nextLine();
                String carName = in.nextLine();
                Car car = new Car();
                car.setName(carName);
                car.setCompanyId(company.getId());
                carRepository.insert(car);
                out.println("The car was added!");
                out.println("");
            }
        }
    }

    private void printCompanyMenu(Company company) {
        out.printf("'%s' company%n", company.getName());
        out.println("1. Car list");
        out.println("2. Create a car");
        out.println("0. Back");
    }

    private void printCarList(List<Car> cars) {
        if (cars.isEmpty()) {
            out.println("The car list is empty!");
        } else {
            out.println("Car list:");
            for (int i = 0; i < cars.size(); i++) {
                Car car = cars.get(i);
                out.printf("%d. %s%n", i + 1, car.getName());
            }
        }
    }

    private List<Car> getCompanyCars(Company company) {
        return carRepository.getAllCarsByCompanyId(company.getId()).stream()
                .sorted(Comparator.comparingInt(Car::getId))
                .toList();
    }

    private List<Car> getFreeCompanyCars(Company company) {
        return carRepository.getAllFreeCarsByCompanyId(company.getId()).stream()
                .sorted(Comparator.comparingInt(Car::getId))
                .toList();
    }

    private void enterCustomersLoop() {
        List<Customer> customers = customerRepository.getAllCustomers().stream()
                .sorted(Comparator.comparingInt(Customer::getId))
                .toList();
        if (customers.isEmpty()) {
            out.println("The customer list is empty!");
            out.println("");
        } else {
            printCustomersMenu(customers);
            int option = in.nextInt();
            out.println("");
            if (option > 0) {
                enterCustomerLoop(customers.get(option - 1));
            }
        }
    }

    private void printCustomersMenu(List<Customer> customers) {
        out.println("Customer list:");
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            out.printf("%d. %s%n", i + 1, customer.getName());
        }
        out.println("0. Back");
    }

    private void enterCustomerLoop(Customer customer) {
        menu:
        while (true) {
            printCustomerMenu();
            int option = in.nextInt();
            out.println("");
            switch (option) {
                case 0 -> {
                    break menu;
                }
                case 1 -> {
                    if (customer.getRentedCarId() != null) {
                        out.println("You've already rented a car!");
                        out.println("");
                        break;
                    }
                    List<Company> companies = getCompanies();
                    if (companies.isEmpty()) {
                        out.println("The company list is empty!");
                        out.println("");
                    } else {
                        printCompaniesList(companies);
                        option = in.nextInt();
                        out.println("");
                        if (option > 0) {
                            Company company = companies.get(option - 1);
                            List<Car> cars = getFreeCompanyCars(company);
                            if (cars.isEmpty()) {
                                out.println("The cars list is empty!");
                                out.println("");
                            } else {
                                printCarList(cars);
                                option = in.nextInt();
                                out.println("");
                                if (option > 0) {
                                    Car car = cars.get(option - 1);
                                    customer.setRentedCarId(car.getId());
                                    boolean updated = customerRepository.update(customer);
                                    Preconditions.checkState(updated, "Customer is not updated");
                                    out.printf("You rented '%s'%n", car.getName());
                                    out.println("");
                                }
                            }
                        }
                    }
                }
                case 2 -> {
                    if (customer.getRentedCarId() == null) {
                        out.println("You didn't rent a car!");
                    } else {
                        customer.setRentedCarId(null);
                        boolean updated = customerRepository.update(customer);
                        Preconditions.checkState(updated, "Customer is not updated");
                        out.println("You've returned a rented car!");
                    }
                    out.println("");
                }
                case 3 -> {
                    if (customer.getRentedCarId() == null) {
                        out.println("You didn't rent a car!");
                    } else {
                        Car car = carRepository.findCarById(customer.getRentedCarId())
                                .orElseThrow();
                        Company company = companyRepository.findCompanyById(car.getCompanyId())
                                .orElseThrow();
                        printRentedCarInfo(company, car);
                    }
                    out.println("");
                }
            }
        }
    }

    private void printCustomerMenu() {
        out.println("1. Rent a car");
        out.println("2. Return a rented car");
        out.println("3. My rented car");
        out.println("0. Back");
    }

    private void printRentedCarInfo(Company company, Car car) {
        out.println("Your rented car:");
        out.println(car.getName());
        out.println("Company:");
        out.println(company.getName());
    }
}
