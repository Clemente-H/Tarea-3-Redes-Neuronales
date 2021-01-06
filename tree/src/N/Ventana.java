package N;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


import javax.swing.*;

import java.util.List;

import static N.Problem1.*;


public class Ventana extends JFrame{
    JPanel panel;
    public Ventana(){
        setTitle("Como Hacer Graficos con Java");
        setSize(800,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        init();
    }

    private void init() {
        panel = new JPanel();
        getContentPane().add(panel);
        // Fuente de Datos
        DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
        MainP1();
        for (int i = 0; i < getIterations().size();i++){
            line_chart_dataset.addValue(getBestFitness().get(i), "best", getIterations().get(i));
            line_chart_dataset.addValue(getWorstFitness().get(i), "worst", getIterations().get(i));
            line_chart_dataset.addValue(getAverageFitness().get(i), "average", getIterations().get(i));
        }
        // Creando el Grafico
        JFreeChart chart=ChartFactory.createLineChart("Fitness vs generación",
                "Generación","Fitness",line_chart_dataset, PlotOrientation.VERTICAL,
                true,true,false);

        // Mostrar Grafico
        ChartPanel chartPanel = new ChartPanel(chart);
        panel.add(chartPanel);
    }

    public static void main(String args[]){
        new Ventana().setVisible(true);
    }
}
