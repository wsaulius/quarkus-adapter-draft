/**
 * Loads workbook sheets into row-based domain objects.
 *
 * <p>This loader is responsible for workbook access and structural validation.
 * It verifies sheet presence, required headers, required values, and numeric parsing.
 *
 * <p>It does not build runtime orchestration objects. That work is delegated to
 * {@link WorkbookCompiler}. This keeps loading and compilation separate.
 */
package com.example.adapter.excel;

import com.example.adapter.config.AdapterConfig;
import com.example.adapter.domain.AggregateFieldRow;
import com.example.adapter.domain.RouteRow;
import com.example.adapter.domain.StepRow;
import com.example.adapter.domain.TransformFieldRow;
import com.example.adapter.excel.error.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.*;

@ApplicationScoped
public class ExcelWorkbookLoader {
    @Inject
    AdapterConfig config;

    public WorkbookModel load(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {
            return new WorkbookModel(
                    loadRoutes(workbook.getSheet(config.excel().routesSheet()), config.excel().routesSheet()),
                    loadSteps(workbook.getSheet(config.excel().stepsSheet()), config.excel().stepsSheet()),
                    loadTransforms(workbook.getSheet(config.excel().transformsSheet()), config.excel().transformsSheet()),
                    loadAggregates(workbook.getSheet(config.excel().aggregatesSheet()), config.excel().aggregatesSheet())
            );
        } catch (ExcelMappingException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load workbook", e);
        }
    }

    private List<RouteRow> loadRoutes(Sheet sheet, String name) {
        requireSheet(sheet, name);
        Map<String, Integer> h = headers(sheet, name, List.of("enabled", "priority", "tenant", "environment", "input_method", "input_path_template", "target_system", "plan_id"));
        List<RouteRow> out = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row r = sheet.getRow(i);
            if (r == null || !bool(r, h, "enabled")) continue;
            out.add(new RouteRow(true, reqInt(r, h, name, "priority"), req(r, h, name, "tenant"), req(r, h, name, "environment"), req(r, h, name, "input_method"), req(r, h, name, "input_path_template"), req(r, h, name, "target_system"), req(r, h, name, "plan_id")));
        }
        return out;
    }

    private List<StepRow> loadSteps(Sheet sheet, String name) {
        requireSheet(sheet, name);
        Map<String, Integer> h = headers(sheet, name, List.of("enabled", "plan_id", "step_id", "step_irisCall", "method", "base_url", "path_template", "transform_ref", "stop_on_error", "timeout_ms"));
        List<StepRow> out = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row r = sheet.getRow(i);
            if (r == null || !bool(r, h, "enabled")) continue;
            out.add(new StepRow(true, req(r, h, name, "plan_id"), req(r, h, name, "step_id"), reqInt(r, h, name, "step_irisCall"), req(r, h, name, "method"), req(r, h, name, "base_url"), req(r, h, name, "path_template"), req(r, h, name, "transform_ref"), bool(r, h, "stop_on_error"), reqInt(r, h, name, "timeout_ms")));
        }
        return out;
    }

    private List<TransformFieldRow> loadTransforms(Sheet sheet, String name) {
        requireSheet(sheet, name);
        Map<String, Integer> h = headers(sheet, name, List.of("enabled", "transform_ref", "target_field", "source_expr", "optional"));
        List<TransformFieldRow> out = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row r = sheet.getRow(i);
            if (r == null || !bool(r, h, "enabled")) continue;
            out.add(new TransformFieldRow(true, req(r, h, name, "transform_ref"), req(r, h, name, "target_field"), req(r, h, name, "source_expr"), bool(r, h, "optional")));
        }
        return out;
    }

    private List<AggregateFieldRow> loadAggregates(Sheet sheet, String name) {
        requireSheet(sheet, name);
        Map<String, Integer> h = headers(sheet, name, List.of("enabled", "plan_id", "target_field", "source_expr", "optional"));
        List<AggregateFieldRow> out = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row r = sheet.getRow(i);
            if (r == null || !bool(r, h, "enabled")) continue;
            out.add(new AggregateFieldRow(true, req(r, h, name, "plan_id"), req(r, h, name, "target_field"), req(r, h, name, "source_expr"), bool(r, h, "optional")));
        }
        return out;
    }

    private void requireSheet(Sheet s, String n) {
        if (s == null) throw new ExcelSheetNotFoundException(n);
    }

    private Map<String, Integer> headers(Sheet sheet, String name, List<String> required) {
        Row header = sheet.getRow(0);
        if (header == null) throw new ExcelMissingColumnException(name, "<header row>");
        Map<String, Integer> map = new HashMap<>();
        for (Cell c : header) map.put(c.getStringCellValue().trim(), c.getColumnIndex());
        for (String r : required) if (!map.containsKey(r)) throw new ExcelMissingColumnException(name, r);
        return map;
    }

    private String cell(Row row, Map<String, Integer> h, String col) {
        Integer idx = h.get(col);
        if (idx == null) return null;
        Cell c = row.getCell(idx, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (c == null) return null;
        return switch (c.getCellType()) {
            case STRING -> c.getStringCellValue().trim();
            case NUMERIC -> {
                double n = c.getNumericCellValue();
                yield Math.floor(n) == n ? String.valueOf((long) n) : String.valueOf(n);
            }
            case BOOLEAN -> Boolean.toString(c.getBooleanCellValue());
            default -> null;
        };
    }

    private String req(Row row, Map<String, Integer> h, String sheet, String col) {
        Integer idx = h.get(col);
        if (idx == null) throw new ExcelMissingColumnException(sheet, col);
        String v = cell(row, h, col);
        if (v == null || v.isBlank()) throw new ExcelMissingCellValueException(sheet, row.getRowNum(), idx, col);
        return v;
    }

    private int reqInt(Row row, Map<String, Integer> h, String sheet, String col) {
        Integer idx = h.get(col);
        if (idx == null) throw new ExcelMissingColumnException(sheet, col);
        String v = cell(row, h, col);
        if (v == null || v.isBlank()) throw new ExcelMissingCellValueException(sheet, row.getRowNum(), idx, col);
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            throw new ExcelInvalidCellFormatException(sheet, row.getRowNum(), idx, col, "integer", v, e);
        }
    }

    private boolean bool(Row row, Map<String, Integer> h, String col) {
        return Boolean.parseBoolean(Optional.ofNullable(cell(row, h, col)).orElse("false"));
    }
}
