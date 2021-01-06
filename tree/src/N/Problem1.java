package N;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static N.Nodo.*;

public class Problem1 {
    private static List<Integer> worstFitness;
    static int[] expresion = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    static double mutacion_prob = 0.5;
    static int goal = 7;
    static int tamanoPoblacion = 10;
    static int h_m_ax = 6;
    static int n_tournament = 4;
    static int n_hijos_gen = 1000;
    static int res = 0;
    static int generacion = 1;
    private static List<Integer> bestFitness;
    private static List<Double> averageFitness;
    private static List<Integer> iterations;

    public static void MainP1() {
        List<Nodo> nodos = generate_Poblacion(tamanoPoblacion, expresion, h_m_ax);
        while (true) {
            for (int k = 0; k < n_hijos_gen; k++) {
                int randomNum1 = ThreadLocalRandom.current().nextInt(1, tamanoPoblacion);
                Nodo padre1 = tournamet_selection(nodos, goal, n_tournament);
                Nodo padre2 = tournamet_selection(nodos, goal, n_tournament);
                Nodo hijo = crossOver(padre1,padre2);
                hijo = mutation(hijo,mutacion_prob,expresion);
                nodos.remove(randomNum1);
                nodos.add(hijo);
            }
            int worst = 0;
            int best = Integer.MAX_VALUE;
            List<Integer> tAv = null;
            for (int t = 0; t < tamanoPoblacion; t++) {
                int fit_c_u = fitness(nodos.get(t), goal);
                tAv.add(fit_c_u);
                worst = Math.max(worst,fit_c_u);
                best = Math.min(best,fit_c_u);
            }
            double sum = 0;
            for (int j = 0; j < tAv.size();j++){
                sum+= tAv.get(j);
            }
            double av = sum / tAv.size();
            averageFitness.add(av);
            worstFitness.add(worst);
            bestFitness.add(best);
            generacion++;
            iterations.add(generacion);
            if (best == goal){
                break;
            }
        }
    }

    public static List<Integer> getBestFitness (){
        return bestFitness;
    }

    public static List<Integer> getWorstFitness(){
        return worstFitness;
    }

    public static List<Double> getAverageFitness (){
        return averageFitness;
    }

    public static List<Integer> getIterations(){
        return iterations;
    }

    public static void main (String args[]) {
        MainP1();
        getWorstFitness();
    }
}
