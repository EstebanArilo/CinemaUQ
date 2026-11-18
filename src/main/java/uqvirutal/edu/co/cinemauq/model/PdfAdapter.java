package uqvirutal.edu.co.cinemauq.model;

public class PdfAdapter implements Exportador{
    private LibreriaPdfExterna libreriaPdfExterna;

    public PdfAdapter() {
        this.libreriaPdfExterna = new LibreriaPdfExterna();
    }

    @Override
    public void exportar(String datos, String rutaArchivo) {
        libreriaPdfExterna.generarDocumentoPdf(datos, rutaArchivo);
    }
}