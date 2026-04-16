package com.example.adapter.excel.error;

public class ExcelInvalidCellFormatException extends ExcelCellException {
    public ExcelInvalidCellFormatException(
            String sheetName,
            int rowIndex,
            int columnIndex,
            String columnName,
            String expected,
            String actual,
            Throwable cause
    ) {
        super("EXCEL_INVALID_CELL_FORMAT",
                "Invalid value at " + new ExcelLocation(sheetName, rowIndex, columnIndex).asRef() +
                        " for column '" + columnName + "'. Expected " + expected + ", got " + actual,
                new ExcelLocation(sheetName, rowIndex, columnIndex),
                columnName,
                cause);
    }
}
