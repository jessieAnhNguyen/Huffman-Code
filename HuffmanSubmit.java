/*
 * Name: Anh Nguyen
 * NETID: anguy39
 * Email: anguy39@u.rochester.edu
 * Assignment: Lab 5
 * Lab session: Monday Wednesday 15:25 - 16:40
 *
 * I DID NOT COLLABORATE WITH ANYONE ON THIS ASSIGNMENT
 */
import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

// Import any package as required
public class HuffmanSubmit implements Huffman {
    // Feel free to add more methods and variables as required.
    HashMap<Character, Integer> freqMap;
    HashMap<Character, String> codeMap;

    class Node{
        /*
         * This class represents a Node of the Huffman tree. A node contains an element (a char) and its frequency.
         */
        Character element;
        int frequency;
        Node left;
        Node right;

        //Constructors
        public Node(Character e, int f){
            element = e;
            frequency = f;
        }

        public Node(Character e, int f, Node l, Node r){
            element = e;
            frequency = f;
            left = l;
            right = r;
        }
    }

    public Node buildHuffTree(){
        /*
         * This method is used to build the Huffman tree, using PriorityQueue
         */
        //initialize a priority queue
        PriorityQueue<Node> queue = new PriorityQueue<>(freqMap.keySet().size(), new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.frequency < o2.frequency) return -1;
                else if (o1.frequency > o2.frequency) return 1;
                else return 0;
            }
        });
        //Add the Nodes to the priority queue
        for (Character c: freqMap.keySet()){
            queue.add(new Node(c, freqMap.get(c)));
        }
        //Build the Huffman tree
        Node n1, n2, n3 = null;
        while (queue.size() > 1){
            n1 = queue.poll();
            n2 = queue.poll();
            n3 = new Node(null, n1.frequency + n2.frequency, n1, n2);
            queue.add(n3);
        }
        return n3;
    }

    public void createBinMap(Node curr, String code){
        /*
         * This method is used to create a map that stores each character to its code from the Huffman tree
         */
        if (curr == null) return;
        if (curr.element != null){
            codeMap.put(curr.element, code);
            return;
        }
        else{
            createBinMap(curr.left, code + "0");
            createBinMap(curr.right, code + "1");
        }
    }


    public void encode(String inputFile, String outputFile, String freqFile){
        // TODO: Your code here

         //Read the input file and count the frequency of each character, store them in a map
        BinaryIn input = new BinaryIn(inputFile);
        freqMap = new HashMap<>();
        while (!input.isEmpty()){
            char c = input.readChar(); //use the readChar() method in BinaryIn to read the char
            if (freqMap.containsKey(c)){
                freqMap.put(c, freqMap.get(c) + 1);
            }
            else{
                freqMap.put(c, 1);
            }
        }

        //Print the frequency file: each character (in binary form) and its frequency
        FileOutputStream newFile = null;
        try{
           newFile = new FileOutputStream(freqFile);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        for (Character c: freqMap.keySet()){
            String binaryChar = Integer.toBinaryString(c);
            while (binaryChar.length() < 8){
                binaryChar = "0" + binaryChar; //add zeros so that the length is 8
            }
            binaryChar = binaryChar + ":" + freqMap.get(c) + "\n";
            try{
                newFile.write(binaryChar.getBytes());
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        //Close the file
        try{
            newFile.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        //Build the Huffman tree and create a map to store the code of each character
        Node huffTree = buildHuffTree();
        codeMap = new HashMap<>();
        createBinMap(huffTree, "");

        //Reread the input file and encode the output file
        input = new BinaryIn(inputFile);
        BinaryOut output = new BinaryOut(outputFile);
        while (!input.isEmpty()){
            char c = input.readChar();
            String charCode = codeMap.get(c);
            for (int i = 0; i < charCode.length(); i++){
                if (charCode.charAt(i) == '1'){
                    output.write(false);
                }
                else {
                    output.write(true);
                }
            }
        }
        output.flush();
        //end of encode method
    }


    public void decode(String inputFile, String outputFile, String freqFile){
        // TODO: Your code here
        //Read from the frequency file and map each character with its frequency
        FileInputStream file = null;
        freqMap = new HashMap<>();
        try{
            file = new FileInputStream(freqFile);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(file));
        while (true){
            try{
                String dataIn = reader.readLine();
                if (dataIn == null) break;
                String [] data = dataIn.split(":");
                char c = (char) Integer.parseInt(data[0], 2);
                int freq = Integer.parseInt(data[1]);
                freqMap.put(c, freq);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        //Build the Huffman tree
        Node root = buildHuffTree();

        //Read the input file, decode them using the Huffman Tree, and print on the output file
        BinaryIn input = new BinaryIn(inputFile);
        BinaryOut output = new BinaryOut(outputFile);
        Node curr = root;
        while (!input.isEmpty()){
            boolean bin = input.readBoolean();
            if (bin == true){
                curr = curr.left;
            }
            else if (bin == false){
                curr = curr.right;
            }
            if (curr.element != null){
                output.write(curr.element);
                curr = root;
                continue;
            }
        }
        output.flush();
        //end of decode method
    }


    public static void main(String[] args) {
        Huffman  huffman = new HuffmanSubmit();
        huffman.encode("ur.jpg", "ur.enc", "freq.txt");
        huffman.decode("ur.enc", "ur_dec.jpg", "freq.txt");

        huffman.encode("alice30.txt", "alice30.enc", "alicefreq.txt");
        huffman.decode("alice30.enc", "alice30_dec.txt", "alicefreq.txt");
        // After decoding, both ur.jpg and ur_dec.jpg should be the same.
        // On linux and mac, you can use `diff' command to check if they are the same.
    }

}
