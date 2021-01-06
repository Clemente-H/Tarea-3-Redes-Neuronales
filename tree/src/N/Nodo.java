package N;

import java.security.KeyPair;
import java.sql.Array;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;

/**
 * Clase Nodo hecha para manipular árboles binarios
 */
public class Nodo {
    String valor;
    Nodo izq;
    Nodo der;

    /**
     * Constructor de la clase nodo
     *
     * @param valor
     *        valor del nodo
     */
    public Nodo (String valor) {
        this.valor = valor;
    }

    /**
     * Agrega un nodo a la izquierda de otro nodo
     *
     * @param izq
     *        nodo que será agregado
     */
    public void setizq(Nodo izq){
        this.izq = izq;
    }

    /**
     * Agrega un nodo a la derecha de otro nodo
     *
     * @param der
     *        nodo que será agregado
     */
    public void setder(Nodo der){
        this.der = der;
    }

    /**
     * @param nodo
     * @param piso
     * @return
     */
    public static Nodo getDerInt(Nodo nodo, int piso){
        if (piso>1){
            return getDerInt(nodo.der,piso-1);
        }
        else{
            return nodo;
        }
    }

    /**
     * Construye un arbol binario a partir de una expresion
     *
     * @param s
     *        String s a partir del cual se va a construir el arbol
     *
     * @return un arbol binario (referencia al nodo raiz)
     */
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

    /**
     * Preevalua un arbol, simplificandolo hasta que solo quede un arbol con sumas y/o restas
     *
     * @param nodo
     *        referencia al nodo raiz del arbol
     */
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


    /**
     * Evalua un arbol binario con las operaciones {+,-}
     *
     * @param nodo
     *        referencia al nodo raiz del arbol
     *
     * @return el resultado de evaluar la expresion del arbol
     */
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

    /**
     * Evalua un arbol binario con las operaciones {*,/,+,-}
     *
     * @param nodo
     *        referencia al nodo raiz del arbol
     *
     * @return el resultado de evaluar la expresion del arbol
     */
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


    /**
     * Entrega un String que representa a un árbol.
     *
     * @param nodo
     *        referencia al nodo raiz del arbol
     *
     * @return expresión representativa del arbol
     */
    public static String print(Nodo nodo){
        if(nodo.izq == null){
            return nodo.valor;
        }
        else{
            return print(nodo.izq) + nodo.valor + print(nodo.der);
        }
    }

    /**
     * @param tamaño_poblacion
     *        el
     * @param numeros
     *        los
     * @param h_max
     *        la
     *
     * @return
     */
    public static List<Nodo> generate_Poblacion(int tamano_poblacion, int[] numeros, int h_max){
        List<Nodo> population = new ArrayList<>();
        String[] operaciones = {"+", "-", "/", "*"};
        int individuo_numero = 0;
        while(individuo_numero<tamano_poblacion){
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

    /**
     * Calcula la altura de un arbol
     *
     * @param nodo
     *        referencia al nodo raiz del arbol
     *
     * @return la altura del arbol
     */
    public static int altura(Nodo nodo){
        if(nodo == null)return 0;
        return 1 + altura(nodo.der);
    }

/**
 fitness:: nodo goal --> int
 de a cuerdo a un nodo, se evalua su valor y se retorna
 la diferencia entre la meta y el valor del nodo
 **/
    /**
     * @param genoma
     * @param goal
     * @return
     */
    public static int fitness(Nodo genoma, int goal){
        int fit = evaluate(genoma);
        return abs(fit - goal);
    }

    /**
     * @param population
     * @param goal
     * @param n_individual
     * @return
     */
    //
    public static Nodo tournamet_selection(List<Nodo> population, int goal, int n_individual){
        Random rand =new Random();
        List<Nodo> potenciales_individual = population;
        HashMap<Integer,Nodo> fit_individual = new HashMap<>();
        List<Integer> llaves = new ArrayList<>();
        for(int i = 0; i<n_individual;i++) {
            int j = rand.nextInt(potenciales_individual.size());
            int fit = fitness(potenciales_individual.get(j),goal);
            llaves.add(fit);
            fit_individual.put(fit,potenciales_individual.get(j));
            potenciales_individual.remove(j);
        }
        Collections.sort(llaves);
        Nodo vuelta =fit_individual.get(llaves.get(0));
        return vuelta;
    }

    /**
     * @param a
     * @param b
     * @return
     */
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

    /**
     *
     *
     * @param nodo
     * @param piso
     */
    public void cambiarder(Nodo nodo, int piso){
        if(piso>1){
            cambiarder(nodo, piso-1);
        }
        else{
            this.setder(nodo);
        }
    }

    /**
     * @param a
     * @param probabilidadMutar
     * @param numeros
     * @return
     */
    public static Nodo mutation(Nodo a, int probabilidadMutar,int[] numeros){
        double option = Math.random();
        if (option <= probabilidadMutar){
            int h = altura(a.der);
            int randomNum = ThreadLocalRandom.current().nextInt(1, h);
            Nodo mutante = copy(a);
            List<Nodo> lista = generate_Poblacion(1,numeros,randomNum);
            mutante.cambiarder(lista.get(0),randomNum);
            return mutante;
        }
        else{
            return a;
        }
    }

    public static void main(String args[]) {
        System.out.println("Ingrese numeros a usar");
        Scanner sc = new Scanner (System.in);
        String expresion = sc.nextLine();

        System.out.println("Ingrese probabilidad de mutacion");
        String mutancion_prob = sc.nextLine();

        System.out.println("Ingrese meta");
        String meta = sc.nextLine();

        System.out.println("Ingrese numero individuos poblacion");
        String numero_individuos = sc.nextLine();

        System.out.println("Ingrese altura maxima Arbol");
        String hmax = sc.nextLine();

        System.out.println("Ingrese individuos para Tournament Selection");
        String tournament_n = sc.nextLine();

        System.out.println("numero maximo de generaciones");
        String n_max_g= sc.nextLine();

        System.out.println("numero hijos por generacion");
        String hijos_x_gen= sc.nextLine();


        sc.close();
        int Tamaño_poblacion = Integer.parseInt(numero_individuos);

        String[] numero = expresion.split(" ");
        int[] N_arr = new int[numero.length];
        for(int o = 0; o <numero.length;o++){
            N_arr[o]=Integer.parseInt(numero[o]);
        }
         int h_m_ax = Integer.parseInt(hmax);

        List<Nodo> nodos = generate_Poblacion(Tamaño_poblacion, N_arr, h_m_ax);

        int goal = Integer.parseInt(meta);

        int n_tournament = Integer.parseInt(tournament_n);

        int ult_generaion = Integer.parseInt(n_max_g);

        int res=0;
        int generacion = 1;

        int n_hijos_gen = Integer.parseInt(hijos_x_gen);


        while( res!=goal && generacion<ult_generaion){

            for(int k = 0; k<n_hijos_gen;k++){
                int randomNum1 = ThreadLocalRandom.current().nextInt(1, Tamaño_poblacion);
                nodos.remove(randomNum1);
                Nodo Hijo = tournamet_selection(nodos,goal,n_tournament);
                nodos.add(Hijo);
            }
            for(int t = 0; t<Tamaño_poblacion;t++){
                int fit_c_u = fitness(nodos.get(t),goal);
                if(fit_c_u == 0){
                    String e  = print(nodos.get(t));
                    System.out.print(e);
                    System.out.print("\n");
                    String n = "encontrado en la generacion" + generacion;
                    System.out.print(n);
                    break;
                }
            }
            generacion++;

        }
    }
}