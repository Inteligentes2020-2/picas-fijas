package com.ISI2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Agent {

    private String guess;
    private int prevPicas;
    private int prevFijas;
    private int picas;
    private int fijas;
    private boolean stage; //delimita si ya se conocen los 4 digitos del numero
    private int[][] known; //guarda fijas o picas (1, 0) para etapa de los 4 digitos conocidos
    private int equalCounter;
    private int acc;
    private boolean group;
    private ArrayList<String> permutations;
    private Random random;
    private boolean midPhase;
    private boolean used;

    public Agent(){
        this.guess="0789";
        this.prevPicas=0;
        this.prevFijas=0;
        this.picas=0;
        this.fijas=0;
        this.stage=false;
        this.known = new int[][] {{-1,-1}, {-1,-1}, {-1,-1}, {-1,-1}};
        this.equalCounter=0;
        this.acc=0;
        this.permutations = new ArrayList<>();
        this.random = new Random();
        this.group=false;
        this.midPhase=false;
        this.used=false;
    }

    public String compute(String action){
        if (action.equals("start")){
            //se usa para iniciar juego, puede simplemente retornar ultimo valor guardado en caso de ser llamado
            return this.guess;
        }
        if (action.equals("finish")||action.equals("reset")) {
            //reinicia agente y da ultimo valor
            String finalGuess = this.guess;
            this.guess="0789";
            this.prevPicas=0;
            this.prevFijas=0;
            this.picas=0;
            this.fijas=0;
            this.stage=false;
            this.known = new int[][] {{-1,-1}, {-1,-1}, {-1,-1}, {-1,-1}};
            this.equalCounter=0;
            this.acc=0;
            this.permutations = new ArrayList<>();
            this.random = new Random();
            this.group=false;
            this.midPhase=false;
            this.used=false;
            return finalGuess;
        }
        return "WAITING VALID STATE";
    }
    public String compute(String action, int picas, int fijas){
        if (action.equals("continue")){
            this.prevPicas=this.picas;
            this.prevFijas=this.fijas;
            this.picas=picas;
            this.fijas=fijas;

            if (!this.stage&&!this.midPhase){

                if (this.fijas + this.picas > 3){ //se consiguen los 4 digitos, 789 son parte

                    this.stage=true;
                    int n = 0;
                    if (this.fijas>this.prevFijas&&this.guess.charAt(0)!='0'){
                        n=1;
                    }
                    this.known= new int[][]{{Character.getNumericValue(this.guess.charAt(0)), n}, {7, 0}, {8, 0}, {9, 0}};
                    this.acc=4;

                }else if(!this.guess.equals("0789")){ //para turno 2 en adelante
                    //para conseguir los 4 digitos
                    if(this.fijas<this.prevFijas&&this.guess.equals("1789")){
                        this.known[0][0]=0;
                        this.known[0][1]=1;
                        this.acc++;
                        if(this.acc>=4){ //si entra aca, ya se tienen los digitos
                            this.stage=true;
                        }
                    }else if (this.fijas>this.prevFijas){ //si suben las fijas quiere decir que es fija
                        this.known[0][0]=Character.getNumericValue(this.guess.charAt(0));
                        this.known[0][1]=1;
                        this.acc++;
                        if(this.acc>=4){ //si entra aca, ya se tienen los digitos
                            this.stage=true;
                        }

                    }
                    if (this.picas>this.prevPicas){ //si suben las picas quiere decir que esta es pica pero las amteriores acumuladas no

                        int i = 1;
                        while(i<3&&this.known[i][0]!=-1){
                            i++;
                        }
                        this.known[i][0]=Character.getNumericValue(this.guess.charAt(0));
                        this.known[i][1]=0;
                        this.acc++;
                        if(this.acc>=4){ //si entra aca, ya se tienen los digitos
                            this.stage=true;
                        }
                        this.equalCounter=0;

                    } else if (this.picas==this.prevPicas){ //si se mantienen las picas quiere decir pueden haber varias picas seguidas o no

                        this.equalCounter++;

                    } else { //si se reducen las picas quiere decir que las anteriores acumuladas eran picas

                        for (int j = 1; j<equalCounter+1; j++){
                            int i = 1;
                            while(i<3&&this.known[i][0]!=-1){
                                i++;
                            }
                            this.known[i][0]=Character.getNumericValue(this.guess.charAt(0))-j;
                            this.known[i][1]=0;
                            this.acc++;
                            if (this.acc>=4) { //si entra aca, ya se tienen los digitos
                                this.stage = true;
                                break;
                            }
                        }
                        this.equalCounter=0;

                    }
                    this.guess=this.guess.replace(this.guess.charAt(0), (char) (this.guess.charAt(0)+1));
                    if (this.guess.equals("7789")){
                        this.midPhase=true;
                    }

                }else { //para turno 1

                    this.guess=this.guess.replace(this.guess.charAt(0), (char) (this.guess.charAt(0)+1));
                    if (this.picas>0){this.equalCounter=1;}

                }

            }
            if (!this.stage&&this.midPhase){//si entra en estos if, en 789 hay digito(s) del numero

                boolean p = true;
                int q = random.nextInt(6);
                while(p){
                    if (this.guess.contains(String.valueOf(q))){
                        q = random.nextInt(6);
                        p =true;
                    }else{
                        p=false;
                    }
                    for (int i = 0; i<4; i++){
                        if (this.known[i][0]==q){
                            q = random.nextInt(6);
                            p=true;
                            break;
                        }else{
                            p=false;
                        }
                    }
                }

                if(this.used){
                    System.out.println(this.acc);
                    if (this.fijas+this.picas<this.prevFijas+this.prevPicas){
                        if (this.guess.contains("8")){
                            if (this.fijas+this.picas<this.prevFijas+this.prevPicas-1){
                                int i = 1;
                                while(i<3&&this.known[i][0]!=-1){
                                    i++;
                                }
                                if (this.known[0][0]==-1){i=0;}
                                this.known[i][0]=7;
                                this.known[i][1]=0;
                                this.acc++;
                                System.out.println(Arrays.deepToString(this.known));
                                if(this.acc>=4){ //si entra aca, ya se tienen los digitos
                                    this.stage=true;
                                    this.midPhase=false;
                                }
                            }
                            if (this.picas+this.fijas==0){
                                int i = 1;
                                while(i<3&&this.known[i][0]!=-1){
                                    i++;
                                }
                                if (this.known[0][0]==-1){i=0;}
                                this.known[i][0]=6;
                                this.known[i][1]=0;
                                this.acc++;
                                System.out.println(Arrays.deepToString(this.known));
                                if(this.acc>=4){ //si entra aca, ya se tienen los digitos
                                    this.stage=true;
                                    this.midPhase=false;
                                }
                            }
                        }else if (this.guess.contains("9")){
                            int i = 1;
                            while(i<3&&this.known[i][0]!=-1){
                                i++;
                            }
                            if (this.known[0][0]==-1){i=0;}
                            this.known[i][0]=8;
                            this.known[i][1]=0;
                            this.acc++;
                            System.out.println(Arrays.deepToString(this.known));
                            if(this.acc>=4){ //si entra aca, ya se tienen los digitos
                                this.stage=true;
                                this.midPhase=false;
                            }
                            if (this.picas+this.fijas==0){
                                i = 1;
                                while(i<3&&this.known[i][0]!=-1){
                                    i++;
                                }
                                if (this.known[0][0]==-1){i=0;}
                                this.known[i][0]=6;
                                this.known[i][1]=0;
                                this.acc++;
                                System.out.println(Arrays.deepToString(this.known));
                                if(this.acc>=4){ //si entra aca, ya se tienen los digitos
                                    this.stage=true;
                                    this.midPhase=false;
                                }
                            }
                        }else{
                            int i = 1;
                            while(i<3&&this.known[i][0]!=-1){
                                i++;
                            }
                            if (this.known[0][0]==-1){i=0;}
                            this.known[i][0]=9;
                            this.known[i][1]=0;
                            this.acc++;
                            System.out.println(Arrays.deepToString(this.known));
                            if(this.acc>=4){ //si entra aca, ya se tienen los digitos
                                this.stage=true;
                                this.midPhase=false;
                            }
                            if (this.picas+this.fijas==0){
                                i = 1;
                                while(i<3&&this.known[i][0]!=-1){
                                    i++;
                                }
                                if (this.known[0][0]==-1){i=0;}
                                this.known[i][0]=6;
                                this.known[i][1]=0;
                                this.acc++;
                                System.out.println(Arrays.deepToString(this.known));
                                if(this.acc>=4){ //si entra aca, ya se tienen los digitos
                                    this.stage=true;
                                    this.midPhase=false;
                                }
                            }
                        }
                    }
                }

                if (this.guess.contains("7")){

                    this.guess = String.valueOf(q)+this.guess.substring(1);
                     p = true;
                     q = random.nextInt(6);
                    while(p){
                        if (this.guess.contains(String.valueOf(q))){
                            q = random.nextInt(6);
                            p =true;
                        }else{
                            p=false;
                        }
                        for (int i = 0; i<4; i++){
                            if (this.known[i][0]==q){
                                q = random.nextInt(6);
                                p=true;
                                break;
                            }else{
                                p=false;
                            }
                        }
                    }
                    this.guess = this.guess.substring(0,1)+String.valueOf(q)+this.guess.substring(2);
                    this.used=true;

                }else if (this.guess.contains("8")){

                    this.guess = this.guess.substring(0,2)+String.valueOf(q)+this.guess.substring(3);
                    this.used=true;

                }else if (this.guess.contains("9")){

                    this.guess = this.guess.substring(0,3)+String.valueOf(q);
                    this.used=true;

                }

            }

            if (this.stage){
                //para adivinar orden de digitos
                this.guess="";
                if (this.known[0][1]==1){this.guess = String.valueOf(this.known[0][0]);}
                if (!this.group){//crea combinaciones posibles
                    String temp = "";
                    if (this.known[0][1]==1){
                        temp = String.valueOf(this.known[1][0])+ this.known[2][0] + this.known[3][0];
                        this.guess = String.valueOf(this.known[0][0]);
                    }else{
                        temp = String.valueOf(this.known[0][0]) + this.known[1][0] + this.known[2][0] + this.known[3][0];
                    }
                    this.permutations=this.allPermutation(temp);
                    this.group=true;
                }

                this.guess+=this.permutations.remove(this.random.nextInt(this.permutations.size()));


            }
            return this.guess;
        }
        return "WAITING VALID STATE";
    }

    private ArrayList<String> allPermutation(String str) { //genera todas las permutaciones de la fase 2
        if (str.length()==0){
            ArrayList<String> baseResult= new ArrayList<>();
            baseResult.add("");
            return baseResult;
        }
        char ch = str.charAt(0);
        String rest = str.substring(1);
        ArrayList<String> recResult = allPermutation(rest);
        ArrayList<String> myResult = new ArrayList<>();
        for (int i = 0; i < recResult.size(); i++) {
            String s = recResult.get(i);
            for (int j = 0; j <= s.length(); j++) {
                String newString = s.substring(0, j) + ch + s.substring(j);
                myResult.add(newString);
            }
        }
        return myResult;
    }

}
