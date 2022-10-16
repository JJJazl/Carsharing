package com.company.ui;

import com.company.domain.*;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MenuImp implements Menu {

    private final Scanner scanner = new Scanner(System.in);
    private final ActionManager actionManager;

    public MenuImp(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    @Override
    public void mainMenu() {
        while (true) {
            System.out.println("\n1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
            System.out.print("0. Exit\n> ");
            try {
                int mainAction = scanner.nextInt();
                switch (mainAction) {
                    case 0:
                        System.exit(0);
                        break;
                    case 1:
                        managerMenu();
                        break;
                    case 2:
                        List<Customer> customers = actionManager.getAllCustomers();

                        if (customers.isEmpty()) {
                            System.out.println("\nThe customer list is empty!");
                        } else {
                            System.out.println("\nCustomer list:");
                            customers.forEach(System.out::println);
                            System.out.print("0. Back\n> ");
                            int customerId = scanner.nextInt();
                            if (customerId > customers.size() || customerId < 0) {
                                throw new IllegalArgumentException("\nNon-existent customer id!");
                            }
                            if (customerId != 0) {
                                customerMenu(customerId);
                            }
                        }
                        break;
                    case 3:
                        System.out.print("\nEnter the customer name:\n> ");
                        scanner.nextLine();
                        String name = scanner.nextLine();
                        int success = actionManager.createCustomer(name);
                        System.out.println(success != -1 ? "The customer was added!" : "Customer adding error!");
                        break;
                    default:
                        System.out.println("\nUnknown command");
                }
            } catch (InputMismatchException e ) {
                System.out.println("\nInput error! Enter the number!");
                scanner.next();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void managerMenu() {
        while (true) {
            System.out.println("\n1. Company list");
            System.out.println("2. Create a company");
            System.out.print("0. Back\n> ");
            try {
                int managerAction = scanner.nextInt();
                switch (managerAction) {
                    case 0:
                        return;
                    case 1:
                        List<Company> companies = actionManager.getAllCompanies();

                        if (companies.isEmpty()) {
                            System.out.println("\nThe company list is empty!");
                        } else {
                            System.out.println("\nChoose the company:");
                            companies.forEach(System.out::println);
                            System.out.print("0. Back\n> ");

                            int companyId = scanner.nextInt();
                            if (companyId > companies.size() || companyId < 0) {
                                throw new IllegalArgumentException("\nNon-existent company id!");
                            }
                            if (companyId != 0) {
                                carMenu(companyId);
                            }
                        }
                        break;
                    case 2:
                        System.out.println("\nEnter the company name:");
                        scanner.nextLine();
                        String name = scanner.nextLine();

                        int success = actionManager.createCompany(name);
                        System.out.println(success != -1 ? "The company was created!" : "Company adding error!");
                        break;
                    default:
                        System.out.println("\nUnknown command");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nInput error! Enter the number");
                scanner.next();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void customerMenu(int customerId) {
        Customer currentCustomer = actionManager.getCustomerById(customerId);
        while (true) {
            System.out.println("1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.print("0. Back\n> ");
            try {
                int customerAction = scanner.nextInt();
                switch (customerAction) {
                    case 0:
                        return;
                    case 1:
                        if (currentCustomer.getRentedCarId() != 0) {
                            System.out.println("\nYou've already rented a car!");
                            break;
                        }
                        List<Company> companies = actionManager.getAllCompanies();
                        while (true) {
                            System.out.println("\nChoose a company:");
                            companies.forEach(System.out::println);
                            System.out.print("0. Back\n> ");
                            try {
                                int companyId = scanner.nextInt();
                                if (companyId > companies.size() || companyId < 0) {
                                    throw new IllegalArgumentException("\nNon-existent company id!");
                                }
                                if (companyId == 0) {
                                    break;
                                }
                                System.out.println("\nChoose a car:");
                                List<Car> freeCars = actionManager.getAllFreeCarsByCompanyId(companyId);
                                for (int i = 0; i < freeCars.size(); i++) {
                                    System.out.println((i + 1) + ". " + freeCars.get(i).getName());
                                }
                                System.out.print("0. Back\n> ");
                                int carId = scanner.nextInt();
                                if (carId > freeCars.size() || carId < 0) {
                                    throw new IllegalArgumentException("\nNon-existent car id!");
                                }
                                if (carId != 0) {
                                    currentCustomer = actionManager.rentACar(
                                            customerId,
                                            currentCustomer.getName(),
                                            freeCars.get(carId - 1).getId());
                                    System.out.println("You rented '" + freeCars.get(carId - 1).getName() + "'");
                                    break;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("\nInput error! Enter the number");
                                scanner.next();
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        break;
                    case 2:
                        if (currentCustomer.getRentedCarId() == 0) {
                            System.out.println("\nYou didn't rent a car!");
                        } else {
                            currentCustomer = actionManager.returnRentedCar(
                                    customerId,
                                    currentCustomer.getName());
                            System.out.println("\nYou've returned a rented car!");
                        }
                        break;
                    case 3:
                        if (currentCustomer.getRentedCarId() == 0) {
                            System.out.println("\nYou didn't rent a car!");
                        } else {
                            List<String> names = actionManager.myRentedCar(customerId);
                            System.out.println("\nYour rented car:\n" + names.get(0) + "\nCompany:\n" + names.get(1));
                        }
                        break;
                    default:
                        System.out.println("\nUnknown command\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nInput error! Enter the number");
                scanner.next();
            }
        }
    }

    @Override
    public void carMenu(int companyId) {
        String companyName = actionManager.getCompanyById(companyId).getName();
        System.out.print("\n' " + companyName + " ' company");
        while (true) {
            System.out.println("\n1. Car list");
            System.out.println("2. Create a car");
            System.out.print("0. Back\n> ");
            try {
                int carAction = scanner.nextInt();
                switch (carAction) {
                    case 0:
                        return;
                    case 1:
                        List<Car> cars = actionManager.getAllCarsByCompanyId(companyId);
                        if (cars.isEmpty()) {
                            System.out.println("\nThe car list is empty!");
                        } else {
                            System.out.println("\nCar list:");
                            for (int i = 0; i < cars.size(); i++) {
                                System.out.println((i + 1) + ". " + cars.get(i).getName());
                            }
                        }
                        break;
                    case 2:
                        System.out.print("\nEnter the car name:\n> ");
                        scanner.nextLine();
                        String name = scanner.nextLine();
                        int success = actionManager.createCar(name, companyId);
                        System.out.println(success > 0 ? "The car was added!" : "Car adding error!");
                        break;
                    default:
                        System.out.println("\nUnknown command");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nInput error! Enter the number");
                scanner.next();
            }
        }
    }
}
