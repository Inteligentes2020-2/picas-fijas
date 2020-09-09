package com.company;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Scanner stdin = new Scanner(System.in);
        System.out.println("Su numero:");
        int number = stdin.nextInt();

        //Set sum to zero for reference
        int[] user_number = new int[4];
        int num = number; //Set num equal to number as reference

        //reads each digit of the scanned number and individually adds them together
        //as it goes through the digits, keep dividing by 10 until its 0.
        for (int i = 0; i<4; i++) {
            int lastDigit = num % 10;
            user_number[3-i] = lastDigit;
            num = num/10;
        }
        System.out.println("Su numero es: " + Arrays.toString(user_number));

        Game juego = new Game(user_number, stdin);
    }
}
