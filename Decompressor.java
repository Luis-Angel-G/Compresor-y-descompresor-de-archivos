import java.io.*;
import java.util.*;

public class Decompressor {

    public void decompress(String inputFileName, String outputFileName) throws IOException {
        Map<Character, Double> frequencyTable = new HashMap<>();
        StringBuilder encodedAscii = new StringBuilder();

        // 1. Leer archivo .huff
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            String line;
            boolean readingData = false;

            while ((line = reader.readLine()) != null) {
                if (line.equals("===")) {
                    readingData = true;
                    continue;
                }

                if (!readingData) {
                    String[] parts = line.split(":");
                    if (parts.length != 2) {
                        System.err.println("Línea mal formateada en la tabla de frecuencias: " + line);
                        continue;
                    }
                    String key = parts[0];
                    char ch;

                    // Manejo de caracteres especiales
                    if (key.equals("\\n")) ch = '\n';
                    else if (key.equals("\\r")) ch = '\r';
                    else if (key.equals("\\t")) ch = '\t';
                    else ch = key.charAt(0);

                    try {
                        double freq = Double.parseDouble(parts[1]);
                        frequencyTable.put(ch, freq);
                    } catch (NumberFormatException e) {
                        System.err.println("Error al parsear la frecuencia: " + parts[1]);
                    }
                } else {
                    encodedAscii.append(line);
                }
            }
        }

        // Validar si la tabla de frecuencias está vacía
        if (frequencyTable.isEmpty()) {
            throw new IOException("La tabla de frecuencias está vacía o mal formateada.");
        }

        // 2. Reconstruir árbol de Huffman
        HuffmanTree tree = new HuffmanTree(frequencyTable);
        HuffmanNode root = tree.getRoot();

        // Validar si el árbol de Huffman es válido
        if (root == null) {
            throw new IOException("El árbol de Huffman no pudo ser reconstruido.");
        }

        // 3. Convertir ASCII a binario
        StringBuilder fullBinary = new StringBuilder();
        for (char c : encodedAscii.toString().toCharArray()) {
            String binStr = String.format("%8s", Integer.toBinaryString(c & 0xFF)).replace(' ', '0');
            fullBinary.append(binStr);
        }

        // 4. Decodificar usando árbol de Huffman
        StringBuilder decodedText = new StringBuilder();
        HuffmanNode current = root;

        for (int i = 0; i < fullBinary.length(); i++) {
            current = fullBinary.charAt(i) == '0' ? current.left : current.right;

            if (current == null) {
                // Bit de padding alcanzado, terminar
                System.err.println("Bits de padding alcanzados, terminando decodificación.");
                break;
            }

            if (current.isLeaf()) {
                decodedText.append(current.character);
                current = root;
            }
        }

        // 5. Escribir texto decodificado a archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            writer.write(decodedText.toString());
        } catch (IOException e) {
            throw new IOException("Error al escribir el archivo descomprimido: " + e.getMessage());
        }

        System.out.println("Archivo descomprimido guardado como: " + outputFileName);
    }
}