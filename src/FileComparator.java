import java.io.*;
import java.util.*;

/**
 * The type File comparator. This takes in two files and compares them to find duplicates.
 * Was designed to take in xls files converted to txt files and searches for duplicate information.
 *
 * @author Jacob Jonas
 * @version 2.0
 * @date 4/7/23
 */
public class FileComparator {


    /**
     * The main function reads in two files, file 1 and file 2.
     * It then creates a map of the data from each file, with the key being the data itself and
     * value being a list of DataRow objects containing information about where that data was found.
     * The function then finds all matching keys between both maps (the matching keys are duplicates)
     * and writes this information to an output textfile called &quot;output.txt&quot;. If no duplicates were found, it will write that instead.
     *
     * @param args Pass command line arguments to the main function
     *
     * @return Void
     *
     */
    public static void main(String[] args) throws IOException {
        int count = 0;
        String file1Path = "file1.txt";
        String file2Path = "file2.txt";
        String outputFilePath = "output.txt";
        int rowCount = countTotalRows(file1Path);
        Map<String, List<DataRow>> file1Data = readDataFromFile(file1Path, rowCount);
        rowCount = countTotalRows(file2Path);
        Map<String, List<DataRow>> file2Data = readDataFromFile(file2Path, rowCount);

        Set<String> matchingData = new HashSet<>(file1Data.keySet());
        matchingData.retainAll(file2Data.keySet());

        Set<String> singularData = new HashSet<>();
        singularData.addAll(file1Data.keySet());
        singularData.addAll(file2Data.keySet());

        Set<String> repeatedData = new HashSet<>();
        for (String data : singularData) {
            int count1 = file1Data.containsKey(data) ? file1Data.get(data).size() : 0;
            int count2 = file2Data.containsKey(data) ? file2Data.get(data).size() : 0;
            if (count1 + count2 > 5) {
                repeatedData.add(data);
            }
        }

        PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath));

        if (matchingData.isEmpty() && repeatedData.isEmpty()) {
            writer.println("No matching or repeated data found.");
        } else {
            if (!matchingData.isEmpty()) {
                writer.println("Total duplicates found between file1 and file2: " + matchingData.size());
                writer.println("Matching data:");
                for (String data : matchingData) {
                    writer.println("Data: " + data);
                    writer.println("Found in row(s):");
                    writer.println(file1Path + ":");

                    for (DataRow row : file1Data.get(data)) {
                        count++;
                        writer.println(row.rowNum + ": " + row.text);
                    }
                    writer.println("Total: "+count);
                    count = 0;
                    writer.println("\t---------------------------");
                    writer.println(file2Path + ":");
                    for (DataRow row : file2Data.get(data)) {
                        count++;
                        writer.println(row.rowNum + ": " + row.text);
                    }
                    writer.println("Total: "+count);
                    count = 0;
                    writer.println();
                    writer.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                }
            }
            //if singular files have repeated data and is more than 10 times include it in the output
// Check for repeated data in singular files and include in output if it occurs more than 10 times
            if (!repeatedData.isEmpty()) {
                writer.println("Repeated data in singular files (occurs more than 10 times):");
                writer.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                for (String data : repeatedData) {
                    int count1 = file1Data.containsKey(data) ? file1Data.get(data).size() : 0;
                    int count2 = file2Data.containsKey(data) ? file2Data.get(data).size() : 0;
                    int totalCount = count1 + count2;

                    int max = 100; // This is the max number of times a data can occur in a singular file before it is not included in the output
                    if (totalCount > 10 && totalCount < max) {
                        writer.println("SINGULAR FILE REPEATED DATA");
                        writer.println("Data: " + data);
                        writer.println("Found in row(s):");
                        if (file1Data.containsKey(data)) {
                            writer.println(file1Path + ":");
                            for (DataRow row : file1Data.get(data)) {
                                count++;
                                writer.println(row.rowNum + ": " + row.text);
                            }
                            writer.println("Total: "+count);
                            count = 0;
                        }
                        if (file2Data.containsKey(data)) {
                            writer.println(file2Path + ":");
                            for (DataRow row : file2Data.get(data)) {
                                count++;
                                writer.println(row.rowNum + ": " + row.text);
                            }
                            writer.println("Total: "+count);
                            count = 0;
                        }
                        writer.println();
                        writer.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    }
                }
            }


        }
        writer.close();
    }





                                /**
                                 * The countTotalRows function takes in a file path and returns the number of rows in that file.
                                 *
                                 *
                                 * @param filePath Specify the path of the file to be read
                                 *
                                 * @return The total number of rows in the file
                                 *
                                 */
    private static int countTotalRows(String filePath) throws IOException {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while (reader.readLine() != null) {
                count++;
            }
        }
        return count;
    }


    /**
     * The readDataFromFile function reads the data from a file and returns a map of words to their corresponding
     * DataRow objects. The key is the word, and the value is a list of DataRow objects that contain information about
     * where in the file that word was found.
     * This function also ignores single characters (e.g., &quot;a&quot; or &quot;I&quot;) and matchs that are in every entry
     * as they are not useful for our purposes here.
     *
     * @param filePath Specify the file path of the text file to be read
     *
     * @return A map with a key of type string and a value of type list&lt;datarow&gt;
     */
    private static Map<String, List<DataRow>> readDataFromFile(String filePath, int rowCount) throws IOException {
        Map<String, List<DataRow>> data = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int rowNum = 1;
            while ((line = reader.readLine()) != null) {
                if (Math.abs(rowNum - rowCount) > 1) { // exclude rows that are +/- 1 from total row count
                    String[] words = line.split("\\s+");
                    for (String word : words) {
                        if (word.length() > 1) { // exclude single chars
                            data.computeIfAbsent(word, k -> new ArrayList<>()).add(new DataRow(rowNum, line));
                        }
                    }
                }
                rowNum++;
            }
        }
        return data;
    }


    /**
     * The DataRow function is a constructor for the DataRow class.
     * It takes two arguments: an integer rowNum and a String text.
     * The function returns an instance of the DataRow class, which contains
     * two fields: rowNum and text. These fields are initialized to the values
     * passed in as arguments to this function when it was called.

     *
     * @param rowNum Identify the row number
     * @param text Set the text field
     *
     * @return A datarow object
     *
     * @docauthor Trelent
     */
    private record DataRow(int rowNum, String text) {

        private DataRow {
        }
        }
}
