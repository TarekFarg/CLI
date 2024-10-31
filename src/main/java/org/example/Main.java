package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.Arrays;
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
        System.out.println("[command has output] > [file]   - Redirect output to a file");
        System.out.println("[command has output] >> [file]  - Append output to a file");
        System.out.println("[command has output] | [command has input]    - Pipe output to another command");
        System.out.println("exit       - Exit the CLI");
        System.out.println("help       - Display this help message");
    }

    static void handleInput(String input, DoCommand doCommand){
            if (input.contains(" > ")) {
                    String[] arr = input.split(" > ");

                    String fileName = arr[1].trim();
                    String[] fullCommand = arr[0].trim().split(" ");

                    doCommand.setCommand(fullCommand[0]);
                    doCommand.setArr(Arrays.copyOfRange(fullCommand, 1, fullCommand.length));
                    doCommand.setPrintOutput(false);
                    doCommand._do();

                    doCommand.setCommand(">");
                    doCommand.setArr(new String[]{fileName});
                    doCommand._do();
            }
            else if (input.contains(" >> ")) {
                String[] arr = input.split(" >> ");

                String fileName = arr[1].trim();
                String[] fullCommand = arr[0].trim().split(" ");

                doCommand.setCommand(fullCommand[0]);
                doCommand.setArr(Arrays.copyOfRange(fullCommand, 1, fullCommand.length));
                doCommand.setPrintOutput(false);
                doCommand._do();

                doCommand.setCommand(">>");
                doCommand.setArr(new String[]{fileName});
                doCommand._do();
            }
                else if(input.contains(" | ")){
                    String[] arr = input.split("\\|") ;

                    String[] fullCommandOne = arr[0].trim().split(" ");
                    String[] fullCommandTwo = arr[1].trim().split(" ");

                    doCommand.setCommand(fullCommandOne[0]);
                    doCommand.setArr(Arrays.copyOfRange(fullCommandOne, 1, fullCommandOne.length));
                    doCommand.setPrintOutput(false);
                    doCommand._do();


                    doCommand.setCommand(fullCommandTwo[0]);
                    doCommand.setArr(Arrays.copyOfRange(fullCommandTwo, 1, fullCommandTwo.length));
                    doCommand._do();
                }
                else{
                    String[] arr = input.split(" ") ;
                    doCommand.setCommand(arr[0]);
                    doCommand.setArr(Arrays.copyOfRange(arr, 1, arr.length));
                    doCommand._do();
                }
    }

    public static void main(String[] args)
    {
        System.out.println("Welcome to the CLI! Type 'exit' to quit.");
        boolean open = true ;
        Scanner scanner = new Scanner(System.in) ;
        DoCommand doCommand = new DoCommand();

        while (open)
        {
            // printing current path
            System.out.print(doCommand.getCurrentDirectory());
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
                handleInput(input, doCommand);
            }
        }
        scanner.close();
    }
}