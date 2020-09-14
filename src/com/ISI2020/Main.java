package com.ISI2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("InfiniteLoopStatement")
public class Main {

    public static void main(String[] args) throws IOException {

        //Este bloque pide un input valido del numero de juego de usuario
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Bienvenido programa que jeuga picas y fijas");
        while (true){
            System.out.println("Escribe un numero de 4 digitos sin repetir");
            String pattern = "^(?!.*(.).*\\1)\\d{4}$";
            String number;
            Pattern r = Pattern.compile(pattern);
            while(true){
                number = reader.readLine();
                Matcher m = r.matcher(number);
                if (m.find( )) {
                    System.out.println("Tu numero es: " + m.group(0) );
                    break;
                }else {
                    System.out.println("NO ES UN VALOR VALIDO");
                }
            }

            //instancia el agente que juega y variables de picas y fijas
            Agent agent = new Agent();
            int picas=0, fijas=0;
            //comienza a adivinar
            String guess = agent.compute("start");
            int turn = 1;
            while (true){
                if (number.equals(guess)){
                    //gana el juego y acaba el programa
                    guess = agent.compute("finish");
                    System.out.println();
                    System.out.println("Agente encontro el numero en el turno: "+ turn);
                    System.out.println("Agente dice que el numero es: "+ guess);
                    break;
                }else{
                    //evalua el numero propuesto por el agente, primero si hay fija, luego si hay pica
                    for (int i=0; i<4; i++){
                        if (guess.charAt(i)==number.charAt(i)){
                            fijas++;
                        }else if (guess.contains(String.valueOf(number.charAt(i)))){
                            picas++;
                        }
                    }
                }
                //imprime resultados del turno y vuelve a jugar
                System.out.println();
                System.out.println("Turno: "+ turn);
                System.out.println("Agente dice que el numero es: "+ guess);
                System.out.println("Picas: "+ picas + " Fijas: " + fijas);
                guess = agent.compute("continue",picas,fijas);
                picas=0;
                fijas=0;
                turn++;
                if (turn>40){break;}
            }
        }

    }
}
