package com.example.adapter.excel.error;

public class ExcelUnknownTransformReferenceException extends ExcelReferenceException {
    public ExcelUnknownTransformReferenceException(String sheetName, int rowIndex, int columnIndex, String transformRef) {
        super("EXCEL_UNKNOWN_TRANSFORM_REF",
                "Unknown transform_ref '" + transformRef + "' at " +
                        new ExcelLocation(sheetName, rowIndex, columnIndex).asRef(),
                new ExcelLocation(sheetName, rowIndex, columnIndex),
                null);
    }
}
