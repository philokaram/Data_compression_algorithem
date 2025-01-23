import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class StandardHuffman {
    static Scanner input = new Scanner(System.in);

    static public String readFormConsole() {
        String text = "";
        while (true) {
            String line = input.nextLine();
            if (line.charAt(0) == '\\' && line.charAt(1) == '0') {
                break;
            }
            if (text.length() != 0) {
                text += '\n';
            }
            text += line;
        }
        return text;
    }

    static public String readFromFile(String path) {
        String text = "";
        try {
            File f = new File(path);
            Scanner fInput = new Scanner(f);
            while (fInput.hasNextLine()) {
                if (text.length() != 0) {
                    text += '\n';
                }
                text += fInput.nextLine();
            }
            fInput.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return text;
    }

    static public void writeToFile(String path, String text, char operation) {
        String newPath = "";
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == '.') {
                break;
            }
            newPath += path.charAt(i);
        }
        if (operation == 'c') {
            newPath += "_Compressed.txt";
        } else {
            newPath += "_Decompressed.txt";
        }
        try {
            File creatFile = new File(newPath);
            creatFile.createNewFile();
            FileWriter f = new FileWriter(newPath);
            f.write(text);
            f.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("    _____________________________________________________________  ");
        System.out.println("   |                                                             | ");
        System.out.println("   |      welcome to Standard Huffman compression algorithm      | ");
        System.out.println("   |_____________________________________________________________| \n");
        System.out.println("Enter 1 to Compress text from the consol.");
        System.out.println("Enter 2 to Decompress tags from the consol.");
        System.out.println("Enter 3 to Compress a txt file.");
        System.out.println("Enter 4 to Decompress a txt file.");
        System.out.print("Enter your choice: ");
        int choice = input.nextInt();
        input.nextLine();
        String in_text;
        String out_text;
        if (choice == 1) {
            System.out.println("Enter your text to compress it (Enter \\0 to end reading): ");
            in_text = readFormConsole();
            out_text = Compress(in_text);
            System.out.println("the compressed text: ");
            System.out.println(out_text);
        } else if (choice == 2) {
            System.out.println("Enter your tags to decompress it (Enter \\0 to end reading): ");
            in_text = readFormConsole();
            out_text = decompress(in_text);
            System.out.println("the decompressed text: ");
            System.out.println(out_text);
        } else if (choice == 3) {
            System.out.print("Enter the path of the text file: ");
            String path = input.nextLine();
            in_text = readFromFile(path);
            out_text = Compress(in_text);
            writeToFile(path, out_text, 'c');
        } else if (choice == 4) {
            System.out.print("Enter the path of the compressed file: ");
            String path = input.nextLine();
            in_text = readFromFile(path);
            out_text = decompress(in_text);
            writeToFile(path, out_text, 'd');
        } else {
        }
    }

    public static String Compress(String originalText) {
        // 1- calculate frequency (instead of probability)
        Map<Character, Integer> frequencyMap = new HashMap<Character, Integer>();
        for (int i = 0; i < originalText.length(); i++) {
            if (frequencyMap.containsKey(originalText.charAt(i))) {
                frequencyMap.put(originalText.charAt(i), frequencyMap.get(originalText.charAt(i)) + 1);
            } else {
                frequencyMap.put(originalText.charAt(i), 1);
            }
        }

        // 2- make nodes and sort it
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> row : frequencyMap.entrySet()) {
            priorityQueue.add(new Node(row.getKey(), row.getValue()));
        }
        // 3- create code Nodes
        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            Node parent = new Node(left.getFrequency() + right.getFrequency(), left, right);
            priorityQueue.add(parent);
        }
        Map<Character, String> codesMap = new HashMap<Character, String>();
        generateCode(priorityQueue.poll(), "", codesMap);
        // 4-compress
        String BinaryText = "";
        for (int i = 0; i < originalText.length(); i++) {
            BinaryText += codesMap.get(originalText.charAt(i));
        }

        for (Map.Entry<Character, String> e : codesMap.entrySet()) {
            if (e.getKey() == '\n') {
                BinaryText += "\n_," + e.getValue();
            } else {
                BinaryText += "\n" + e.getKey() + "," + e.getValue();
            }
        }
        return BinaryText;
    }

    public static void generateCode(Node root, String code, Map<Character, String> codesMap) {
        if (root == null) {
            return;
        }
        if (root.getRight() == null && root.getLeft() == null) {
            codesMap.put(root.getValue(), code);
            return;
        }
        generateCode(root.getLeft(), code + "0", codesMap);
        generateCode(root.getRight(), code + "1", codesMap);
    }

    public static String decompress(String decodedText) {
        Map<String, Character> codesMap = new HashMap<String, Character>();
        String[] arr = decodedText.split("\n");
        decodedText = arr[0];
        for (int i = 1; i < arr.length; i++) {
            String[] charCode = arr[i].split(",");
            if (charCode[0].charAt(0) == '_') {
                codesMap.put(charCode[1], '\n');
            } else {
                codesMap.put(charCode[1], charCode[0].charAt(0));
            }
        }
        String code = "", originalText = "";
        for (int i = 0; i < decodedText.length(); i++) {
            code += decodedText.charAt(i);
            if (codesMap.containsKey(code)) {
                originalText += codesMap.get(code);
                code = "";
            }
        }

        return originalText;
    }

    public static class Node implements Comparable<Node> {
        private Character value;
        private Node left;
        private Node right;
        private int frequency;

        public Node(Character value, int frequency) {
            this.value = value;
            this.frequency = frequency;
        }

        public Node(int frequency, Node left, Node right) {
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public Character getValue() {
            return value;
        }

        public void setValue(Character value) {
            this.value = value;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        public Node getLeft() {
            return left;
        }

        @Override
        public int compareTo(Node other) {
            return this.frequency - other.frequency;
        }

    }
}