# File Comparator
This is a Java program that takes in two text files and compares them to find duplicates(used for identifying patterns). It was designed to take in .xls files converted to .txt files and searches for duplicate information. If any duplicates are found, the program writes the information to an output text file called "output.txt".

# Usage
The program takes in two text files as input, file1.txt and file2.txt, and outputs the results to output.txt. To run the program, simply compile and run the FileComparator.java file. The program can also be run from the command line by passing in the file paths as arguments to the main function.

# How it Works
The program reads the data from each file and creates a map of the data, with the key being the data itself and the value being a list of DataRow objects containing information about where that data was found. The program then finds all matching keys between both maps (the matching keys are duplicates) and writes this information to the output file.

The readDataFromFile function reads the data from a file and returns a map of words to their corresponding DataRow objects. The key is the word, and the value is a list of DataRow objects that contain information about where in the file that word was found. This function also ignores single characters (e.g., "a" or "I") and matches that are in every entry as they are not useful for the purposes of this program.

The DataRow function is a constructor for the DataRow class. It takes two arguments: an integer rowNum and a String text. The function returns an instance of the DataRow class, which contains two fields: rowNum and text. These fields are initialized to the values passed in as arguments to this function when it was called.



### Author and Version
This program was created by Jacob Jonas and is currently at version 2.0.
