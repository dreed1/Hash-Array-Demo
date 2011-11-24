//menu class for hash array demo
//written by Dan Reed
//last updated 11/23/11

import java.util.Scanner;

public class Menu{
    public Menu(){
        
    }
    public void run(){
        //HashArray array = new HashArray();
        printIntro();
        int mChoice =-1;
        while(mChoice!=3){
            printMenu();
            mChoice = getMenuChoice();
            doMenuLoop(mChoice);
        }
    }
    public void doMenuLoop(int choice){
        switch(choice){
            case 1:
                System.out.println("Probe Hash");
                ProbeHash paul = new ProbeHash();
                paul.doSomeHashing();
                break;
            case 2:
                System.out.println("Chain Hash");
                ChainHash sue = new ChainHash();
                sue. doSomeHashing();
                break;
            case 3:
                System.out.println("Going so soon? Come back!");
                break;
            default:
                System.out.println("I dunno if you meant to do that, but you should slow down.");
                System.out.println("Try it again, put a helmet on this time.");
                break;
        }
    }
    public int getMenuChoice(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter a choice.");
        int tmp = sc.nextInt();
        return tmp;
    }
    public void printIntro(){
        System.out.println("-------------------------------------------------");
        System.out.println("--------------Hash array simulator---------------");
        System.out.println("---------------Written by Dan Reed---------------");
        System.out.println("-------------------------------------------------");
        System.out.println("-This program will create a hash array and fill -");
        System.out.println("-it with random strings. It takes this output   -");
        System.out.println("-and maps the process to an animated .gif file  -");
        System.out.println("-and the final result to a static png.          -");
        System.out.println("-------------------------------------------------");
        System.out.println("The colored dots in the pictures are single data ");
        System.out.println("members, our ideal behavior. They change colors");
        System.out.println("along a gradient as they fill the array.         ");
        System.out.println("-------------------------------------------------");
        System.out.println("The gray/black pixels show us collisions.");
        System.out.println("Darker grays and blacks are the result of heavy");
        System.out.println("searching. Black means we're wasting CPU time.");
        System.out.println("-------------------------------------------------");
        System.out.println("Not only does this demo include 2 different means");
        System.out.println("of handling collisions, it also includes four");
        System.out.println("different hash methods to see what it looks like");
        System.out.println("to load up a hash array-- and a key to understand");
        System.out.println("your visually represented data.");
        System.out.println("-------------------------------------------------");
    }
    public void printMenu(){
        System.out.println("Main Menu");
        System.out.println("1. Probe Hash");
        System.out.println("2. Chain Hash");
        System.out.println("3. Quit");
    }
}


