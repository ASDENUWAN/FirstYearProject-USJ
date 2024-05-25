/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPCell;
import java.io.File;
import java.io.FileOutputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author Sachii
 */
public class PaymentAndReport {
    
    public static void generateSlip(int bid,int cid,int seats,String date,String stime,String etime){
        LocalDate ndate = LocalDate.now();
        int hours = (int) getHours(stime, etime);
        double total = getSubtotal(seats, hours);
        
        JFileChooser dialog = new JFileChooser();
        dialog.setSelectedFile(new File(bid+"_slip.pdf"));
        int dialogRes = dialog.showSaveDialog(null);
        
        if(dialogRes == JFileChooser.APPROVE_OPTION){
            String filePath = dialog.getSelectedFile().getPath();
            try {
                Document myDoc = new Document();
                PdfWriter myWrite = PdfWriter.getInstance(myDoc, new FileOutputStream(filePath));
                
                myDoc.open();
                
                myDoc.add(new Paragraph("Booking Payment Slip",FontFactory.getFont(FontFactory.TIMES_ROMAN,20,Font.BOLD)));
                myDoc.add(new Paragraph("-----------------------------------------------------"));
                myDoc.add(new Paragraph("Booking ID    : "+bid));
                myDoc.add(new Paragraph("Date              : "+ndate));
                myDoc.add(new Paragraph("Customer ID : "+cid));
                myDoc.add(new Paragraph("-----------------------------------------------------"));
                myDoc.add(new Paragraph("Booking Date   Seats     Time      Hours"));
                myDoc.add(new Paragraph(" "+date+"        "+seats+"    "+stime+"-"+etime+"   "+hours));
                myDoc.add(new Paragraph("-----------------------------------------------------"));
                myDoc.add(new Paragraph(String.format("SubTotal    : Rs %.2f", total)));
                myDoc.add(new Paragraph("Vat(%)       : 0.0"));
                myDoc.add(new Paragraph(String.format("Total          : Rs %.2f", total)));
                myDoc.add(new Paragraph("-----------------------------------------------------"));
                myDoc.add(new Paragraph("                        Thank You!                    "));
                myDoc.close();
                JOptionPane.showMessageDialog(null, "Slip created");
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    public static long getHours(String stime,String etime){
        LocalTime startTime = LocalTime.parse(stime);
        LocalTime endTime = LocalTime.parse(etime);
        
        // Calculate the duration between the two times
        Duration duration = Duration.between(startTime, endTime);
        
        // Extract the number of hours from the duration
        long hours = duration.toHours();
        
        return hours;
    }
    
    public static double getSubtotal(int seats,int hours){
        double res = 0;
        res = seats * hours * 300.0;
        return res;
    }
    
    public static void getBookingDetailReport(JTable t,String topic){
      
        JFileChooser dialog = new JFileChooser();
        dialog.setSelectedFile(new File(topic+"_bookings.pdf"));
        int dialogRes = dialog.showSaveDialog(null);
        
        if(dialogRes == JFileChooser.APPROVE_OPTION){
            String filePath = dialog.getSelectedFile().getPath();
            try {
                Document myDoc = new Document();
                PdfWriter myWrite = PdfWriter.getInstance(myDoc, new FileOutputStream(filePath));
                
                myDoc.open();
                
                myDoc.add(new Paragraph(topic+" Booking Details",FontFactory.getFont(FontFactory.TIMES_ROMAN,20,Font.BOLD)));
                myDoc.add(new Paragraph("  "));
                PdfPTable tbl = new PdfPTable(7);
                float[] columnWidths = {9f, 5f, 5f, 10f, 10f, 10f, 10f};
                tbl.setWidths(columnWidths);
                tbl.setWidthPercentage(100);
                
                PdfPCell cell = new PdfPCell();
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPhrase(new com.itextpdf.text.Phrase("BookingID"));
                tbl.addCell(createHeadCell("BookingID"));
                cell.setPhrase(new com.itextpdf.text.Phrase("Table"));
                tbl.addCell(createHeadCell("Table"));
                cell.setPhrase(new com.itextpdf.text.Phrase("Seats"));
                tbl.addCell(createHeadCell("Seats"));
                cell.setPhrase(new com.itextpdf.text.Phrase("Date"));
                tbl.addCell(createHeadCell("Date"));
                cell.setPhrase(new com.itextpdf.text.Phrase("Time"));
                tbl.addCell(createHeadCell("Time"));
                cell.setPhrase(new com.itextpdf.text.Phrase("CustomerID"));
                tbl.addCell(createHeadCell("CustomerID"));
                cell.setPhrase(new com.itextpdf.text.Phrase("Pay_Status"));
                tbl.addCell(createHeadCell("Pay_Status"));
                
                for (int i = 0; i < t.getRowCount(); i++) {
                    String bid = t.getValueAt(i, 0).toString();
                    String tabelNo = t.getValueAt(i, 1).toString();
                    String seats = t.getValueAt(i, 2).toString();
                    String date = t.getValueAt(i, 3).toString();
                    String time = t.getValueAt(i, 4).toString()+"-"+t.getValueAt(i, 5).toString();
                    String cid = t.getValueAt(i, 6).toString();
                    String pstat = t.getValueAt(i, 7).toString();
                    
                    tbl.addCell(createCell(bid));
                    tbl.addCell(createCell(tabelNo));
                    tbl.addCell(createCell(seats));
                    tbl.addCell(createCell(date));
                    tbl.addCell(createCell(time));
                    tbl.addCell(createCell(cid));
                    tbl.addCell(createCell(pstat));
                   
                }
                myDoc.add(tbl);
                myDoc.close();
                JOptionPane.showMessageDialog(null, "Slip created");
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    private static PdfPCell createHeadCell(String content) {
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Phrase(content));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(10);  // Add top padding of 5 units
        cell.setPaddingBottom(10);  // Add bottom padding of 5 units
        return cell;
    }
    private static PdfPCell createCell(String content) {
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Phrase(content));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(5);  // Add top padding of 5 units
        cell.setPaddingBottom(5);  // Add bottom padding of 5 units
        return cell;
    }
}
