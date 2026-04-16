package com.example.adapter.excel;

import com.example.adapter.domain.CompiledRoute;
import com.example.adapter.domain.ExcelRouteDefinition;
import com.example.adapter.excel.error.ExcelInvalidCellFormatException;
import com.example.adapter.excel.error.ExcelLoadException;
import com.example.adapter.excel.error.ExcelMappingException;
import com.example.adapter.excel.error.ExcelMissingCellValueException;
import com.example.adapter.excel.error.ExcelMissingColumnException;
import com.example.adapter.excel.error.ExcelSheetNotFoundException;
import com.example.adapter.routes.CompiledRouteFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.*;

@ApplicationScoped
public class ExcelMappingLoader {
    @Inject CompiledRouteFactory routeFactory;

    private static final List<String> REQUIRED_COLUMNS = List.of(
            "enabled", "priority", "tenant", "environment", "input_method", "input_path_template",
            "target_system", "target_base_url", "target_path_template", "target_method",
            "transform_ref", "timeout_ms", "version_tag"
    );

    public List<CompiledRoute> load(InputStream is, String sheetName) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) throw new ExcelSheetNotFoundException(sheetName);

            Map<String, Integer> cols = headerMap(sheet.getRow(0), sheetName);
            validateHeaders(cols, sheetName);

            List<CompiledRoute> routes = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || !bool(row, cols, "enabled")) continue;

                String routeId = optional(row, cols, "version_tag");
                ExcelRouteDefinition def = new ExcelRouteDefinition(
                        true,
                        requiredInt(row, cols, sheetName, "priority"),
                        required(row, cols, sheetName, "tenant"),
                        required(row, cols, sheetName, "environment"),
                        required(row, cols, sheetName, "input_method"),
                        required(row, cols, sheetName, "input_path_template"),
                        required(row, cols, sheetName, "target_system"),
                        required(row, cols, sheetName, "target_base_url"),
                        required(row, cols, sheetName, "target_path_template"),
                        required(row, cols, sheetName, "target_method"),
                        required(row, cols, sheetName, "transform_ref"),
                        requiredInt(row, cols, sheetName, "timeout_ms"),
                        required(row, cols, sheetName, "version_tag")
                );

                routes.add(routeFactory.compile(def, sheetName, row.getRowNum(), cols.get("transform_ref"), routeId));
            }

            routes.sort(Comparator.comparingInt((CompiledRoute r) -> r.definition().priority()).reversed());
            return routes;
        } catch (ExcelMappingException e) {
            throw e;
        } catch (Exception e) {
            throw new ExcelLoadException("Failed to load Excel mappings", e);
        }
    }

    private void validateHeaders(Map<String, Integer> cols, String sheetName) {
        for (String required : REQUIRED_COLUMNS) {
            if (!cols.containsKey(required)) {
                throw new ExcelMissingColumnException(sheetName, required);
            }
        }
    }

    private Map<String, Integer> headerMap(Row header, String sheetName) {
        if (header == null) {
            throw new ExcelMissingColumnException(sheetName, "<header row>");
        }
        Map<String, Integer> map = new HashMap<>();
        for (Cell cell : header) {
            String value = cell.getStringCellValue().trim();
            map.put(value, cell.getColumnIndex());
        }
        return map;
    }

    private String str(Row row, Map<String, Integer> cols, String name) {
        Integer idx = cols.get(name);
        if (idx == null) return null;
        Cell cell = row.getCell(idx, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                double n = cell.getNumericCellValue();
                yield Math.floor(n) == n ? String.valueOf((long) n) : String.valueOf(n);
            }
            case BOOLEAN -> Boolean.toString(cell.getBooleanCellValue());
            default -> null;
        };
    }

    private String required(Row row, Map<String, Integer> cols, String sheetName, String columnName) {
        Integer columnIndex = cols.get(columnName);
        if (columnIndex == null) {
            throw new ExcelMissingColumnException(sheetName, columnName);
        }
        String value = str(row, cols, columnName);
        if (value == null || value.isBlank()) {
            throw new ExcelMissingCellValueException(sheetName, row.getRowNum(), columnIndex, columnName);
        }
        return value;
    }

    private String optional(Row row, Map<String, Integer> cols, String columnName) {
        return str(row, cols, columnName);
    }

    private int requiredInt(Row row, Map<String, Integer> cols, String sheetName, String columnName) {
        Integer columnIndex = cols.get(columnName);
        if (columnIndex == null) {
            throw new ExcelMissingColumnException(sheetName, columnName);
        }
        String value = str(row, cols, columnName);
        if (value == null || value.isBlank()) {
            throw new ExcelMissingCellValueException(sheetName, row.getRowNum(), columnIndex, columnName);
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ExcelInvalidCellFormatException(sheetName, row.getRowNum(), columnIndex, columnName, "integer", value, e);
        }
    }

    private boolean bool(Row row, Map<String, Integer> cols, String name) {
        String v = str(row, cols, name);
        return v != null && Boolean.parseBoolean(v);
    }
}
