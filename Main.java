import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Compressor compressor = new Compressor();
        Decompressor decompressor = new Decompressor();

        System.out.println("1. Comprimir archivo");
        System.out.println("2. Descomprimir archivo");
        System.out.print("Seleccione una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine(); // limpiar buffer

        try {
            if (opcion == 1) {
                System.out.print("Nombre del archivo .txt a comprimir: ");
                String input = scanner.nextLine();
                compressor.compress(input, input + ".huff");
            } else if (opcion == 2) {
                System.out.print("Nombre del archivo .huff a descomprimir: ");
                String input = scanner.nextLine();
                decompressor.decompress(input, input.replace(".huff", "_descomprimido.txt"));
            } else {
                System.out.println("Opción inválida.");
            }
        } catch (Exception e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
        }
    }
}