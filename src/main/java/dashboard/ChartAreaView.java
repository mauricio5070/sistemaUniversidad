package dashboard;

import gestion.EstudianteGestion;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import model.YearGender;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.LineChartModel;

@Named(value = "chartAreaView")
@Dependent
public class ChartAreaView {

    private LineChartModel areaModel;

    public ChartAreaView() {
    }

    public LineChartModel getAreaModel() {
        return areaModel;
    }

    @PostConstruct
    public void init() {
        createAreaModel();
    }

    private void createAreaModel() {
        areaModel = new LineChartModel();

//        LineChartSeries boys = new LineChartSeries();
//        boys.setFill(true);
//        boys.setLabel("Boys");
//        boys.set("2004", 120);
//        boys.set("2005", 100);
//        boys.set("2006", 44);
//        boys.set("2007", 150);
//        boys.set("2008", 25);
//        boys.set("2021", 700);
//
//        LineChartSeries girls = new LineChartSeries();
//        girls.setFill(true);
//        girls.setLabel("Girls");
//        girls.set("2004", 52);
//        girls.set("2005", 60);
//        girls.set("2006", 110);
//        girls.set("2007", 90);
//        girls.set("2008", 120);
//        girls.set("2021", 700);

        /*Variables para labels*/
        String label1 = "";
        String label2 = "";

        /*Initialization of Grafic*/
        LineChartSeries hombres = new LineChartSeries();
        hombres.setFill(true);
        LineChartSeries mujeres = new LineChartSeries();
        mujeres.setFill(true);

        /*Call DataBase*/
        ArrayList<YearGender> datos = EstudianteGestion.getIngresoYearGender();
        int mayor = datos.get(0).getTotal();

        /*Create a new list to get the gender*/
        ArrayList<String> lista = new ArrayList<>();
        /*Iterate existing list to add the elements to the new list*/
        datos.forEach(linea -> {
            lista.add(linea.getGenero());
        });
        /*Get collection without duplicate i.e. distinct only*/
        List<String> distinctElements = lista.stream()
                .distinct()
                .collect(Collectors.toList());

        /*Assign labels*/
        for (String value : distinctElements) {
            if (value.equalsIgnoreCase("M")) {
                label1 = "Masculino";

            }
            if (value.equalsIgnoreCase("F")) {
                label2 = "Femenino";

            }
        }

        hombres.setLabel(label1);
        mujeres.setLabel(label2);

        for (YearGender linea : datos) {
            if (linea.getGenero().equalsIgnoreCase("M")) {
                hombres.set(linea.getYear(), linea.getTotal());

            } else {
                mujeres.set(linea.getYear(), linea.getTotal());
            }
            if (mayor < linea.getTotal()) {
                mayor = linea.getTotal();
            }
        }

        /*Remove Duplicates in a list*/
//        List<YearGender> personListFiltered = datos.stream()
//                .filter(distinctByKey(p -> p.getName()))
//                .collect(Collectors.toList());
        areaModel.addSeries(hombres);
        areaModel.addSeries(mujeres);

        areaModel.setTitle("Ingreso Year Gender");
        areaModel.setLegendPosition("ne");
        areaModel.setStacked(true);
        areaModel.setShowPointLabels(true);

        Axis xAxis = new CategoryAxis("Total");
        areaModel.getAxes().put(AxisType.X, xAxis);
        Axis yAxis = areaModel.getAxis(AxisType.Y);
        yAxis.setLabel("Births");
        yAxis.setMin(0);
//        yAxis.setMax(300);
    }
}
