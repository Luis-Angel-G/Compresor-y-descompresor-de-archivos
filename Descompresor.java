import java.io.*;
import java.util.*;

/**
 * Clase que representa un descompresor de archivos utilizando el algoritmo de Huffman.
 * Permite descomprimir un archivo previamente comprimido en su formato original.
 */
public class Descompresor {

    /**
     * Descomprime un archivo comprimido utilizando el algoritmo de Huffman.
     *
     * @param nombreDelArchivo El nombre del archivo comprimido de entrada.
     * @param nombreDeSalida   El nombre del archivo de salida descomprimido.
     * @throws IOException Si ocurre un error al leer o escribir los archivos, o si el archivo comprimido es inválido.
     */
    public void descomprimir(String nombreDelArchivo, String nombreDeSalida) throws IOException {
        // Mapa para almacenar las frecuencias de los caracteres
        Map<Character, Integer> mapaDeFrecuencias = new HashMap<>();
        int caracteresTotales = 0;
        int longitudDeBits = 0;
        StringBuilder codigoAscii = new StringBuilder();

        // Leer el archivo comprimido
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreDelArchivo))) {
            String line;
            boolean readingData = false;

            while ((line = reader.readLine()) != null) {
                if (line.equals("===")) {
                    readingData = true;
                    char[] buffer = new char[1024];
                    int charsRead;
                    while ((charsRead = reader.read(buffer)) != -1) {
                        codigoAscii.append(buffer, 0, charsRead);
                    }
                    break;
                }

                if (!readingData) {
                    String[] parts = line.split(":");
                    if (parts.length != 2) continue;
                    String llave = parts[0];
                    String valor = parts[1];

                    if (llave.equals("caracteres_totales")) {
                        caracteresTotales = Integer.parseInt(valor);
                    } else if (llave.equals("longitud_de_bits")) {
                        longitudDeBits = Integer.parseInt(valor);
                    } else {
                        char ch;
                        if (llave.equals("\\n")) ch = '\n';
                        else if (llave.equals("\\r")) ch = '\r';
                        else if (llave.equals("\\t")) ch = '\t';
                        else ch = llave.charAt(0);

                        int count = Integer.parseInt(valor);
                        mapaDeFrecuencias.put(ch, count);
                    }
                }
            }
        }

        // Validar que los datos leídos sean correctos
        if (caracteresTotales == 0 || longitudDeBits == 0 || mapaDeFrecuencias.isEmpty()) {
            throw new IOException(".huff invalido");
        }

        // Calcular las frecuencias relativas
        Map<Character, Double> tablaDeFrecuencias = new HashMap<>();
        for (Map.Entry<Character, Integer> caracterYConteo : mapaDeFrecuencias.entrySet()) {
            tablaDeFrecuencias.put(caracterYConteo.getKey(), caracterYConteo.getValue() / (double) caracteresTotales);
        }

        // Reconstruir el árbol de Huffman
        HuffmanTree arbol = new HuffmanTree(tablaDeFrecuencias);
        HuffmanNode raiz = arbol.obtenerRaiz();

        // Convertir el contenido ASCII a código binario
        StringBuilder codigoBinario = new StringBuilder();
        for (char c : codigoAscii.toString().toCharArray()) {
            String binStr = String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
            codigoBinario.append(binStr);
        }

        // Decodificar el texto utilizando el árbol de Huffman
        StringBuilder textoDecodificado = new StringBuilder();
        HuffmanNode actual = raiz;

        for (int i = 0; i < longitudDeBits && i < codigoBinario.length(); i++) {
            actual = codigoBinario.charAt(i) == '0' ? actual.izquierda : actual.derecha;

            if (actual.esHoja()) {
                textoDecodificado.append(actual.caracter);
                actual = raiz;
            }
        }

        // Validar que el texto decodificado coincida con el número total de caracteres
        if (textoDecodificado.length() != caracteresTotales) {
            throw new IOException("Texto decodificado no coincide: " + textoDecodificado.length() + " vs " + caracteresTotales);
        }

        // Escribir el texto decodificado en el archivo de salida
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreDeSalida))) {
            writer.write(textoDecodificado.toString());
        }

        // Confirmar la descompresión
        System.out.println("Archivo descomprimido guardado como: " + nombreDeSalida);
    }
}