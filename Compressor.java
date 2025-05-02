import java.io.*;
import java.util.*;

public class Compressor {

    public void compress(String inputFileName, String outputFileName) throws IOException {
        // 1. Leer el archivo original
        StringBuilder text = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
            int c;
            while ((c = br.read()) != -1) {
                text.append((char) c);
            }
        }

        // 2. Calcular frecuencias relativas
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char ch : text.toString().toCharArray()) {
            freqMap.put(ch, freqMap.getOrDefault(ch, 0) + 1);
        }

        int totalChars = text.length();
        Map<Character, Double> relativeFreq = new HashMap<>();
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            relativeFreq.put(entry.getKey(), entry.getValue() / (double) totalChars);
        }

        // 3. Crear árbol de Huffman
        HuffmanTree tree = new HuffmanTree(relativeFreq);
        Map<Character, String> codes = tree.getCodes();

        // 4. Codificar el texto
        StringBuilder encodedBits = new StringBuilder();
        for (char ch : text.toString().toCharArray()) {
            encodedBits.append(codes.get(ch));
        }

        // 5. Convertir bits en bloques de 8 a ASCII
        StringBuilder asciiEncoded = new StringBuilder();
        for (int i = 0; i < encodedBits.length(); i += 8) {
            String byteString = encodedBits.substring(i, Math.min(i + 8, encodedBits.length()));
            // Rellenar con ceros si falta
            while (byteString.length() < 8) {
                byteString += "0";
            }
            int decimal = Integer.parseInt(byteString, 2);
            asciiEncoded.append((char) decimal);
        }

        // 6. Escribir archivo .huff
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            // Parte 1: guardar tabla de frecuencias
            for (Map.Entry<Character, Double> entry : relativeFreq.entrySet()) {
                char ch = entry.getKey();
                double freq = entry.getValue();
                if (ch == '\n') writer.write("\\n:" + freq + "\n");
                else if (ch == '\r') writer.write("\\r:" + freq + "\n");
                else if (ch == '\t') writer.write("\\t:" + freq + "\n");
                else writer.write(ch + ":" + freq + "\n");
            }
            writer.write("===\n"); // Separador entre frecuencia y datos

            // Parte 2: escribir el texto codificado como ASCII
            writer.write(asciiEncoded.toString());
        }

        System.out.println("Archivo comprimido guardado como: " + outputFileName);
    }
}