package com.ISI2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Agent {

    private String guess;//Adivinanza de turno
    private int prevPicas;//memoria de resultado anterior
    private int prevFijas;
    private int picas;//memoria de resultado mas reciente
    private int fijas;
    private boolean stage; //delimita si ya se conocen los 4 digitos del numero
    private int[][] known; //guarda fijas o picas (1, 0) para etapa de los 4 digitos conocidos
    private int equalCounter;//contador de mismos resultados para etapa de obtener digitos
    private int acc;//cuenta numero de digitos conocidos
    private boolean group;//determina si ya se crearon permutaciones de los 4 digitos
    private ArrayList<String> permutations;//lista de permutaciones de los 3 o 4 digitos
    private Random random;
    private boolean midPhase;//etapa para sacar digitos de 6-7-8-9
    private boolean used;//determina si ya se uso el numero para adivinar en la parte de 6-7-8-9

    public Agent(){//inicializa el agente para jugar desde cero
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
        return "WAITING VALID STATE";//espera recibir una entrada valida para actuar
    }
    public String compute(String action, int picas, int fijas){
        if (action.equals("continue")){//juega un turno
            this.prevPicas=this.picas;//actualiza valores historicos
            this.prevFijas=this.fijas;
            this.picas=picas;
            this.fijas=fijas;

            if (!this.stage&&!this.midPhase){//primera parte del juego, determina digitos entre 0-5

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
                    if(this.fijas<this.prevFijas&&this.guess.equals("1789")){//para turno 2, evalua el 0 en posicion 0
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
                    this.guess=this.guess.replace(this.guess.charAt(0), (char) (this.guess.charAt(0)+1));//siguiente adivinanza es (n+1)789
                    if (this.guess.equals("7789")){//si llega aca, hay digitos entre 6-9, pasa a segunda parte
                        this.midPhase=true;
                    }

                }else { //para turno 1

                    this.guess=this.guess.replace(this.guess.charAt(0), (char) (this.guess.charAt(0)+1));
                    if (this.picas>0){this.equalCounter=1;}

                }

            }
            if (!this.stage&&this.midPhase){//segunda parte del juego, determina digitos entre 6-9

                int q = random.nextInt(5);
                q = getQ(q);//obtiene digito que se considera, no esta en el numero

                if(this.used){//avalua resultados de una adivinanza de la segunda parte, si se reduce picas+fijas, se añaden digitos al numero
                    if (this.fijas+this.picas<this.prevFijas+this.prevPicas){
                        int num;
                        if (this.guess.contains("8")){
                            num=7;
                            if (this.fijas+this.picas<this.prevFijas+this.prevPicas-1){
                                addToKnown(num);
                            }
                            checkIfAddSix();
                        }else if (this.guess.contains("9")){
                            this.equalCounter=0;
                            num=8;
                            addToKnown(num);
                            checkIfAddSix();
                        }else{
                            this.equalCounter=0;
                            num=9;
                            addToKnown(num);
                            checkIfAddSix();
                        }
                        System.out.println(Arrays.deepToString(this.known));
                    }
                }

                if (this.guess.contains("7")){//si es 7789, quita los 7 por 2 numeros que se crean que no estan en el numero

                    this.guess = q +this.guess.substring(1);
                    q = random.nextInt(5);
                    q = getQ(q);
                    this.guess = this.guess.substring(0,1)+ q +this.guess.substring(2);
                    this.used=true;

                }else if (this.guess.contains("8")){//se quita el 8 por un numero que se crea que no esta en el numero

                    this.guess = this.guess.substring(0,2)+ q +this.guess.substring(3);
                    this.used=true;

                }else if (this.guess.contains("9")){//se quita el 9 por un numero que se crea que no esta en el numero

                    this.guess = this.guess.substring(0,3)+ q;
                    this.used=true;

                }

            }

            if (this.stage){//tercera parte del juego, determina orden de digitos conocidos
                //para adivinar orden de digitos
                this.guess="";
                if (this.known[0][1]==1){this.guess = String.valueOf(this.known[0][0]);}
                if (!this.group){//crea combinaciones posibles, si hay fija en pos 0, son solo 6, si no, son 24
                    String temp;
                    if (this.known[0][1]==1){
                        temp = String.valueOf(this.known[1][0])+ this.known[2][0] + this.known[3][0];
                        this.guess = String.valueOf(this.known[0][0]);
                    }else{
                        temp = String.valueOf(this.known[0][0]) + this.known[1][0] + this.known[2][0] + this.known[3][0];
                    }
                    this.permutations=this.allPermutation(temp);
                    this.group=true;
                }

                this.guess+=this.permutations.remove(this.random.nextInt(this.permutations.size()));//advina permutacion correcta del numero al azar


            }
            return this.guess;
        }
        return "WAITING VALID STATE";//espera recibir una entrada valida para actuar
    }

    private void checkIfAddSix() {//evalua si debe añadir digitos desde el 6 para atras en la etapa 6-7-8-9
        int i;
        if (this.picas+this.fijas==0){
            for (int j = 0; j<equalCounter; j++){
                i = 1;
                while(i<3&&this.known[i][0]!=-1){
                    i++;
                }
                this.known[i][0]=6-j;
                this.known[i][1]=0;
                this.acc++;
                if (this.acc>=4) { //si entra aca, ya se tienen los digitos
                    this.stage=true;
                    this.midPhase=false;
                    break;
                }
            }
            this.equalCounter=0;
        }
    }

    private void addToKnown(int num) {//Añade digitos en la etapa 6-7-8-9
        System.out.println(num);
        int i;
        i = 1;
        while(i<3&&this.known[i][0]!=-1){
            i++;
        }
        if (this.known[0][0]==-1){i=0;}
        this.known[i][0]=num;
        this.known[i][1]=0;
        this.acc++;
        if(this.acc>=4){ //si entra aca, ya se tienen los digitos
            this.stage=true;
            this.midPhase=false;
        }
    }

    private int getQ( int q) { //obtiene un digito que se cree no esta en el numero
        boolean p = true;
        while(p){
            if (this.guess.contains(String.valueOf(q))){
                q = random.nextInt(5 );
                p =true;
            }else{
                p=false;
            }
            for (int i = 0; i<4; i++){
                if (this.known[i][0]==q){
                    q = random.nextInt(5);
                    p=true;
                    break;
                }else{
                    p=false;
                }
            }
        }
        return q;
    }

    private ArrayList<String> allPermutation(String str) { //genera todas las permutaciones de la fase con digitos conocidos
        if (str.length()==0){
            ArrayList<String> baseResult= new ArrayList<>();
            baseResult.add("");
            return baseResult;
        }
        char ch = str.charAt(0);
        String rest = str.substring(1);
        ArrayList<String> recResult = allPermutation(rest);
        ArrayList<String> myResult = new ArrayList<>();
        for (String s : recResult) {
            for (int j = 0; j <= s.length(); j++) {
                String newString = s.substring(0, j) + ch + s.substring(j);
                myResult.add(newString);
            }
        }
        return myResult;
    }

}
