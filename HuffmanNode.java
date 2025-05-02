public class HuffmanNode implements Comparable<HuffmanNode> {
    public char character;
    public double frequency;
    public HuffmanNode left;
    public HuffmanNode right;

    public HuffmanNode(char character, double frequency) {
        this.character = character;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    public HuffmanNode(double frequency, HuffmanNode left, HuffmanNode right) {
        this.character = '\0';  // Nodo interno
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    @Override
    public int compareTo(HuffmanNode other) {
        return Double.compare(this.frequency, other.frequency);
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }
}