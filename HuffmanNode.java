public class HuffmanNode implements Comparable<HuffmanNode> {
    public char caracter;
    public double frecuencia;
    public HuffmanNode izquierda;
    public HuffmanNode derecha;

    public HuffmanNode(char caracter, double frecuencia) {
        this.caracter = caracter;
        this.frecuencia = frecuencia;
        this.izquierda = null;
        this.derecha = null;
    }

    public HuffmanNode(double frecuencia, HuffmanNode izquierda, HuffmanNode derecha) {
        this.caracter = '\0';
        this.frecuencia = frecuencia;
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    @Override
    public int compareTo(HuffmanNode other) {
        return Double.compare(this.frecuencia, other.frecuencia);
    }

    public boolean esHoja() {
        return izquierda == null && derecha == null;
    }
}