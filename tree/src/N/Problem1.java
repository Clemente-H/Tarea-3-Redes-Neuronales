package N;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static N.Nodo.*;

/**
 * Clase hecha para probar la estructura creada en el problema 1 de la tarea
 */
public class Problem1 {
    List<Integer> worstFitness;
    static int[] expresion = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    static double mutacion_prob = 0.5;
    static int goal = 7;
    static int tamanoPoblacion = 10;
    static int h_m_ax = 6;
    static int n_tournament = 4;
    static int n_hijos_gen = 1000;
    static int res = 0;
    static int generacion = 1;
    List<Integer> bestFitness;
    List<Double> averageFitness;
    List<Integer> iterations;

    /**
     * Genera las listas de las
     */
    public void MainP1() {
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

    /**
     * Entrega la lista con los mejores fitness de cada iteración hasta alcanzar la meta
     *
     * @return Lista de los mejores fitness
     */
    public List<Integer> getBestFitness (){
        return bestFitness;
    }

    /**
     * Entrega la lista con los peores fitness de cada iteración hasta alcanzar la meta
     *
     * @return Lista de los peores fitness
     */
    public List<Integer> getWorstFitness(){
        return worstFitness;
    }

    /**
     * Entrega la lista con los fitness promedio de cada iteracioón hasta alcanzar la meta
     *
     * @return Lista de los fitness promedio
     */
    public List<Double> getAverageFitness (){
        return averageFitness;
    }

    /**
     * Entrega la lista con las iteraciones realizadas hasta alcanzar la meta
     *
     * @return Lista de las iteraciones de la 1 hasta la final
     */
    public List<Integer> getIterations(){
        return iterations;
    }

}
