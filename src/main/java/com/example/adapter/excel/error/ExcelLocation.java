package com.example.adapter.excel.error;

import org.apache.poi.ss.util.CellReference;

public record ExcelLocation(
        String sheetName,
        Integer rowIndex,
        Integer columnIndex
) {
    public String asRef() {
        if (sheetName == null) {
            return "<unknown>";
        }
        if (rowIndex == null) {
            return sheetName;
        }
        if (columnIndex == null) {
            return sheetName + "!" + (rowIndex + 1);
        }
        return sheetName + "!" + new CellReference(rowIndex, columnIndex).formatAsString();
    }
}
