/**
 * Representa un nodo en un árbol de Huffman, utilizado para la compresión de datos.
 * Cada nodo puede ser una hoja que contiene un carácter y su frecuencia,
 * o un nodo interno que tiene referencias a nodos hijos.
 */
public class HuffmanNode implements Comparable<HuffmanNode> {

    /**
     * El carácter representado por este nodo (solo para nodos hoja).
     * Si el nodo no es una hoja, este valor será '\0'.
     */
    public char caracter;

    /**
     * La frecuencia del carácter o la suma de frecuencias de los nodos hijos.
     */
    public double frecuencia;

    /**
     * Referencia al nodo hijo izquierdo.
     */
    public HuffmanNode izquierda;

    /**
     * Referencia al nodo hijo derecho.
     */
    public HuffmanNode derecha;

    /**
     * Constructor para un nodo hoja.
     *
     * @param caracter   El carácter representado por este nodo.
     * @param frecuencia La frecuencia del carácter.
     */
    public HuffmanNode(char caracter, double frecuencia) {
        this.caracter = caracter;
        this.frecuencia = frecuencia;
        this.izquierda = null;
        this.derecha = null;
    }

    /**
     * Constructor para un nodo interno.
     *
     * @param frecuencia La suma de las frecuencias de los nodos hijos.
     * @param izquierda  Referencia al nodo hijo izquierdo.
     * @param derecha    Referencia al nodo hijo derecho.
     */
    public HuffmanNode(double frecuencia, HuffmanNode izquierda, HuffmanNode derecha) {
        this.caracter = '\0';
        this.frecuencia = frecuencia;
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    /**
     * Compara este nodo con otro nodo de Huffman basado en la frecuencia.
     *
     * @param other El otro nodo de Huffman a comparar.
     * @return Un valor negativo si este nodo tiene menor frecuencia,
     *         un valor positivo si tiene mayor frecuencia,
     *         o 0 si las frecuencias son iguales.
     */
    @Override
    public int compareTo(HuffmanNode other) {
        return Double.compare(this.frecuencia, other.frecuencia);
    }

    /**
     * Verifica si este nodo es una hoja (no tiene hijos).
     *
     * @return {@code true} si el nodo es una hoja, {@code false} en caso contrario.
     */
    public boolean esHoja() {
        return izquierda == null && derecha == null;
    }
}