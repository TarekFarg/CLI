package org.example;

import java.io.*;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class DoCommand {
    String command;
    String[] arr;
    private String currentDirectory = System.getProperty("user.dir");

    // constructors
    DoCommand(String command, String[] arr) {
        this.command = command;
        this.arr = arr;
    }
    DoCommand()
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
    }
    public void setArr(String[] arr)
    {
        this.arr = arr ;
    }
    public void display()
    {
        // this function will be removed
        System.out.println(command);
    }

    public void _do() {
        switch (command) {
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
                moveFile();
                break;
            case "rm":
                removeFile();
                break;
            case "cat":
                concatenateFile();
                break;
            case ">":
                redirectOutput();
                break;
            case ">>":
                appendOutput();
                break;
            case "|":
                pipeCommand();
                break;
            default:
                System.out.println("This command not found.");
                break;
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

    private void pipeCommand() {
        // this function will be updated
    }

    private void appendOutput() {
        // this function will be updated
    }

    private void redirectOutput() {
        // this function will be updated
    }

    private void concatenateFile() {
        // this function will be updated
    }

    private void removeFile() {
        // this function will be updated
    }

    private void moveFile() {
        // this function will be updated
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
            for (String file : files) {
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
                System.setProperty("user.dir", currentDirectory); // Update system property for consistent output
                System.out.println("user.dir: " + currentDirectory);
                System.out.println("Changed directory to: " + currentDirectory);
            } else {
                System.out.println("Directory not found: " + path);
            }
        } catch (IOException e) {
            System.out.println("Error resolving path: " + e.getMessage());
        }
    }


    private void printWorkingDirectory() {
        System.out.println(currentDirectory);
    }
}
