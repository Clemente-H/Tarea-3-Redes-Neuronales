import java.security.KeyPair;
import java.sql.Array;
import java.util.*;

import static java.lang.Math.abs;

public class Nodo {
    String valor;
    Nodo izq;
    Nodo der;

    public Nodo (String valor) {
        this.valor = valor;
    }

    public static Nodo arbol( String s) {
        String[] p = s.split(" ");
        if (p.length == 1) {
            Nodo end = new Nodo(p[0]);
            return end;
        } else {
            String izq = "";
            String der = "";
            int i = 0;
            while (true) {
                if (p[i].equals("+") || p[i].equals("-") || p[i].equals("*") || p[i].equals("/")) {
                    break;
                } else {
                    i++;
                }
            }
            for (int j = 0; j < i; j++) {
                izq += p[j] + " " ;
            }
            for (int j = i + 1; j < p.length; j++) {
                der += p[j] + " ";
            }
            Nodo pa = new Nodo(p[i]);
            pa.izq = arbol(izq);
            pa.der = arbol(der);
            return pa;
        }
    }
    //el de arriba esta bueno
    //el de abajo no


    //evaluar(nodo)->int
    //mediante una recursion comprueba el valor de cada nodo
    //para luego evaluarlo
    public static int evaluate(Nodo nodo){
        String r = "+-*/";
        if(nodo.valor.equals("+")){
            return evaluate(nodo.izq) + evaluate(nodo.der);
        }
        if(nodo.valor.equals("-")){
            return evaluate(nodo.izq) - evaluate(nodo.der);

        }
        if(nodo.valor.equals("*")){
            return evaluate(nodo.izq) + evaluate(nodo.der);
        }
        if(nodo.valor.equals("/")){
            return evaluate(nodo.izq) / evaluate(nodo.der);
            }
        else {
            if (!nodo.valor.equals(null)) {
                System.out.print(nodo.valor);
                System.out.print("holiholi");
                int k = Integer.parseInt(nodo.valor);
                return k;
            }
            else{
                return 0;
            }
        }
    }

    public static Nodo copy(Nodo nodo){
        Nodo copia = new Nodo(nodo.valor);
        copia = nodo;
        return copia;
    }

    public static String print(Nodo nodo){
        if(nodo.izq == null){
            return nodo.valor;
        }
        else{
            return print(nodo.izq) + nodo.valor + print(nodo.der);
        }
    }


    public static Nodo derivar(Nodo nodo,String x) {
        String r = "+-/*";
        if(r.indexOf(nodo.valor)!=-1) {
            if (nodo.valor.equals("+")){
                Nodo q = new Nodo(nodo.valor);
                q.izq = derivar(nodo.izq,x);
                q.der = derivar(nodo.der,x);
                return q;
            }
            else if (nodo.valor.equals("-")){
                Nodo q = new Nodo(nodo.valor);
                q.izq = derivar(nodo.izq,x);
                q.der = derivar(nodo.der,x);
                return q;
            }
            else if (nodo.valor.equals("*")){
                Nodo q = new Nodo("+");
                q.izq = new Nodo("*");
                q.der = new Nodo("*");
                q.izq.izq = derivar(nodo.izq,x);
                q.izq.der = nodo.der;
                q.der.izq = nodo.izq;
                q.der.der = derivar(nodo.der,x);
                return q;
            }
            else{
                Nodo q = new Nodo(nodo.valor);
                q.izq = new Nodo("-");
                q.der = new Nodo("*");
                q.izq.izq = new Nodo("*");
                q.izq.der = new Nodo("*");
                q.izq.izq.izq = derivar(nodo.izq,x);
                q.izq.izq.der = nodo.der;
                q.izq.der.izq = nodo.izq;
                q.izq.der.der = derivar(nodo.der, x);
                q.der.izq = nodo.der;
                q.der.der = nodo.der;
                return q;
            }
        }
        else if (nodo.valor.equals(x)){
            return new Nodo("1");
        }
        else {
            return new Nodo("0");
        }
    }



    //Creo que esta ya esta bien
    public static List<Nodo> generate_Poblacion(int tamaño, int[] numeros){
        List<Nodo> population = new ArrayList<>();
        String[] operaciones = {"+", "-", "/", "*"};
        for(int j = 0; j< tamaño; j++){
            Random rand = new Random();
            int tamaño_individual = rand.nextInt(numeros.length);
            String individual = "";
            for(int i = 0; i<tamaño_individual; i++){
                int randomm = rand.nextInt(numeros.length);
                individual += Integer.toString(numeros[randomm]);
                int op = rand.nextInt(4);
                if(i!=tamaño_individual-1){
                    individual += operaciones[op];
                }
            }
            Nodo individual1 = arbol(individual);
            population.add(individual1);
        }
        return population;
    }

    //seems ok to me
    public static int fitness(Nodo genoma, int goal){
        int fit = evaluate(genoma);
        return abs(fit - goal);
    }
    //
    public static List<Nodo> tournamet_selection(List<Nodo> population, int goal, int n_individual){
        Random rand =new Random();
        List<Nodo> potenciales_individual = population;
        HashMap<Integer,Nodo> fit_individual = new HashMap<>();
        List<Integer> llaves = new ArrayList<>();
        for(int i = 0; i<n_individual;i++) {
            int j = rand.nextInt(potenciales_individual.size());
            int fit = fitness(potenciales_individual.get(i),goal);
            llaves.add(fit);
            fit_individual.put(fit,potenciales_individual.get(i));
            potenciales_individual.remove(i);
        }
        Collections.sort(llaves);
        List<Nodo> vuelta =new ArrayList<>();
        vuelta.add(fit_individual.get(llaves.get(0)));
        vuelta.add(fit_individual.get(llaves.get(1)));
        return vuelta;
    }

    public static Nodo crossOver(Nodo a, Nodo b){//se puede mejorar este cross over
        Nodo a2  = copy(a);
        Nodo b2 = copy(b);
        double option = Math.random() * 10;
        if(option<5.0) {
            Nodo vuelta = a2;
            if(!b2.der.equals(null) && !a2.der.equals(null)){
                double optionn = Math.random()*10;
                if(optionn>5){
                    vuelta.der=crossOver(a.der,b.der);
                }
                else{
                    Nodo no = new Nodo(" ");
                    vuelta.der = no;
                }
            }
            return vuelta;
        }
        else{
            Nodo vuelta = b2;
            if(!b2.der.equals(null) && !a2.der.equals(null)){
                double optionn = Math.random()*10;
                if(optionn>5){
                    vuelta.der=crossOver(a.der,b.der);
                }
                else{
                    Nodo no = new Nodo(" ");
                    vuelta.der = no;
                }
            }
            return vuelta;

        }
    }

    public static Nodo mutation(Nodo a){
        Nodo c = copy(a);
        double option = Math.random() * 10;
        if (option<5.0){
            //cambiar
        }
        else{

        }
        return c;
    }

//+Nodo q = arbol(expresion);
    //Nodo q = (derivar(pilaArbol(expresion),variable));
    //String e  = print(q);
    //int j = evaluate(q);
    //System.out.print(j);


    public static void main (String args[]) {
        System.out.println("Ingrese expresion");
        Scanner sc = new Scanner (System.in);
        String expresion = sc.nextLine();
        sc.close();
        List<Nodo> nodos = generate_Poblacion(20, new int[]{1, 2, 3, 4,5,6,7,8,9,10});
        for(int j=0;j<4;j++){
            String e  = print(nodos.get(j));
            System.out.print(e);
            System.out.print("\n");
        }
        List<Nodo> nodos2 = tournamet_selection(nodos,5,3);
        for(int l = 0;l<3;l++){
            String e  = print(nodos.get(l));
            System.out.print(e);
            System.out.print("\n");
        }

    }
}