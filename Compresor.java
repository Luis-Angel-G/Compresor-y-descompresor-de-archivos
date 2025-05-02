import java.io.*;
import java.util.*;

/**
 * Clase que representa un compresor de archivos utilizando el algoritmo de Huffman.
 * Permite comprimir un archivo de texto en un formato más compacto.
 */
public class Compresor {

    /**
     * Comprime un archivo de texto utilizando el algoritmo de Huffman.
     * 
     * @param nombreDelArchivo El nombre del archivo de entrada que se desea comprimir.
     * @param nombreDeSalida   El nombre del archivo comprimido de salida.
     * @throws IOException Si ocurre un error al leer o escribir los archivos.
     */
    public void comprimir(String nombreDelArchivo, String nombreDeSalida) throws IOException {
        // Leer el contenido del archivo de entrada
        StringBuilder texto = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(nombreDelArchivo))) {
            int c;
            while ((c = br.read()) != -1) {
                texto.append((char) c);
            }
        }

        // Verificar si el archivo está vacío
        if (texto.length() == 0) {
            throw new IOException("El nombre del archivo está vacío.");
        }

        // Calcular las frecuencias de los caracteres
        Map<Character, Integer> mapaDeFrecuencias = new HashMap<>();
        for (char ch : texto.toString().toCharArray()) {
            mapaDeFrecuencias.put(ch, mapaDeFrecuencias.getOrDefault(ch, 0) + 1);
        }

        // Calcular las frecuencias relativas
        int caracteresTotales = texto.length();
        Map<Character, Double> frecuenciaRelativa = new HashMap<>();
        for (Map.Entry<Character, Integer> caracterYFrecuencia : mapaDeFrecuencias.entrySet()) {
            frecuenciaRelativa.put(caracterYFrecuencia.getKey(), caracterYFrecuencia.getValue() / (double) caracteresTotales);
        }

        // Construir el árbol de Huffman
        HuffmanTree arbol = new HuffmanTree(frecuenciaRelativa);
        Map<Character, String> codigos = arbol.obtenerCodigos();

        // Generar el código binario del texto
        StringBuilder codigoBinario = new StringBuilder();
        for (char ch : texto.toString().toCharArray()) {
            codigoBinario.append(codigos.get(ch));
        }

        // Convertir el código binario a ASCII
        StringBuilder codigoAscii = new StringBuilder();
        for (int i = 0; i < codigoBinario.length(); i += 8) {
            String ochoCaracteres = codigoBinario.substring(i, Math.min(i + 8, codigoBinario.length()));
            while (ochoCaracteres.length() < 8) {
                ochoCaracteres += "0";
            }
            int decimal = Integer.parseInt(ochoCaracteres, 2);
            codigoAscii.append((char) decimal);
        }

        // Escribir el archivo comprimido
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreDeSalida))) {
            // Escribir las frecuencias de los caracteres
            for (Map.Entry<Character, Integer> caracterYFrecuencia : mapaDeFrecuencias.entrySet()) {
                char ch = caracterYFrecuencia.getKey();
                int count = caracterYFrecuencia.getValue();
                if (ch == '\n') writer.write("\\n:" + count + "\n");
                else if (ch == '\r') writer.write("\\r:" + count + "\n");
                else if (ch == '\t') writer.write("\\t:" + count + "\n");
                else writer.write(ch + ":" + count + "\n");
            }
            // Escribir la longitud total de caracteres y bits
            writer.write("caracteres_totales:" + caracteresTotales + "\n");
            writer.write("longitud_de_bits:" + codigoBinario.length() + "\n");
            writer.write("===\n");
            // Escribir el contenido comprimido en ASCII
            writer.write(codigoAscii.toString());
        }

        // Confirmar la compresión
        System.out.println("Archivo comprimido guardado como: " + nombreDeSalida);
    }
}