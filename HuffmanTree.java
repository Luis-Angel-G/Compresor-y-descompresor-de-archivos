import java.util.*;

public class HuffmanTree {
    private HuffmanNode raiz;
    private Map<Character, String> caracterConCodigo = new HashMap<>();

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

    private void crearCodigoBinario(HuffmanNode nodo, String codigo) {
        if (nodo == null) return;
        if (nodo.esHoja()) {
            caracterConCodigo.put(nodo.caracter, codigo);
        }
        crearCodigoBinario(nodo.izquierda, codigo + "0");
        crearCodigoBinario(nodo.derecha, codigo + "1");
    }

    public Map<Character, String> obtenerCodigos() {
        return caracterConCodigo;
    }

    public HuffmanNode obtenerRaiz() {
        return raiz;
    }
}