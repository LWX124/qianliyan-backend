package com.stylefeng.guns.modular.system.utils;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class POIUtils {
    public static void exportExcel(List<Map<String, Object>> data, OutputStream out, String sheetName, Map<String, String> titles) throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();
        try {
            if (null == sheetName) {
                sheetName = "Sheet1";
            }
            XSSFSheet sheet = wb.createSheet(sheetName);
            writeExcel(wb, sheet, data, titles);
            wb.write(out);
        } finally {
            wb.close();
        }
    }

    private static void writeExcel(XSSFWorkbook wb, Sheet sheet, List<Map<String, Object>> data, Map<String, String> titles) {

        int rowIndex = 0;

        rowIndex = writeTitlesToExcel(wb, sheet, titles);
        writeRowsToExcel(wb, sheet, data, titles, rowIndex);
        autoSizeColumns(sheet, titles.size() + 1);

    }

    private static int writeRowsToExcel(XSSFWorkbook wb, Sheet sheet, List<Map<String, Object>> data, Map<String, String> titles, int rowIndex) {
        int colIndex = 0;
        DateFormat format=new SimpleDateFormat();
        short df = wb.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss");
        Font dataFont = wb.createFont();
        dataFont.setFontName("simsun");
        dataFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle dataStyle = wb.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dataStyle.setFont(dataFont);

        XSSFCellStyle dateStyle = wb.createCellStyle();
        dateStyle.setAlignment(HorizontalAlignment.CENTER);
        dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dateStyle.setFont(dataFont);
        dateStyle.setDataFormat(df);

        for (Map<String, Object> map : data) {
            Row dataRow = sheet.createRow(rowIndex);
            // dataRow.setHeightInPoints(25);
            colIndex = 0;

            for(String key : titles.keySet()){
                Cell cell = dataRow.createCell(colIndex);
                Object value = map.get(key);
                if (map.get(key) != null) {
                    if(value instanceof Integer){
                        cell.setCellValue((Integer)map.get(key));
                    }else if (value instanceof Long){
                        cell.setCellValue((Long)map.get(key));
                    }else if (value instanceof Date){
                        cell.setCellValue((Date)map.get(key));

                    }else if (value instanceof BigDecimal){
                        cell.setCellValue(((BigDecimal)map.get(key)).doubleValue());
                    }else {
                        cell.setCellValue(map.get(key).toString());
                    }
                } else {
                    cell.setCellValue("");
                }
                if(value instanceof Date){
                    cell.setCellStyle(dateStyle);
                }else {
                    cell.setCellStyle(dataStyle);
                }

                colIndex++;
            }
            rowIndex++;
        }
        return rowIndex;
    }

    private static int writeTitlesToExcel(XSSFWorkbook wb, Sheet sheet, Map<String, String> titles) {
        int rowIndex = 0;
        int colIndex = 0;

        Font titleFont = wb.createFont();
        titleFont.setFontName("simsun");
        titleFont.setBold(true);
        // titleFont.setFontHeightInPoints((short) 14);
        titleFont.setColor(IndexedColors.BLACK.index);
        XSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        titleStyle.setFont(titleFont);

        Row titleRow = sheet.createRow(rowIndex);
        // titleRow.setHeightInPoints(25);
        colIndex = 0;
        Collection<String> cnTitles = titles.values();
        for (String field : cnTitles) {
            Cell cell = titleRow.createCell(colIndex);
            cell.setCellValue(field);
            cell.setCellStyle(titleStyle);
            colIndex++;
        }

        rowIndex++;
        return rowIndex;
    }

    private static void autoSizeColumns(Sheet sheet, int columnNumber) {

        for (int i = 0; i < columnNumber; i++) {
            int orgWidth = sheet.getColumnWidth(i);
            sheet.autoSizeColumn(i, true);
            int newWidth = (int) (sheet.getColumnWidth(i) + 100);
            if (newWidth > orgWidth) {
                sheet.setColumnWidth(i, newWidth);
            } else {
                sheet.setColumnWidth(i, orgWidth);
            }
        }
    }

}
