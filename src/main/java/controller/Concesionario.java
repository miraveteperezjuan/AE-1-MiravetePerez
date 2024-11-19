package controller;

import model.Coche;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Concesionario {

    private ArrayList<Coche> listaCoche;
    private Scanner scanner;
    private File file;

    public Concesionario() {
        iniciarCoches();
    }

    public void opciones() {
        System.out.println("\n--- Parking ---");
        System.out.println("1. Añadir nuevo coche");
        System.out.println("2. Eliminar coche por ID");
        System.out.println("3. Buscar coche por ID");
        System.out.println("4. Listar todos los coches");
        System.out.println("5. Exportar los coches a CSV");
        System.out.println("6. Salir del programa");
        System.out.print("Elige una opción: ");
    }

    public void menu() {
        int option;
        scanner = new Scanner(System.in);

        do {
            opciones();
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    addCar();
                    break;
                case 2:
                    deleteCarById();
                    break;
                case 3:
                    searchCarById();
                    break;
                case 4:
                    listCars();
                    break;
                case 5:
                    exportCarsToCSV();
                    break;
                case 6:
                    System.out.println("Guardando datos...");
                    saveCarsToFile();
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción invalida. Vuelva a intentarlo");
            }
        } while (option != 6);
    }

    public void iniciarCoches() {
        file = new File("coches.dat");

        if (file.exists()) {
            System.out.println("Cargando los coches del archivo..");
            loadCarsFromFile(file);
        } else {
            listaCoche = new ArrayList<Coche>();
            System.out.println("Archivo no encontrado. Comenzando con una lista vacía.");
        }
    }

    public void loadCarsFromFile(File file) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))) {
            listaCoche = (ArrayList<Coche>) objectInputStream.readObject();
            System.out.println("Coches cargados correctamente");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar los coches del archivo: " + e.getMessage() + "\n");
        }
    }

    public void saveCarsToFile() {
        try (ObjectOutputStream objectInputStream = new ObjectOutputStream(new FileOutputStream("coches.dat"))) {
            objectInputStream.writeObject(listaCoche);
            System.out.println("Coches añadidos sin problema\n");
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo: " + e.getMessage() + "\n");
        }
    }

    public void addCar() {

        int id = getValidId(true);

        String matricula = getValidLicensePlate();

        System.out.print("Marca: ");
        String marca = scanner.nextLine();

        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();

        System.out.print("Color: ");
        String color = scanner.nextLine();

        listaCoche.add(new Coche(id, matricula, marca, modelo, color));
        System.out.println("Car añadido con exito");
    }

    public void deleteCarById() {
        int id = getValidId(false);

        boolean borrarCoche = listaCoche.removeIf(coche -> coche.getId() == id);

        if (borrarCoche) {
            System.out.println("Coche borrado con éxito");
        } else {
            System.out.println("No se ha encontrado un coche con ese ID");
        }
    }

    public void searchCarById() {

        int id = getValidId(false);

        Coche buscarCoche = listaCoche.stream()
                .filter(coche -> coche.getId() == id)
                .findFirst()
                .orElse(null);

        if (buscarCoche != null) {
            System.out.println("Car encontrado:");
            System.out.println(buscarCoche);
        } else {
            System.out.println("No hay ningun coche asociado a ese ID");
        }
    }

    public void listCars() {
        if (listaCoche.isEmpty()) {
            System.out.println("No hay coches en el concesionario");
        } else {
            System.out.println("--- Lista de coches ---\n");
            for (Coche coche : listaCoche) {
                System.out.println(coche);
            }
        }
    }

    public int getValidId(boolean nuevoCoche) {
        while (true) {
            System.out.print("Ingresa un ID válido (número): ");
            if (scanner.hasNextInt()) {
                int valido = scanner.nextInt();
                scanner.nextLine();
                if(nuevoCoche) {
                    if (isIdExists(valido)) {
                        System.out.println("El ID ya existe. Por favor, ingresa un ID diferente.\n");
                    } else {
                        return valido;
                    }
                } else {
                    return valido;
                }
            } else {
                System.out.println("Entrada no válida. Por favor, ingresa un número válido.\n");
                scanner.next();
            }
        }
    }

    public boolean isIdExists(int id) {
        return listaCoche.stream().anyMatch(coche -> coche.getId() == id);
    }

    public String getValidLicensePlate() {
        String matricula;

        while (true) {
            System.out.print("Ingresa una Matrícula única:: ");
            matricula = scanner.nextLine().trim();

            if (isLicensePlateExists(matricula)) {
                System.out.println("La matricula ya existe. Introduce otra distinta.\n");
            } else {
                break;
            }
        }
        return matricula;
    }

    public boolean isLicensePlateExists(String matricula) {
        return listaCoche.stream().anyMatch(coche -> coche.getMatricula().equalsIgnoreCase(matricula));
    }

    public void exportCarsToCSV() {
        String archivoCoches = "coches.csv";

        try (FileWriter writer = new FileWriter(archivoCoches)) {
            writer.write("ID;Matrícula;Marca;Modelo;Color\n");

            for (Coche coche : listaCoche) {
                writer.write(coche.getId() + ";" +
                        coche.getMatricula() + ";" +
                        coche.getMarca() + ";" +
                        coche.getModelo() + ";" +
                        coche.getColor() + "\n");
            }

            System.out.println("Coches exportados a " + archivoCoches + " con éxito");
        } catch (IOException e) {
            System.out.println("Error en la exportación al CSV: " + e.getMessage());
        }
    }
}
