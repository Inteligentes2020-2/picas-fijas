package com.company;

import java.util.Arrays;
import java.util.Random;

public class Agent {
    private int[] agent_number = new int[4];
    private Random rd = new Random();
    private String action;

    public Agent(){
        this.generate();
        this.action = "NO_OP";
    }

    private void generate(){
        int i=0, num;
        while( i < 4 ){
            int paso = 0;
            num= this.rd.nextInt(10);
            for(int j=0;j<i;j++){
                if( this.agent_number[j] == (char) (num) ){
                    paso = 1;
                }
            }
            if( paso == 0 ){
                this.agent_number[ i ] = (char)( num );
                i++;
            }
        }
    }

    /*public String process(String perception){
        return this.agentGuess();
    }*/

    public String process(int[] perception){
        return this.userGuess(perception);
    }

    private String userGuess(int[] user_guess){
        int i, j;
        int fijas = 0, picas = 0 ;
        for( i = 0 ; i < 4; i++ ){
            for( j = 0 ; j < 4 ; j++ ){
                if( i == j ){
                    if( this.agent_number[i] == user_guess[j] ){
                        fijas++;
                        break;
                    }
                }
                else{
                    if( this.agent_number[i] == user_guess[j] ){
                        picas++;
                        break;
                    }
                }
            }
        }
        this.action = "fijas: " + fijas + " picas: " + picas;
        return this.action;
    }

    public int[] agentGuess(){
        int[] guess = new int[4];
        int i=0, num;
        while( i < 4 ){
            int paso = 0;
            num= this.rd.nextInt(10);
            for(int j=0;j<i;j++){
                if( guess[j] == (char) (num) ){
                    paso = 1;
                }
            }
            if( paso == 0 ){
                guess[ i ] = (char)( num );
                i++;
            }
        }
        this.action = Arrays.toString(guess);
        return guess;
    }
}
