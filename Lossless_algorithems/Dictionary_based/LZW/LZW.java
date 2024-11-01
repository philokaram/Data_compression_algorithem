
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.Vector;

public class LZW {

    static Scanner input = new Scanner(System.in);

    static public String readFormConsole(char operation) {
        String text = "";
        if (operation == 'c') {
            System.out.println("Enter your text to compress it (Enter \\0 to end reading): ");
        } else {
            System.out.println("Enter your tags to decompress it (Enter \\0 to end reading): ");
        }
        while (true) {
            String line = input.nextLine();
            if (line.charAt(0) == '\\' && line.charAt(1) == '0') {
                break;
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
                text += fInput.nextLine() + "\n";
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
        System.out.println("    _________________________________________________  ");
        System.out.println("   |                                                 | ");
        System.out.println("   |      welcome to LZW compression algorithm       | ");
        System.out.println("   |_________________________________________________| \n");
        System.out.println("Enter 1 to Compress text from the consol.");
        System.out.println("Enter 2 to Decompress tags from the consol.");
        System.out.println("Enter 3 to Compress a txt file.");
        System.out.println("Enter 4 to Decompress a txt file.");
        System.out.print("Enter your choice: ");
        int choice = input.nextInt();
        input.nextLine();
        String in_text;
        String out_text;
        switch (choice) {
            case 1:
                in_text = readFormConsole('c');
                compress(in_text);
                out_text = compress(in_text);
                System.out.println("the compressed text: ");
                System.out.println(out_text);
                break;
            case 2:
                in_text = readFormConsole('d');
                // out_text = decompress(in_text);
                System.out.println("the decompressed Tags: ");
                //System.out.println(out_text);
                break;
            case 3: {
                System.out.print("Enter the path of the text file: ");
                String path = input.nextLine();
                in_text = readFromFile(path);
                // out_text = compress(in_text, 15, 15);
                // writeToFile(path,out_text, 'c');
                break;
            }
            case 4: {
                System.out.print("Enter the path of the Tags file: ");
                String path = input.nextLine();
                in_text = readFromFile(path);
                //out_text = decompress(in_text);
                // writeToFile(path,out_text, 'd');
                break;
            }
            default:
                break;
        }
    }

    public static String compress(String originalText) {
        Vector<String> dictionary = new Vector<>();
        Vector<Integer> indexes = new Vector<>();
        String word = "";
        int index = 0;
        for (int i = 0; i < originalText.length(); i++) {
            word += originalText.charAt(i);
            if (word.length() == 1) {
                index = word.charAt(0) - '\0';
            } else {
                if (dictionary.contains(word)) {
                    index = dictionary.indexOf(word);
                }
                else{
                    dictionary.add(word);
                    if(word.length()==2){
                        indexes.add(index);
                    }else{
                        indexes.add(index+128);
                    }
                    i--;
                    word = "";
                    index = 0;
                    
                }
            }
        }
        if(word.length() != 0){
            if(word.length() == 1){
                indexes.add(word.charAt(0) -'\0');
            }
            else{
                indexes.add(index+128);
            }
        }
        String compressedText = "";
        for (int i : indexes) {
            compressedText += Integer.toString(i) +",";
        }
        return compressedText;
    }
}
