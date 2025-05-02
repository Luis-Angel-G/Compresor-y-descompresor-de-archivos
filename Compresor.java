import java.io.*;
import java.util.*;

public class Compresor {

    public void comprimir(String nombreDelArchivo, String nombreDeSalida) throws IOException {
        StringBuilder texto = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(nombreDelArchivo))) {
            int c;
            while ((c = br.read()) != -1) {
                texto.append((char) c);
            }
        }

        if (texto.length() == 0) {
            throw new IOException("El nombre del archivo esta vacío.");
        }

        Map<Character, Integer> mapaDeFrecuencias = new HashMap<>();
        for (char ch : texto.toString().toCharArray()) {
            mapaDeFrecuencias.put(ch, mapaDeFrecuencias.getOrDefault(ch, 0) + 1);
        }

        int caracteresTotales = texto.length();
        Map<Character, Double> frecuenciaRelativa = new HashMap<>();
        for (Map.Entry<Character, Integer> caracterYFrecuencia : mapaDeFrecuencias.entrySet()) {
            frecuenciaRelativa.put(caracterYFrecuencia.getKey(), caracterYFrecuencia.getValue() / (double) caracteresTotales);
        }

        HuffmanTree arbol = new HuffmanTree(frecuenciaRelativa);
        Map<Character, String> codigos = arbol.obtenerCodigos();

        StringBuilder codigoBinario = new StringBuilder();
        for (char ch : texto.toString().toCharArray()) {
            codigoBinario.append(codigos.get(ch));
        }

        StringBuilder codigoAscii = new StringBuilder();
        for (int i = 0; i < codigoBinario.length(); i += 8) {
            String ochoCaracteres = codigoBinario.substring(i, Math.min(i + 8, codigoBinario.length()));
            while (ochoCaracteres.length() < 8) {
                ochoCaracteres += "0";
            }
            int decimal = Integer.parseInt(ochoCaracteres, 2);
            codigoAscii.append((char) decimal);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreDeSalida))) {
            for (Map.Entry<Character, Integer> caracterYFrecuencia : mapaDeFrecuencias.entrySet()) {
                char ch = caracterYFrecuencia.getKey();
                int count = caracterYFrecuencia.getValue();
                if (ch == '\n') writer.write("\\n:" + count + "\n");
                else if (ch == '\r') writer.write("\\r:" + count + "\n");
                else if (ch == '\t') writer.write("\\t:" + count + "\n");
                else writer.write(ch + ":" + count + "\n");
            }
            writer.write("caracteres_totales:" + caracteresTotales + "\n");
            writer.write("longitud_de_bits:" + codigoBinario.length() + "\n");
            writer.write("===\n");
            writer.write(codigoAscii.toString());
        }

        System.out.println("Archivo comprimido guardado como: " + nombreDeSalida);
    }
}