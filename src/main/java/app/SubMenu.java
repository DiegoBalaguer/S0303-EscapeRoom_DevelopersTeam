package app;

import interfaces.AbstractFactory;
import services.ElementService;

import java.util.Scanner;

public class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private final ElementService<?> service;
    private final AbstractFactory factory;

    public Menu(ElementService<?> service, AbstractFactory factory) {
        this.service = service;
        this.factory = factory;
    }

    public void showMenu() {
        while (true) {
            System.out.println("1. Crear elemento");
            System.out.println("2. Leer elementos");
            System.out.println("3. Actualizar elemento");
            System.out.println("4. Eliminar elemento");
            System.out.println("0. Salir");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createElement();
                case 2 -> readElements();
                case 3 -> updateElement();
                case 4 -> deleteElement();
                case 0 -> {
                    System.out.println("Saliendo del menú...");
                    return;
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    private void createElement() {
        System.out.print("Ingrese nombre: ");
        String name = scanner.nextLine();
        /* Pedir otros parámetros específicos */
        // Object... args según el tipo requerido (ejemplo para Room: dificultad y precio)
        Object[] args = {...}; /* Recopilar del usuario */
        service.create(factory.createElement(name, args));
    }

    private void readElements() {
        service.readAll().forEach(System.out::println);
    }

    private void updateElement() {
        System.out.print("Ingrese ID del elemento a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        // Pedir nuevos datos
        Object[] args = {...};
        service.update(id, factory.createElement("NuevoNombre", args));
    }

    private void deleteElement() {
        System.out.print("Ingrese ID del elemento a eliminar: ");
        int id = scanner.nextInt();
        service.delete(id);
    }
}
