import java.security.KeyPair;
import java.sql.Array;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;

public class Nodo {
    String valor;
    Nodo izq;
    Nodo der;

    public Nodo (String valor) {
        this.valor = valor;
    }

    public void setizq(Nodo izq){
        this.izq = izq;
    }

    public void setder(Nodo der){
        this.der = der;
    }

    public static Nodo getDerInt(Nodo nodo, int piso){
        if (piso>1){
            return getDerInt(nodo.der,piso-1);
        }
        else{
            return nodo;
        }
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

    public static void preevaluate(Nodo nodo){
        if (nodo.valor.equals("*") || nodo.valor.equals("/")){
            if (nodo.der.valor.equals("*") || nodo.der.valor.equals("/") ||
                    nodo.der.valor.equals("+") || nodo.der.valor.equals("-")) {
                int hijoI = Integer.parseInt(nodo.izq.valor);//5
                int hijoDI = Integer.parseInt(nodo.der.izq.valor);//6
                if (nodo.valor.equals("*")) {
                    nodo.izq.valor = "" + (hijoI * hijoDI); //30
                }
                if (nodo.valor.equals("/")) {
                    nodo.izq.valor = "" + (hijoI / hijoDI); //30
                }
                nodo.valor = nodo.der.valor;
                nodo.der = nodo.der.der;
                if (nodo.valor.equals("+") || nodo.valor.equals("-")) {
                    preevaluate(nodo.der);
                }
                if (nodo.valor.equals("*") || nodo.valor.equals("/")) {
                    if (nodo.der.valor.equals("*") || nodo.der.valor.equals("/") ||
                            nodo.der.valor.equals("+") || nodo.der.valor.equals("-")) {
                        preevaluate(nodo);
                    }
                }
            }
        }
        if (nodo.valor.equals("+") || nodo.valor.equals("-")){
            if (nodo.der.valor.equals("*") || nodo.der.valor.equals("/") ||
                    nodo.der.valor.equals("+") || nodo.der.valor.equals("-")) {
                preevaluate(nodo.der);
            }
        }
    }

    public static int evaluate2(Nodo nodo){
        String r = "+-*/";
        if(r.indexOf(nodo.valor)!=-1) {
            if (nodo.valor.equals("+")) {
                return evaluate2(nodo.izq) + evaluate2(nodo.der);
            }
            if (nodo.valor.equals("-")) {
                return evaluate2(nodo.izq) - evaluate2(nodo.der);
            }
            if (nodo.valor.equals("*")) {
                return evaluate2(nodo.izq) * evaluate2(nodo.der);
            }
            if (nodo.valor.equals("/")) {
                return evaluate2(nodo.izq) / evaluate2(nodo.der);
            }
        }
        else{
            return Integer.parseInt(nodo.valor);
        }
        return 0;
    }

    public static int evaluate(Nodo nodo){
        preevaluate(nodo);
        int result = evaluate2(nodo);
        return result;
    }

    /**
     Copy:: nodo goal --> int
     de a cuerdo a un nodo, se evalua su valor y se retorna
     la diferencia entre la meta y el valor del nodo
     **/
    public static Nodo copy(Nodo nodo){
        Nodo copia = new Nodo(nodo.valor);
        copia.setder(nodo.der);
        copia.setizq(nodo.izq);
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

    //Creo que esta ya esta bien
    public static List<Nodo> generate_Poblacion(int tamaño_poblacion, int[] numeros, int h_max){
        List<Nodo> population = new ArrayList<>();
        String[] operaciones = {"+", "-", "/", "*"};
        int individuo_numero = 0;
        while(individuo_numero<tamaño_poblacion){
            String individual = "";
            Random rand = new Random();
            int randomNum = ThreadLocalRandom.current().nextInt(1, h_max);
            if(randomNum == 1){
                int randomm = rand.nextInt(numeros.length);
                individual += Integer.toString(numeros[randomm]);
                int op = rand.nextInt(4);
                individual += operaciones[op];
                randomm = rand.nextInt(numeros.length);
                individual += Integer.toString(numeros[randomm]);
                Nodo individual1 = arbol(individual);
                population.add(individual1);
            }

            else {
                for (int i = 0; i < randomNum; i++) {
                    if (i != randomNum - 1) {
                        int randomm = rand.nextInt(numeros.length);
                        individual += Integer.toString(numeros[randomm]);
                        int op = rand.nextInt(4);
                        individual += operaciones[op];
                    } else {
                        int randomm = rand.nextInt(numeros.length);
                        individual += Integer.toString(numeros[randomm]);

                    }
                    Nodo individual1 = arbol(individual);
                    population.add(individual1);
                }
            }
            ++individuo_numero;
        }
        return population;
    }

    public static int altura(Nodo nodo){
        if(nodo == null)return 0;
        return 1 + altura(nodo.der);
    }

    /**
    fitness:: nodo goal --> int
     de a cuerdo a un nodo, se evalua su valor y se retorna
     la diferencia entre la meta y el valor del nodo
    **/
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

    public static Nodo crossOver(Nodo a, Nodo b){
        double option = Math.random();
        String s = "";
        if(option>0.5){
            s+=a.valor;
        }
        else{
            s+=b.valor;
        }
        Nodo hijo = new Nodo(s);
        if(option>0.5){
            hijo.setizq(a.izq);
        }
        else{
            hijo.setizq(b.izq);
        }
        double opcion2 = Math.random();
        Random random = new Random();
        if(opcion2<0.5){
            int h = altura(a.der);
            int randomNum = ThreadLocalRandom.current().nextInt(1, h);
            hijo.setder(getDerInt(a.der,randomNum));
        }
        else{
            int h = altura(b.der);
            int randomNum = ThreadLocalRandom.current().nextInt(1, h);
            hijo.setder(getDerInt(b.der,randomNum));
        }
        return hijo;
    }

    public void cambiarder(Nodo a, int piso){
        if(piso>1){
            cambiarder(a, piso-1);
        }
        else{
            this.setder(a);
        }
    }

    public static Nodo mutation(Nodo a, int probabilidadMutar){
        double option = Math.random();
        if (option <= probabilidadMutar){
            int h = altura(a.der);
            int randomNum = ThreadLocalRandom.current().nextInt(1, h);
            Nodo mutante = copy(a);
            List<Nodo> lista = generate_Poblacion(1,new int[]{1, 2, 3, 4,5,6,7,8,9,10});
            mutante.cambiarder(lista.get(0),randomNum);
            return mutante;
        }
        else{
            return a;
        }
    }

    public static void main (String args[]) {
        System.out.println("Ingrese expresion");
        Scanner sc = new Scanner (System.in);
        String expresion = sc.nextLine();
        sc.close();
        List<Nodo> nodos = generate_Poblacion(20, new int[]{1, 2, 3, 4,5,6,7,8,9,10}, 6);
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