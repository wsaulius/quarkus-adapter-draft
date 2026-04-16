package com.example.adapter.excel.error;

public class ExcelMissingCellValueException extends ExcelCellException {
    public ExcelMissingCellValueException(String sheetName, int rowIndex, int columnIndex, String columnName) {
        super("EXCEL_MISSING_CELL_VALUE",
                "Missing required value for column '" + columnName + "' at " +
                        new ExcelLocation(sheetName, rowIndex, columnIndex).asRef(),
                new ExcelLocation(sheetName, rowIndex, columnIndex),
                columnName,
                null);
    }
}
