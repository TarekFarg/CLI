package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.Scanner;


public class Main {
    static void DisplayHelp()
    {
        // this function will be updated
        System.out.println("Help") ;
    }

    public static void main(String[] args)
    {
        System.out.println("Welcome to the CLI! Type 'exit' to quit.");
        boolean open = true ;
        Scanner scanner = new Scanner(System.in) ;

        while (open)
        {
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