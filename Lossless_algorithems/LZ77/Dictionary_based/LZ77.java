
import java.util.ArrayList;
import java.util.Scanner;

class LZ77 {
    static Scanner input = new Scanner(System.in);

    static class Tag {
        public int position;
        public int length;
        public char nextSymbol;

        public Tag() {
            this.position = 0;
            this.length = 0;
            this.nextSymbol = '\n';
        }

        public Tag(int position, int length, char nextSymbol) {
            this.position = position;
            this.length = length;
            this.nextSymbol = nextSymbol;
        }
    }

    static public Tag maxTagLength(ArrayList<Tag> arr) {
        int maxILength = -1;
        int maxLength = -1;
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).length > maxLength) {
                maxLength = arr.get(i).length;
                maxILength = i;
            }
        }
        return arr.get(maxILength);
    }

    static public String read(char from, char operation) {
        String text = "";
        if (from == 'c') {
            if (operation == 'c') {
                System.out.println("Enter your text to compress it (Enter \\0 to end reading): ");
            } else {
                System.out.println("Enter your tags to decompress it (Enter \\0 to end reading): ");
            }
            while(true){
                String line = input.nextLine();
                if(line.charAt(0)== '\\' && line.charAt(1)== '0'  ){
                    break;
                }
                text += line;
            }
            return text;
        }
        return text;
    }
    static public void write(String text,char from, char operation) {
        if (from == 'c') {
            if (operation == 'c') {
                System.out.println("the compressed text: ");
            } else {
                System.out.println("the decompressed Tags: ");
            }
           System.out.println(text);
        }
    }

    public static void main(String[] args) {
        System.out.println("    _________________________________________________  ");
        System.out.println("   |                                                 | ");
        System.out.println("   |      welcome to LZ77 compression algorithm      | ");
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
        if (choice == 1) {
            in_text = read('c', 'c');
            out_text =compress(in_text,10,10);
            write(out_text, 'c', 'c');
        } else if (choice == 2) {
            in_text = read('c', 'd');
            out_text = decompress(in_text);
            write(out_text, 'c', 'd');
        } else if (choice == 3) {
            in_text = read('f', 'c');
        } else if (choice == 4) {
            in_text = read('f', 'd');
        } else {
        }
    }

    public static String compress(String text , int search_window_size ,int look_ahead_window_size  ) {
        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(new Tag(0, 0, text.charAt(0)));
        for (int i = 1; i < text.length(); i++) { // text
            ArrayList<Tag> tags_for_symbol = new ArrayList<>();
            for (int j = i - 1; j >= 0 && i - j <= search_window_size; j--) {
                int length = 0;
                for (int k = 0; (k <= look_ahead_window_size) && ((i + k) < text.length() - 1) && ((j + k) < i); k++) {
                    if (text.charAt(i + k) == text.charAt(j + k)) {
                        length++;
                    } else {
                        break;
                    }
                }
                if (length == 0) {
                    tags_for_symbol.add(new Tag(0, 0, text.charAt(i)));
                } else {
                    char nextSymbol;
                    if ((i == text.length() - 1) && (i + length >= text.length() - 1)) {
                        nextSymbol = '_';
                    } else {
                        nextSymbol = text.charAt(i + length);
                    }
                    tags_for_symbol.add(new Tag(i - j, length, nextSymbol));
                }
            }
            Tag x = maxTagLength(tags_for_symbol);
            tags.add(x);
            i += x.length;
        }
        String outputString="";
        for (int i = 0; i < tags.size(); i++) {
            outputString += ("<" + tags.get(i).position + "," + tags.get(i).length + "," + tags.get(i).nextSymbol + ">");
        }
        return outputString;
    }

    public static String decompress(String input_tags) {
        String decompressed_text = "";
        for (int i = 0; i < input_tags.length(); i++) {
            if (input_tags.charAt(i) == '<') {
                int position = (input_tags.charAt((i + 1)) - '0');
                int length = (input_tags.charAt((i + 3)) - '0');
                for (int j = decompressed_text.length() - position; length > 0; length--, j++) {
                    decompressed_text += decompressed_text.charAt(j);
                }
                decompressed_text += input_tags.charAt(i + 5);
                i += 6;
            }
        }
        return decompressed_text;
    }
}
