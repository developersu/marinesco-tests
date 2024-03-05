package ru.redrise.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;

public class TwoColumnsExcelReader {
    private static final int COLUMN_COUNT = 2;

    private TwoColumnsExcelReader(){}

    public static Object[][] getFromFile(boolean firstRowIsTitle, String fileLocation, String sheetName) throws Exception{
        return getFromFile(0, firstRowIsTitle ? 1 : 0, fileLocation, sheetName);
    }
    public static Object[][] getFromFile(int startColumn, int startRow, String fileLocation, String sheetName) throws Exception{
        XSSFWorkbook excelWorkbook = new XSSFWorkbook(new FileInputStream(fileLocation));
        XSSFSheet excelSpreadsheet = excelWorkbook.getSheet(sheetName);
        int rowsCount = excelSpreadsheet.getLastRowNum();

        String[][] results = new String[rowsCount - startRow][COLUMN_COUNT];
        int j = 0;
        for (int i = startRow; i < rowsCount; i++) {
            Row row = excelSpreadsheet.getRow(i);
            results[j][0] = row.getCell(startColumn).getStringCellValue();
            results[j][1] = row.getCell(startColumn+1).getStringCellValue();
            j++;
        }

        return results;
    }
}