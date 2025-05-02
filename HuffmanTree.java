import java.util.*;

public class HuffmanTree {
    private HuffmanNode root;
    private Map<Character, String> huffmanCodes = new HashMap<>();

    public HuffmanTree(Map<Character, Double> frequencies) {
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Double> entry : frequencies.entrySet()) {
            pq.offer(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();
            HuffmanNode parent = new HuffmanNode(left.frequency + right.frequency, left, right);
            pq.offer(parent);
        }

        this.root = pq.poll();
        generateCodes(this.root, "");
    }

    private void generateCodes(HuffmanNode node, String code) {
        if (node == null) return;
        if (node.isLeaf()) {
            huffmanCodes.put(node.character, code);
        }
        generateCodes(node.left, code + "0");
        generateCodes(node.right, code + "1");
    }

    public Map<Character, String> getCodes() {
        return huffmanCodes;
    }

    public HuffmanNode getRoot() {
        return root;
    }
}