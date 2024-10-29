package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.Scanner;



public class Main {
    static void DisplayHelp()
    {
        System.out.println("Available commands:");
        System.out.println("pwd        - Print working directory");
        System.out.println("cd [path]  - Change directory");
        System.out.println("ls         - List directory contents");
        System.out.println("mkdir [dir]- Create directory");
        System.out.println("rmdir [dir]- Remove directory");
        System.out.println("touch [file]- Create an empty file");
        System.out.println("mv [src] [dest] - Move or rename a file or directory");
        System.out.println("rm [file]  - Remove a file");
        System.out.println("cat [file] - Display file content");
        System.out.println("> [file]   - Redirect output to a file");
        System.out.println("exit       - Exit the CLI");
        System.out.println("help       - Display this help message");
    }



    public static void main(String[] args)
    {
        System.out.println("Welcome to the CLI! Type 'exit' to quit.");
        boolean open = true ;
        Scanner scanner = new Scanner(System.in) ;

        while (open)
        {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if(input.equals("exit"))
            {
                open = false ;
                System.out.println("Exiting CLI");
            }
            else if (input.equals("help"))
            {
                DisplayHelp() ;
            }
            else
            {
                String[] arr = input.split(" ") ;
                DoCommand doCommand = new DoCommand(arr[0], java.util.Arrays.copyOfRange(arr, 1, arr.length)) ;
                doCommand._do();
            }
        }
        scanner.close();
    }
}