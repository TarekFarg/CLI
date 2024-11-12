package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;


public class DoCommand {
    String command;
    String[] arr;
    public String currentDirectory ;

    public String output;
    String input;

    boolean printOutput;

    // constructors
    public DoCommand(String command, String[] arr) {
        this.command = command;
        this.arr = arr;
        this.output = "";
        this.input = "";
        this.printOutput = true;
        currentDirectory = System.getProperty("user.home");
    }
    public DoCommand()
    {
        currentDirectory = System.getProperty("user.home");
    }

    // getters & setters
    public String getCurrentDirectory()
    {
        return currentDirectory;
    }
    public void setCommand(String command)
    {
        this.command = command ;
        this.printOutput = true;
    }
    public void setArr(String[] arr)
    {
        this.arr = arr ;
    }

    public String getOutput() {
        return output;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setPrintOutput(boolean bool){
        this.printOutput = bool;
    }

    public void display()
    {
        // this function will be removed
        System.out.println(command);
    }

    public void _do() {
        switch (command) {
            case "echo":
                echo();
                break;
            case "pwd":
                printWorkingDirectory();
                break;
            case "cd":
                changeDirectory(arr[0]);
                break;
            case "ls":
                if(arr.length > 0) {
                    listDirectory(arr[0]);
                }
                else {
                    listDirectory(null);
                }
                break;
            case "mkdir":
                makeDirectory();
                break;
            case "rmdir":
                removeDirectory();
                break;
            case "touch":
                createFile();
                break;
            case "mv":
            	if (arr.length < 2) {
                    System.err.println("mv requires source and destination paths.");
                } else {
                    moveFile(arr[0], arr[1]);
                }
                break;
            case "rm":
            	if (arr.length < 1) {
                    System.err.println("rm requires a file or directory path.");
                } else {
                    removeFile(arr[0]);
                }
                break;
            case "cat":
                if (arr.length < 1) {
                    System.err.println("cat requires a file path.");
                } else {
                	concatenateFile(arr[0]);
                }
                break;
            case ">":
                handleRedirection(false);
                break;
            case ">>":
                handleRedirection(true);
                break;
            case "grep":
                grep();
                break;
            default:
                System.out.println("This command not found.");
                break;
        }
    }

    private void echo(){
        if(arr.length < 1){
            System.out.println("Usage: echo statement");
            return;
        }
        this.output = arr[0];
        if(this.printOutput){
            System.out.println(arr[0]);
        }
    }
    private void createFile() {
        if(arr.length==0)
        {
            System.out.println("touch <fileName>");
            return;
        }
        for(String fileName:arr)
        {
            Path tempPath = Paths.get(currentDirectory,fileName) ;
            try{
                if(Files.exists(tempPath))
                {
                    System.out.println("File already exists");
                }
                else
                {
                    Files.createFile(tempPath);
                    System.out.println("Created new file: " + tempPath.toAbsolutePath());
                }
            }catch (IOException e){
                System.out.println("Failed to create file: "+e.getMessage());
            }
        }
    }


    private void grep() {
        String searchTerm = "";
        if (arr.length > 0)
            searchTerm = arr[0];
         else
            System.out.println("Usage: grep <pattern>");

        String[] lines = this.output.split("\n");
        boolean found = false;



        for (String line : lines) {
            if (line.contains(searchTerm)) {
                System.out.println(line);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matches found for: " + searchTerm);
        }
    }

    private void handleRedirection(boolean append) {
        String output = this.output;
        String fileName = this.arr[0].trim();

        File file = new File(currentDirectory, fileName); // Use current directory

        try (FileWriter writer = new FileWriter(file, append)) {
            writer.write(output);
            System.out.println("Output " + (append ? "appended" : "written") + " to file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error with file redirection: " + e.getMessage());
        }
    }

    private void concatenateFile(String fileName) {
    	  Path currentDir = Paths.get(currentDirectory);  // Use currentDirectory variable

        // Create the file path relative to the current working directory
        Path filePath = currentDir.resolve(fileName);

        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (NoSuchFileException e) {
            System.err.println("File not found: " + filePath);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private void removeFile(String fileName) {
        Path currentDir = Paths.get(currentDirectory);  // Use currentDirectory variable

        // Create the file path relative to the current working directory
        Path filePath = currentDir.resolve(fileName);

        // Try deleting the file or directory
        try {
            Files.delete(filePath);
            System.out.println("File or directory deleted successfully: ");
        } catch (NoSuchFileException e) {
            System.err.println("File or directory not found: " + filePath);
        } catch (DirectoryNotEmptyException e) {
            System.err.println("Directory is not empty: " + filePath);
        } catch (IOException e) {
            System.err.println("Error deleting file or directory: " + e.getMessage());
        }
    }

    private void moveFile(String sourcePath, String destinationPath) {
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(destinationPath);
        Path destinationFile = destination.resolve(source.getFileName());

        try {
        	Files.move(source, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File moved successfully from " + sourcePath + " to " + destinationFile);
        } catch (NoSuchFileException e) {
            System.err.println("Source file not found: " + source);
        } catch (IOException e) {
            System.err.println("Error moving file: " + e.getMessage());
        }
    }

    private void removeDirectory() {
        if(arr.length==0)
        {
            System.out.println("rmdir <dirName>");
            return;
        }
        for (String dirName : arr) {
            Path tempPath = Paths.get(currentDirectory, dirName);
            try {
                Files.delete(tempPath);
                System.out.println("Directory deleted: " + tempPath.toAbsolutePath());
            } catch (DirectoryNotEmptyException e) {
                System.err.println("Failed to delete directory. Directory is not empty: " + tempPath.toAbsolutePath());
            } catch (IOException e) {
                System.err.println("Failed to delete directory: " + e.getMessage());
            }
        }
    }

    private void makeDirectory() {
        if(arr.length == 0) {
            System.out.println("mkdir <directoryName>");
            return;
        }

        String dirName;
        Path tempPath;
        if(arr.length>1) { // mkdir <path> <directoryName>
            dirName = arr[1] ;
            tempPath = Paths.get(arr[0],dirName) ;
        }
        else{ // mkdir <directoryName>
            dirName = arr[0] ;
            tempPath = Paths.get(currentDirectory,dirName) ;
        }

        try {
            if(Files.exists(tempPath))
                System.out.println("Directory already exists");
            else {
                Files.createDirectory(tempPath);
                System.out.println("Directory created: " + tempPath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + e.getMessage());
        }
    }

    private void listDirectory(String a) {
        File dir = new File(currentDirectory);
        String[] files;

        if (a == null || a.isEmpty()) {
            // If no argument is provided, list only non-hidden files
            files = dir.list((file, name) -> !name.startsWith("."));
        } else if (a.equals("-a")) {
            // List all files, including hidden ones
            files = dir.list();
        } else {
            System.out.println("Invalid option: " + a);
            return; // Exit the method for invalid options
        }

        if (files != null) {
            this.output = "";
            for (String file : files) {
                this.output += file + "\n";
                if(this.printOutput)
                    System.out.println(file);
            }
        } else {
            System.out.println("Failed to list directory contents.");
        }
    }


    private void changeDirectory(String path) {
        File dir;

        // If path is absolute, use it as is; if relative, join with currentDirectory
        if (new File(path).isAbsolute()) {
            dir = new File(path);
        } else {
            dir = new File(currentDirectory, path);
        }

        try {
            dir = dir.getCanonicalFile();

            if (dir.isDirectory()) {
                currentDirectory = dir.getAbsolutePath();
//                System.setProperty("user.dir", currentDirectory);
                System.out.println("Changed directory to: " + currentDirectory);
            } else {
                System.out.println("Directory not found: " + path);
            }
        } catch (IOException e) {
            System.out.println("Error resolving path: " + e.getMessage());
        }
    }


    private void printWorkingDirectory() {
        this.output = currentDirectory;
        System.out.println(currentDirectory);
    }
}
