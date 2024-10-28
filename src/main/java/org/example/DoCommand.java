package org.example;

import static java.nio.file.Files.createFile;

public class DoCommand {
    String command;
    String[] arr ;
    DoCommand(String command, String[] arr)
    {
        this.command = command ;
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
                changeDirectory();
                break;
            case "ls":
                listDirectory();
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
        // this function will be updated
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
        // this function will be updated
    }

    private void makeDirectory() {
        // this function will be updated
    }

    private void listDirectory() {
        // this function will be updated
    }

    private void changeDirectory() {
        // this function will be updated
    }

    private void printWorkingDirectory() {
        // this function will be updated
    }
}

