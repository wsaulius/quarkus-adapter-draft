package com.example.adapter.excel.error;

public class ExcelInvalidCellFormatException extends ExcelMappingException {
    public ExcelInvalidCellFormatException(String sheet, int row, int col, String name, String expected, String actual, Throwable cause) {
        super("EXCEL_INVALID_CELL_FORMAT", "Invalid value at " + new ExcelLocation(sheet, row, col).asRef() + " for column '" + name + "'. Expected " + expected + ", got " + actual, new ExcelLocation(sheet, row, col), cause);
    }
}
