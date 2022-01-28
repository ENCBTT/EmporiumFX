package resources.reports;

import javafx.collections.ObservableList;
import modelo.MPedidos;
import modelo.MPedidos_detalle;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reportes {
    private String reporte;

    public Reportes (String reporte){
        this.reporte = reporte;
    }

    public void generarReporte(Connection connection){
        try {
            Map paramentros = new HashMap();
            //ingresar los parametros
            JasperReport report = (JasperReport) JRLoader.loadObject(getClass().getResource(this.reporte+".jasper"));
            JasperPrint jp = JasperFillManager.fillReport(report, null, connection);

            JasperViewer.viewReport(jp, false);

        }catch(HeadlessException | NumberFormatException | JRException ex){
            System.out.println(ex.getMessage());
            System.out.println("Error al generar reporte");

        }
    }

    public void reportePedidos(MPedidos pedido, List<MPedidos_detalle> lista){
        try {

            Map parametros = new HashMap();
            parametros.put("fechaHora", pedido.getFecMun() +" - "+ pedido.getHorMun());
            parametros.put("mesa", pedido.getMesas());
            parametros.put("delivery", pedido.getDelivery());
            parametros.put("numero", pedido.getNumMun());
            parametros.put("total", pedido.getTotalMun());
            parametros.put("funcionario", pedido.getFuncionario());
            JasperReport report = (JasperReport) JRLoader.loadObject(getClass().getResource(this.reporte+".jasper"));
            JasperPrint jp = JasperFillManager.fillReport(report, parametros, new JRBeanCollectionDataSource(lista));
            JasperViewer.viewReport(jp, false);

        }catch(HeadlessException | NumberFormatException | JRException ex){
            System.out.println(ex.getMessage());
            System.out.println("Error al generar reporte");

        }
    }

}
