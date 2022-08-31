package core.basesyntax.service.impl;

import core.basesyntax.service.ReportService;
import core.basesyntax.db.Storage;

public class ReportServiceImpl implements ReportService {
    private static final String SEPARATOR = ",";

    @Override
    public String getReport() {
        StringBuilder report = new StringBuilder("fruit, quantity");
        Storage.storage.forEach((k, v) -> report.append(System.lineSeparator())
                .append(k)
                .append(SEPARATOR)
                .append(v));
        return report.toString();
    }
}
