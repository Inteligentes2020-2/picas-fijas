package com.company;

import java.util.Arrays;
import java.util.Scanner;

public class Game {
    protected Agent agent;
    protected int[] user_number;
    protected int[] agent_guess;
    protected boolean winner;
    protected Scanner stdin;
    protected String agent_answer;
    protected String user_answer;

    public Game (int[] user_number, Scanner scanner){
        this.agent = new Agent();
        this.user_number = user_number;
        this.winner = false;
        this.stdin = scanner;
        this.agent_answer = "";
        this.user_answer = "";
        this.startGame();
    }

    private void startGame(){
        while (!winner){
            System.out.println("Su pregunta:");
            int number = 0;
            try{
                number = this.stdin.nextInt();
            } catch (Exception e){
                System.out.println("No ingreso numero valido");
                return;
            }

            //Set sum to zero for reference
            int[] user_guess = new int[4];
            int num = number; //Set num equal to number as reference

            //reads each digit of the scanned number and individually adds them together
            //as it goes through the digits, keep dividing by 10 until its 0.
            for (int i = 0; i<4; i++) {
                int lastDigit = num % 10;
                user_guess[3-i] = lastDigit;
                num = num/10;
            }

            this.agent_answer = this.agent.process(user_guess);
            System.out.println(this.agent_answer);
            if (this.agent_answer.equals("fijas: 4 picas: 0")){
                System.out.println("Jugador gana");
                this.winner = true;
            }

            this.agent_guess = this.agent.agentGuess();
            System.out.println("Computador dice: " + Arrays.toString(this.agent_guess));
            this.user_answer = this.compare(this.agent_guess);
            System.out.println(this.user_answer);
            if (this.user_answer.equals("fijas: 4 picas: 0")){
                System.out.println("Computador gana");
                this.winner = true;
            }

        }
    }

    private String compare(int[] agent_guess){
        int i, j;
        int fijas = 0, picas = 0 ;
        for( i = 0 ; i < 4; i++ ){
            for( j = 0 ; j < 4 ; j++ ){
                if( i == j ){
                    if( this.user_number[i] == agent_guess[j] ){
                        fijas++;
                        break;
                    }
                }
                else{
                    if( this.user_number[i] == agent_guess[j] ){
                        picas++;
                        break;
                    }
                }
            }
        }
        String result = "fijas: " + fijas + " picas: " + picas;
        return result;
    }

}
