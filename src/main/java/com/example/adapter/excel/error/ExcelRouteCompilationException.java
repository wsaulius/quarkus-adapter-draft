package com.example.adapter.excel.error;

public class ExcelRouteCompilationException extends ExcelMappingException {
    private final String routeId;

    public ExcelRouteCompilationException(String message, String routeId, ExcelLocation location, Throwable cause) {
        super("EXCEL_ROUTE_COMPILATION_ERROR", message, location, cause);
        this.routeId = routeId;
    }

    public String routeId() {
        return routeId;
    }
}
