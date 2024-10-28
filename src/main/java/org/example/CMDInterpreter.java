package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CMDInterpreter {
    private boolean running;

    public CMDInterpreter() {
        this.running = true;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to CLI! Type 'help' for a list of commands.");

        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            executeCommand(input);
        }
        scanner.close();
    }

    public void executeCommand(String input) {
        String[] args = input.split("\\s+");
        String command = args[0];

        switch (command) {
            case "exit":
                exit();
                break;
            case "help":
                help();
                break;
            case "pwd":
                pwd();
                break;
            case "ls":
                ls(args);
                break;
            case "cd":
                cd(args);
                break;
            case "mkdir":
                mkdir(args);
                break;
            case "rmdir":
                rmdir(args);
                break;
            case "rm":
                rm(args);
                break;
            case "touch":
                touch(args);
                break;
            case "cat":
                cat(args);
                break;
            default:
                System.out.println("Command not recognized. Type 'help' for available commands.");
        }
    }

    private void exit() {
        System.out.println("Exiting CLI...");
        running = false;
    }

    private void help() {
        System.out.println("Available commands:");
        System.out.println("exit - Terminates the CLI");
        System.out.println("help - Displays available commands");
        System.out.println("pwd - Prints the current directory");
        System.out.println("ls - Lists files in the current directory");
        System.out.println("cd [directory] - Changes the current directory");
        System.out.println("mkdir [directory] - Creates a new directory");
        System.out.println("rmdir [directory] - Removes an empty directory");
        System.out.println("rm [file] - Deletes a file");
        System.out.println("touch [file] - Creates a new empty file");
        System.out.println("cat [file] - Displays file content");
    }

    private void pwd() {
        System.out.println(System.getProperty("user.dir"));
    }

    private void ls(String[] args) {
        File currentDir = new File(System.getProperty("user.dir"));
        File[] files = currentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                System.out.println(file.getName());
            }
        } else {
            System.out.println("Unable to list files.");
        }
    }

    private void cd(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: cd [directory]");
            return;
        }
        File dir = new File(args[1]);
        if (dir.exists() && dir.isDirectory()) {
            System.setProperty("user.dir", dir.getAbsolutePath());
            System.out.println("Changed directory to " + dir.getAbsolutePath());
        } else {
            System.out.println("Directory not found: " + args[1]);
        }
    }

    private void mkdir(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: mkdir [directory]");
            return;
        }
        File dir = new File(args[1]);
        if (!dir.exists()) {
            if (dir.mkdir()) {
                System.out.println("Directory created: " + args[1]);
            } else {
                System.out.println("Failed to create directory.");
            }
        } else {
            System.out.println("Directory already exists: " + args[1]);
        }
    }

    private void rmdir(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: rmdir [directory]");
            return;
        }
        File dir = new File(args[1]);
        if (dir.exists() && dir.isDirectory() && dir.list().length == 0) {
            if (dir.delete()) {
                System.out.println("Directory removed: " + args[1]);
            } else {
                System.out.println("Failed to remove directory.");
            }
        } else {
            System.out.println("Directory not empty or does not exist: " + args[1]);
        }
    }

    private void rm(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: rm [file]");
            return;
        }
        File file = new File(args[1]);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("File deleted: " + args[1]);
            } else {
                System.out.println("Failed to delete file.");
            }
        } else {
            System.out.println("File not found: " + args[1]);
        }
    }

    private void touch(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: touch [file]");
            return;
        }
        File file = new File(args[1]);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("File created: " + args[1]);
                }
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
        } else {
            System.out.println("File already exists: " + args[1]);
        }
    }

    private void cat(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: cat [file]");
            return;
        }
        File file = new File(args[1]);
        if (file.exists() && file.isFile()) {
            try (Scanner fileScanner = new Scanner(file)) {
                while (fileScanner.hasNextLine()) {
                    System.out.println(fileScanner.nextLine());
                }
            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
            }
        } else {
            System.out.println("File not found: " + args[1]);
        }
    }

}
