package uqvirutal.edu.co.cinemauq.model;

public class ExportadorTxt implements Exportador {

    @Override
    public void exportar(String datos, String rutaArchivo) {
        System.out.println("=========================================");
        System.out.println(" EXPORTANDO REPORTE A TXT (Simulacion)");
        System.out.println(" Destino: " + rutaArchivo + ".txt");
        System.out.println("=========================================");
        System.out.println(datos);
        System.out.println("=========================================");
        System.out.println("¡Reporte exportado con exito!");
    }
}
