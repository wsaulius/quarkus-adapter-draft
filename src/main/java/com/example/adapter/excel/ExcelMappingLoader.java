package com.example.adapter.excel;

import com.example.adapter.domain.CompiledRoute;
import com.example.adapter.domain.ExcelRouteDefinition;
import com.example.adapter.fp.CompiledRouteFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.InputStream;
import java.util.*;

@ApplicationScoped
public class ExcelMappingLoader {
    @Inject CompiledRouteFactory routeFactory;

    public List<CompiledRoute> load(InputStream is, String sheetName) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) throw new IllegalStateException("Missing sheet: " + sheetName);
            Map<String, Integer> cols = headerMap(sheet.getRow(0));
            List<CompiledRoute> routes = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || !bool(row, cols, "enabled")) continue;
                ExcelRouteDefinition def = new ExcelRouteDefinition(
                        true, integer(row, cols, "priority", 0), str(row, cols, "tenant"), str(row, cols, "environment"),
                        str(row, cols, "input_method"), str(row, cols, "input_path_template"), str(row, cols, "target_system"),
                        str(row, cols, "target_base_url"), str(row, cols, "target_path_template"), str(row, cols, "target_method"),
                        str(row, cols, "transform_ref"), integer(row, cols, "timeout_ms", 3000), str(row, cols, "version_tag")
                );
                routes.add(routeFactory.compile(def));
            }
            routes.sort(Comparator.comparingInt((CompiledRoute r) -> r.definition().priority()).reversed());
            return routes;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load Excel mappings", e);
        }
    }

    private Map<String, Integer> headerMap(Row header) {
        Map<String, Integer> map = new HashMap<>();
        for (Cell cell : header) map.put(cell.getStringCellValue().trim(), cell.getColumnIndex());
        return map;
    }
    private String str(Row row, Map<String, Integer> cols, String name) {
        Integer idx = cols.get(name);
        if (idx == null) return null;
        Cell cell = row.getCell(idx, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> { double n = cell.getNumericCellValue(); yield Math.floor(n) == n ? String.valueOf((long) n) : String.valueOf(n); }
            case BOOLEAN -> Boolean.toString(cell.getBooleanCellValue());
            default -> null;
        };
    }
    private boolean bool(Row row, Map<String, Integer> cols, String name) {
        return Boolean.parseBoolean(Optional.ofNullable(str(row, cols, name)).orElse("false"));
    }
    private int integer(Row row, Map<String, Integer> cols, String name, int fallback) {
        try { return Integer.parseInt(Optional.ofNullable(str(row, cols, name)).orElse(String.valueOf(fallback))); }
        catch (Exception e) { return fallback; }
    }
}
