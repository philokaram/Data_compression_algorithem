
import java.util.ArrayList;
import java.util.Scanner;

class LZ77 {

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("    _________________________________________________  ");
        System.out.println("   |                                                 | ");
        System.out.println("   |      welcome to LZ77 compression algorithm      | ");
        System.out.println("   |_________________________________________________| \n");
        System.out.println("Enter 1 to Comperes or Decompress in the consol.\nEnter 2 to Comperes or Decompress a txt file.\n");
        System.out.print("Enter your choice: ");
        // int choice = input.nextInt();
        //  if (choice == 1) {
        Comperes();
//} else if (choice == 2) {

//} else {
//}
    }

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

    public static void Comperes() {
        ArrayList<Tag> tags = new ArrayList<>();
        int search_window_size = 10;
        int look_ahead_window_size = 10;
        System.out.print("Enter your text to compress it: ");
        // input.nextLine();
        String text = input.nextLine();
        tags.add(new Tag(0, 0, text.charAt(0)));
        for (int i = 1; i < text.length(); i++) { //text
            ArrayList<Tag> tags_for_symbol = new ArrayList<>();
            for (int j = i - 1; j >= 0 && i - j <= search_window_size; j--) {
                int length = 0;
                for (int k = 0; (k <= look_ahead_window_size) && ((i + k) < text.length()-1) && ((j + k) < i); k++) {
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
                    if ((i == text.length() - 1)&&(i+length >= text.length()-1) ) {
                        nextSymbol = '_';
                    }
                    else {
                        nextSymbol = text.charAt(i+length);
                    }
                    tags_for_symbol.add(new Tag(i - j, length, nextSymbol));
                }
            }
            Tag x = maxTagLength(tags_for_symbol);
            tags.add(x);
            i += x.length;
        }
        for (int i = 0; i < tags.size(); i++) {
            System.out.print("<" + tags.get(i).position + "," + tags.get(i).length + "," + tags.get(i).nextSymbol + ">");
        }
    }
    
}
