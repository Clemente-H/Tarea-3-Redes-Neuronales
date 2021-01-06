package N;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


import javax.swing.*;


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
        line_chart_dataset.addValue(80, "visitas", "Julio");
        line_chart_dataset.addValue(300, "visitas", "Agosto");
        line_chart_dataset.addValue(600, "visitas", "Septiembre");
        line_chart_dataset.addValue(1200, "visitas", "Octubre");
        line_chart_dataset.addValue(2400, "visitas", "Noviembre");

        // Creando el Grafico
        JFreeChart chart=ChartFactory.createLineChart("Trafico en el Blog",
                "Mes","Visitas",line_chart_dataset, PlotOrientation.VERTICAL,
                true,true,false);

        // Mostrar Grafico
        ChartPanel chartPanel = new ChartPanel(chart);
        panel.add(chartPanel);
    }

    public static void main(String args[]){
        new Ventana().setVisible(true);
    }
}
