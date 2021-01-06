package N;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import static N.Nodo.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests hechos para el problema de la tarea 3
 */
public class Test1 {
    String expresion;
    double mutacion_prob;
    int goal;
    int tamanoPoblacion;
    int h_m_ax;
    int n_tournament;
    int n_hijos_gen;
    int ult_generacion;
    int res = 0;
    int generacion = 1;


    @Test
    public void Test11() {
        expresion = "1 2 3 4 5 6 7 8 9";
        mutacion_prob = 0.5;
        goal = 7;
        tamanoPoblacion = 10;
        h_m_ax = 6;
        n_tournament = 4;
        n_hijos_gen = 1000;

        String[] numero = expresion.split(" ");
        int[] N_arr = new int[numero.length];
        for (int i = 0; i < numero.length; i++) {
            N_arr[i] = Integer.parseInt(numero[i]);
        }
        List<Nodo> nodos = generate_Poblacion(tamanoPoblacion, N_arr, h_m_ax);
        while (res != goal && generacion < ult_generacion) {
            for (int k = 0; k < n_hijos_gen; k++) {
                int randomNum1 = ThreadLocalRandom.current().nextInt(1, tamanoPoblacion);
                nodos.remove(randomNum1);
                Nodo Hijo = tournamet_selection(nodos, goal, n_tournament);
                nodos.add(Hijo);
            }
            for (int t = 0; t < tamanoPoblacion; t++) {
                int fit_c_u = fitness(nodos.get(t), goal);
                if (fit_c_u == 0) {
                    String e = print(nodos.get(t));
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
