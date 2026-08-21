package com.serfagab.util;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.serfagab.entities.DetalleOrdenCompra;
import com.serfagab.entities.OrdenCompra;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrdenCompraPdf {

    private static final Color AZUL_OSCURO = new Color(26, 26, 46);
    private static final Color GRIS_CLARO = new Color(242, 242, 242);

    private static final Font FUENTE_TITULO = new Font(Font.HELVETICA, 18, Font.BOLD, AZUL_OSCURO);
    private static final Font FUENTE_SUBTITULO = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.GRAY);
    private static final Font FUENTE_ETIQUETA = new Font(Font.HELVETICA, 10, Font.BOLD, Color.DARK_GRAY);
    private static final Font FUENTE_TEXTO = new Font(Font.HELVETICA, 10, Font.NORMAL);
    private static final Font FUENTE_TABLA_CABECERA = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static byte[] generar(OrdenCompra orden) {
        try (ByteArrayOutputStream salida = new ByteArrayOutputStream()) {
            Document documento = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(documento, salida);
            documento.open();

            Paragraph titulo = new Paragraph("SERFAGAB", FUENTE_TITULO);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);

            Paragraph subtitulo = new Paragraph("Sistema de Gestión de Compras", FUENTE_SUBTITULO);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(subtitulo);

            Paragraph numeroOrden = new Paragraph("ORDEN DE COMPRA N° " + orden.getIdOrdenCompra(),
                    new Font(Font.HELVETICA, 14, Font.BOLD));
            numeroOrden.setAlignment(Element.ALIGN_CENTER);
            numeroOrden.setSpacingBefore(12);
            numeroOrden.setSpacingAfter(12);
            documento.add(numeroOrden);

            PdfPTable tablaInfo = new PdfPTable(2);
            tablaInfo.setWidthPercentage(100);
            tablaInfo.setWidths(new float[]{0.35f, 0.65f});
            agregarFilaInfo(tablaInfo, "Fecha de emisión:", formatearFecha(orden.getFecha()));
            agregarFilaInfo(tablaInfo, "Estado:", orden.getEstado() == null ? "-" : orden.getEstado());
            agregarFilaInfo(tablaInfo, "Proveedor:",
                    orden.getProveedor() == null ? "-" : nvl(orden.getProveedor().getRazonSocial()));
            agregarFilaInfo(tablaInfo, "RUC:",
                    orden.getProveedor() == null ? "-" : nvl(orden.getProveedor().getRuc()));
            agregarFilaInfo(tablaInfo, "Celular:",
                    orden.getProveedor() == null ? "-" : nvl(orden.getProveedor().getCelular()));
            agregarFilaInfo(tablaInfo, "Email:",
                    orden.getProveedor() == null ? "-" : nvl(orden.getProveedor().getEmail()));
            agregarFilaInfo(tablaInfo, "Registrado por:",
                    orden.getUsuario() == null ? "-"
                            : nvl(orden.getUsuario().getNombres()) + " " + nvl(orden.getUsuario().getApellidos()));
            agregarFilaInfo(tablaInfo, "Observaciones:",
                    orden.getObservaciones() == null || orden.getObservaciones().isBlank()
                            ? "-" : orden.getObservaciones());
            documento.add(tablaInfo);

            documento.add(new Paragraph(" ", FUENTE_TEXTO));

            PdfPTable tablaDetalles = new PdfPTable(5);
            tablaDetalles.setWidthPercentage(100);
            tablaDetalles.setWidths(new float[]{2.2f, 1.0f, 0.9f, 1.2f, 1.2f});
            agregarCeldaCabecera(tablaDetalles, "Material");
            agregarCeldaCabecera(tablaDetalles, "Unidad");
            agregarCeldaCabecera(tablaDetalles, "Cantidad");
            agregarCeldaCabecera(tablaDetalles, "Precio Unit. (S/)");
            agregarCeldaCabecera(tablaDetalles, "Subtotal (S/)");

            if (orden.getDetalles() != null) {
                for (DetalleOrdenCompra detalle : orden.getDetalles()) {
                    agregarCeldaTexto(tablaDetalles,
                            detalle.getMaterial() == null ? "-" : nvl(detalle.getMaterial().getNombre()));
                    agregarCeldaTexto(tablaDetalles,
                            detalle.getMaterial() == null ? "-" : nvl(detalle.getMaterial().getUnidadMedida()));
                    agregarCeldaTexto(tablaDetalles, String.valueOf(detalle.getCantidad()));
                    agregarCeldaTexto(tablaDetalles, String.format("%.2f", detalle.getPrecioUnitario()));
                    agregarCeldaTexto(tablaDetalles, String.format("%.2f", detalle.getSubtotal()));
                }
            }
            documento.add(tablaDetalles);

            Paragraph total = new Paragraph(
                    "TOTAL: S/ " + String.format("%.2f", orden.getTotal() == null ? 0.0 : orden.getTotal()),
                    new Font(Font.HELVETICA, 12, Font.BOLD, AZUL_OSCURO));
            total.setAlignment(Element.ALIGN_RIGHT);
            total.setSpacingBefore(10);
            documento.add(total);

            Paragraph pie = new Paragraph(
                    "Documento generado el "
                            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    FUENTE_SUBTITULO);
            pie.setAlignment(Element.ALIGN_CENTER);
            pie.setSpacingBefore(30);
            documento.add(pie);

            documento.close();
            return salida.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Error al generar el PDF de la orden de compra", e);
        }
    }

    private static void agregarFilaInfo(PdfPTable tabla, String etiqueta, String valor) {
        PdfPCell celdaEtiqueta = new PdfPCell(new Phrase(etiqueta, FUENTE_ETIQUETA));
        celdaEtiqueta.setBackgroundColor(GRIS_CLARO);
        celdaEtiqueta.setBorderWidth(0.5f);
        celdaEtiqueta.setPadding(6);
        tabla.addCell(celdaEtiqueta);

        PdfPCell celdaValor = new PdfPCell(new Phrase(valor, FUENTE_TEXTO));
        celdaValor.setBorderWidth(0.5f);
        celdaValor.setPadding(6);
        tabla.addCell(celdaValor);
    }

    private static void agregarCeldaCabecera(PdfPTable tabla, String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, FUENTE_TABLA_CABECERA));
        celda.setBackgroundColor(AZUL_OSCURO);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(6);
        tabla.addCell(celda);
    }

    private static void agregarCeldaTexto(PdfPTable tabla, String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, FUENTE_TEXTO));
        celda.setPadding(6);
        tabla.addCell(celda);
    }

    private static String formatearFecha(java.time.LocalDate fecha) {
        return fecha == null ? "-" : fecha.format(FORMATO_FECHA);
    }

    private static String nvl(String texto) {
        return texto == null ? "-" : texto;
    }
}
