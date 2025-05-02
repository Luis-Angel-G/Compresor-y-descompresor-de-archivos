import java.io.*;
import java.util.*;

public class Descompresor {

    public void descomprimir(String nombreDelArchivo, String nombreDeSalida) throws IOException {
        Map<Character, Integer> mapaDeFrecuencias = new HashMap<>();
        int caracteresTotales = 0;
        int longitudDeBits = 0;
        StringBuilder codigoAscii = new StringBuilder();

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

        if (caracteresTotales == 0 || longitudDeBits == 0 || mapaDeFrecuencias.isEmpty()) {
            throw new IOException(".huff invalido");
        }

        Map<Character, Double> tablaDeFrecuencias = new HashMap<>();
        for (Map.Entry<Character, Integer> caracterYConteo : mapaDeFrecuencias.entrySet()) {
            tablaDeFrecuencias.put(caracterYConteo.getKey(), caracterYConteo.getValue() / (double) caracteresTotales);
        }

        HuffmanTree arbol = new HuffmanTree(tablaDeFrecuencias);
        HuffmanNode raiz = arbol.obtenerRaiz();

        StringBuilder codigoBinario = new StringBuilder();
        for (char c : codigoAscii.toString().toCharArray()) {
            String binStr = String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
            codigoBinario.append(binStr);
        }

        StringBuilder textoDecodificado = new StringBuilder();
        HuffmanNode actual = raiz;

        for (int i = 0; i < longitudDeBits && i < codigoBinario.length(); i++) {
            actual = codigoBinario.charAt(i) == '0' ? actual.izquierda : actual.derecha;

            if (actual.esHoja()) {
                textoDecodificado.append(actual.caracter);
                actual = raiz;
            }
        }

        if (textoDecodificado.length() != caracteresTotales) {
            throw new IOException("Texto decodificado no coincide: " + textoDecodificado.length() + " vs " + caracteresTotales);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreDeSalida))) {
            writer.write(textoDecodificado.toString());
        }

        System.out.println("Archivo descomprimido guardado como: " + nombreDeSalida);
    }
}