package controller;

import model.Coche;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Concesionario {

    private ArrayList<Coche> listaCoche; // Lista para almacenar los objetos Coche
    private Scanner scanner;
    private File file; // Archivo para almacenar los datos de los coches

    public Concesionario() {
        // Inicializa los coches al crear una instancia de Concesionario
        iniciarCoches();
    }

    // Muestra las opciones del menú al usuario
    public void opciones() {
        System.out.println("\n--- Concesionario ---");
        System.out.println("1. Añadir nuevo coche");
        System.out.println("2. Eliminar coche por ID");
        System.out.println("3. Buscar coche por ID");
        System.out.println("4. Listar todos los coches");
        System.out.println("5. Exportar los coches a CSV");
        System.out.println("6. Salir del programa");
        System.out.print("Elige una opción: ");
    }

    // Controla el menú y la interacción con el usuario
    public void menu() {
        int option;
        scanner = new Scanner(System.in);

        do {
            opciones();
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    addCar(); // Añadir nuevo coche
                    break;
                case 2:
                    deleteCarById(); // Eliminar coche por ID
                    break;
                case 3:
                    searchCarById(); // Buscar coche por ID
                    break;
                case 4:
                    listCars(); // Listar todos los coches
                    break;
                case 5:
                    exportCarsToCSV(); // Exportar los coches a CSV
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
        // Inicializa la lista de coches desde un archivo o crea una lista vacía si el archivo no existe
        file = new File("coches.dat");

        if (file.exists()) {
            System.out.println("Cargando los coches del archivo..");
            loadCarsFromFile(file); // Carga los coches del archivo
        } else {
            listaCoche = new ArrayList<Coche>(); // Crea una lista vacía
            System.out.println("Archivo no encontrado. Comenzando con una lista vacía.");
        }
    }

    // Carga los coches desde un archivo
    public void loadCarsFromFile(File file) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))) {
            // Lee el objeto desde el archivo
            listaCoche = (ArrayList<Coche>) objectInputStream.readObject();
            System.out.println("Coches cargados correctamente");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar los coches del archivo: " + e.getMessage() + "\n");
        }
    }

    // Guarda los coches en un archivo
    public void saveCarsToFile() {
        try (ObjectOutputStream objectInputStream = new ObjectOutputStream(new FileOutputStream("coches.dat"))) {
            objectInputStream.writeObject(listaCoche); // Escribe el objeto en el archivo
            System.out.println("Coches añadidos sin problema\n");
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo: " + e.getMessage() + "\n");
        }
    }

    // Añade un nuevo coche a la lista
    public void addCar() {

        int id = getValidId(true);

        String matricula = getValidLicensePlate();

        System.out.print("Marca: ");
        String marca = scanner.nextLine();

        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();

        System.out.print("Color: ");
        String color = scanner.nextLine();

        // Añade el coche a la lista
        listaCoche.add(new Coche(id, matricula, marca, modelo, color));
        System.out.println("Coche añadido con exito");
    }

    // Elimina un coche de la lista por su ID
    public void deleteCarById() {
        int id = getValidId(false);

        // Elimina el coche si existe con una expresión Lambda
        boolean borrarCoche = listaCoche.removeIf(coche -> coche.getId() == id);

        if (borrarCoche) {
            System.out.println("Coche borrado con éxito");
        } else {
            System.out.println("No se ha encontrado un coche con ese ID");
        }
    }

    // Busca un coche por su ID
    public void searchCarById() {

        int id = getValidId(false); // Obtiene un ID válido
        Coche buscarCoche = null;

        for (Coche coche : listaCoche) {
            if (coche.getId() == id) {
                buscarCoche = coche; // Busca el coche por ID
                break;
            }
        }
        if (buscarCoche != null) {
            System.out.println("Coche encontrado:");
            System.out.println(buscarCoche);
        } else {
            System.out.println("No hay ningun coche asociado a ese ID");
        }
    }

    // Lista todos los coches
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

    // Obtiene un ID válido (y único si nuevoCoche es true)
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
                        return valido; // Retorna un ID único
                    }
                } else {
                    return valido; // Retorna un ID válido
                }
            } else {
                System.out.println("Entrada no válida. Por favor, ingresa un número válido.\n");
                scanner.next();
            }
        }
    }

    // Verifica si un ID ya existe en la lista
    public boolean isIdExists(int id) {
        for (Coche coche : listaCoche) {
            if (coche.getId() == id) {
                return true;
            }
        }
        return false;
    }

    // Obtiene una matrícula válida y única
    public String getValidLicensePlate() {
        String matricula;

        while (true) {
            System.out.print("Ingresa una Matrícula única: ");
            matricula = scanner.nextLine().trim();

            // Verifica si la matrícula ya existe en la lista de coches
            if (isLicensePlateExists(matricula)) {
                System.out.println("La matricula ya existe. Introduce otra distinta.\n");
            } else {
                break;
            }
        }
        return matricula; // Retorna la matrícula válida y única
    }

    // Verifica si una matrícula ya existe en la lista
    public boolean isLicensePlateExists(String matricula) {
        for (Coche coche : listaCoche) {
            if (coche.getMatricula().equalsIgnoreCase(matricula)) {
                return true;  // Si encuentra una coincidencia, retorna true
            }
        }
        return false; // Si no encuentra coincidencias, retorna false
    }

    // Exporta los coches a un archivo CSV
    public void exportCarsToCSV() {

        try (FileWriter fileWriter = new FileWriter("src/main/java/resources/coches.csv")) {
            // Escribe la cabecera del archivo CSV
            fileWriter.write("ID,Matricula,Marca,Modelo,Color\n");

            // Recorre la lista de coches y escribe cada coche en el archivo CSV for (Coche coche : listaCoche)
            for (Coche coche : listaCoche) {
                fileWriter.write(coche.getId() + "," +
                        coche.getMatricula() + "," +
                        coche.getMarca() + "," +
                        coche.getModelo() + "," +
                        coche.getColor() + "\n");
            }
            System.out.println("Coches exportados con éxito");
        } catch (IOException e) {
            System.out.println("Error en la exportación al CSV: " + e.getMessage());
        }
    }
}
