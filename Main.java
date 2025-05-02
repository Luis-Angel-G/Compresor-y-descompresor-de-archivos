import java.util.Scanner;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Compresor compresor = new Compresor();
        Descompresor descompresor = new Descompresor();
        boolean continuar = true;

        while (continuar) {
            System.out.println("Sistema de Compresión Huffman");
            System.out.println("1. Comprimir archivo");
            System.out.println("2. Descomprimir archivo");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción (1-3): ");

            String input = scanner.nextLine().trim();
            int opcion;
            try {
                opcion = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, ingrese un número válido.");
                continue;
            }


            try {
                switch (opcion) {
                    case 1:
                        System.out.print("Nombre del archivo .txt a comprimir: ");
                        String compressInput = scanner.nextLine().trim();
                        File compressFile = new File(compressInput);
                        if (!compressFile.exists()) {
                            System.out.println("Error: El archivo no existe.");
                        } else if (compressFile.length() == 0) {
                            System.out.println("Error: El archivo está vacío.");
                        } else {
                            compresor.comprimir(compressInput, compressInput + ".huff");
                        }
                        break;

                    case 2:
                        System.out.print("Nombre del archivo .huff a descomprimir: ");
                        String decompressInput = scanner.nextLine().trim();
                        File decompressFile = new File(decompressInput);
                        if (!decompressFile.exists()) {
                            System.out.println("Error: El archivo no existe.");
                        } else if (decompressFile.length() == 0) {
                            System.out.println("Error: El archivo está vacío.");
                        } else {
                            descompresor.descomprimir(decompressInput, decompressInput.replace(".huff", "_descomprimido.txt"));
                        }
                        break;

                    case 3:
                        continuar = false;
                        System.out.println("¡Gracias por usar el sistema de compresión Huffman!");
                        break;

                    default:
                        System.out.println("Error: Opción inválida. Seleccione 1, 2 o 3.");
                }
            } catch (Exception e) {
                System.out.println("Ocurrió un error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}