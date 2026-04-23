package com.example.adapter.excel.error;

public class ExcelMissingCellValueException extends ExcelMappingException {
    public ExcelMissingCellValueException(String sheet, int row, int col, String name) {
        super("EXCEL_MISSING_CELL_VALUE", "Missing required value for column '" + name + "' at " + new ExcelLocation(sheet, row, col).asRef(), new ExcelLocation(sheet, row, col), null);
    }
}
