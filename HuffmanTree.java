import java.util.*;

/**
 * Representa un árbol de Huffman utilizado para la compresión de datos.
 * Este árbol se construye a partir de las frecuencias de los caracteres
 * y genera códigos binarios únicos para cada carácter.
 */
public class HuffmanTree {
    /**
     * Nodo raíz del árbol de Huffman.
     */
    private HuffmanNode raiz;

    /**
     * Mapa que asocia cada carácter con su código binario correspondiente.
     */
    private Map<Character, String> caracterConCodigo = new HashMap<>();

    /**
     * Construye un árbol de Huffman a partir de un mapa de frecuencias de caracteres.
     *
     * @param frecuencias Un mapa donde las claves son caracteres y los valores son sus frecuencias.
     */
    public HuffmanTree(Map<Character, Double> frecuencias) {
        PriorityQueue<HuffmanNode> colaDePrioridad = new PriorityQueue<>();
        for (Map.Entry<Character, Double> caracterYFrecuencia : frecuencias.entrySet()) {
            colaDePrioridad.offer(new HuffmanNode(caracterYFrecuencia.getKey(), caracterYFrecuencia.getValue()));
        }

        while (colaDePrioridad.size() > 1) {
            HuffmanNode izquierda = colaDePrioridad.poll();
            HuffmanNode derecha = colaDePrioridad.poll();
            HuffmanNode padre = new HuffmanNode(izquierda.frecuencia + derecha.frecuencia, izquierda, derecha);
            colaDePrioridad.offer(padre);
        }

        this.raiz = colaDePrioridad.poll();
        crearCodigoBinario(this.raiz, "");
    }

    /**
     * Genera los códigos binarios para cada carácter recorriendo el árbol de Huffman.
     *
     * @param nodo  El nodo actual del árbol.
     * @param codigo El código binario acumulado hasta el nodo actual.
     */
    private void crearCodigoBinario(HuffmanNode nodo, String codigo) {
        if (nodo == null) return;
        if (nodo.esHoja()) {
            caracterConCodigo.put(nodo.caracter, codigo);
        }
        crearCodigoBinario(nodo.izquierda, codigo + "0");
        crearCodigoBinario(nodo.derecha, codigo + "1");
    }

    /**
     * Obtiene el mapa de códigos binarios generados para cada carácter.
     *
     * @return Un mapa donde las claves son caracteres y los valores son sus códigos binarios.
     */
    public Map<Character, String> obtenerCodigos() {
        return caracterConCodigo;
    }

    /**
     * Obtiene la raíz del árbol de Huffman.
     *
     * @return El nodo raíz del árbol.
     */
    public HuffmanNode obtenerRaiz() {
        return raiz;
    }
}