
// HERE’S THE COMPLETE CODE THAT INCLUDES GENERATING THE HUFFMAN CODES FOR THE TEXT:

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class HuffmanNode {
    int frequency;
    char character;
    HuffmanNode left;
    HuffmanNode right;
}

class HuffmanComparator implements Comparator<HuffmanNode> {
    public int compare(HuffmanNode x, HuffmanNode y) {
        return x.frequency - y.frequency;
    }
}

public class FileReadMain {
    // Previous methods...
        // public static void main(String[] args) {
        //         String text = readFile("metamorphosis.txt");
        //         if (text != null) {
        //             analyzeText(text);
        //             generateWordFrequencyList(text, "frequencies.txt");
        //             // Extension: Generate Huffman codes for the text
        //         } else {
        //             System.out.println("Error reading the file.");
        //         }
        //     }
        
            public static String readFile(String fileName) {
                StringBuilder sb = new StringBuilder();
                try {
                    File file = new File(fileName);
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine()) {
                        sb.append(scanner.nextLine()).append("\n");
                    }
                    scanner.close();
                } catch (FileNotFoundException e) {
                    return null;
                }
                return sb.toString();
            }

    public static void analyzeText(String text) {
        int charCount = text.length();
        int wordCount = countWords(text);
        int sentenceCount = countSentences(text);
        int paragraphCount = countParagraphs(text);

        System.out.println("Length in characters: " + charCount);
        System.out.println("Number of words: " + wordCount);
        System.out.println("Number of sentences: " + sentenceCount);
        System.out.println("Number of paragraphs: " + paragraphCount);
    }

    public static int countWords(String text) {
        String[] words = text.split("\\s+");
        return words.length;
    }

    public static int countSentences(String text) {
        String[] sentences = text.split("[.!?]+");
        return sentences.length;
    }

    public static int countParagraphs(String text) {
        String[] paragraphs = text.split("\n\\s*\n");
        return paragraphs.length;
    }

    public static void generateWordFrequencyList(String text, String outputFileName) {
        HashMap<String, Integer> wordFrequency = new HashMap<>();
        Pattern wordPattern = Pattern.compile("\\b(\\w+('\\w+)?)\\b");
        Matcher matcher = wordPattern.matcher(text);

        while (matcher.find()) {
            String word = matcher.group().toLowerCase();
            wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
        }

        try {
            PrintWriter writer = new PrintWriter(new File(outputFileName));
            for (String word : wordFrequency.keySet()) {
                writer.println(word + ": " + wordFrequency.get(word));
            }
            writer.close();
            System.out.println("Word frequency list saved to " + outputFileName);
        } catch (FileNotFoundException e) {
            System.out.println("Error creating the frequency list file.");
        }
    }

    public static void generateHuffmanCodes(String text) {
        HashMap<Character, Integer> frequencyMap = buildFrequencyMap(text);
        PriorityQueue<HuffmanNode> priorityQueue = buildPriorityQueue(frequencyMap);

        HuffmanNode rootNode = buildHuffmanTree(priorityQueue);
        Map<Character, String> huffmanCodes = new HashMap<>();
        generateCodes(rootNode, "", huffmanCodes);

        System.out.println("Huffman Codes:");
        for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
            System.out.println("'" + entry.getKey() + "': " + entry.getValue());
        }
    }

    public static HashMap<Character, Integer> buildFrequencyMap(String text) {
        HashMap<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        return frequencyMap;
    }

    public static PriorityQueue<HuffmanNode> buildPriorityQueue(HashMap<Character, Integer> frequencyMap) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>(new HuffmanComparator());
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            HuffmanNode node = new HuffmanNode();
            node.character = entry.getKey();
            node.frequency = entry.getValue();
            node.left = null;
            node.right = null;
            priorityQueue.add(node);
        }
        return priorityQueue;
    }

    public static HuffmanNode buildHuffmanTree(PriorityQueue<HuffmanNode> priorityQueue) {
        while (priorityQueue.size() > 1) {
            HuffmanNode x = priorityQueue.poll();
            HuffmanNode y = priorityQueue.poll();

            HuffmanNode newNode = new HuffmanNode();
            newNode.frequency = x.frequency + y.frequency;
            newNode.left = x;
            newNode.right = y;
            priorityQueue.add(newNode);
        }
        return priorityQueue.poll();
    }

    public static void generateCodes(HuffmanNode node, String code, Map<Character, String> huffmanCodes) {
        if (node.left == null && node.right == null) {
            huffmanCodes.put(node.character, code);
            return;
        }
        generateCodes(node.left, code + "0", huffmanCodes);
        generateCodes(node.right, code + "1", huffmanCodes);
    }

    public static void main(String[] args) {
        String text = readFile("metamorphosis.txt");
        if (text != null) {
            analyzeText(text);
            generateWordFrequencyList(text, "frequencies.txt");
            generateHuffmanCodes(text); // Extension: Generate Huffman codes for the text
        } else {
            System.out.println("Error reading the file.");
        }
    }
}
