package core.basesyntax;

import core.basesyntax.model.Transaction;
import core.basesyntax.service.WriteToFileService;
import core.basesyntax.service.CreateFileService;
import core.basesyntax.service.ParseService;
import core.basesyntax.service.ReaderService;
import core.basesyntax.service.ReportService;
import core.basesyntax.serviceImpl.WriteToFileServiceImpl;
import core.basesyntax.serviceImpl.CreateFileServiceImpl;
import core.basesyntax.serviceImpl.ParseServiceImpl;
import core.basesyntax.serviceImpl.ReaderServiceImpl;
import core.basesyntax.serviceImpl.ReportServiceImpl;
import core.basesyntax.strategy.ReturnOperationHandler;
import core.basesyntax.strategy.BalanceOperationHandler;
import core.basesyntax.strategy.OperationHandler;
import core.basesyntax.strategy.PurchaseOperationHandler;
import core.basesyntax.strategy.SupplyOperationHandler;
import core.basesyntax.strategy.OperationStrategy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static final String INPUT_FILE_PATH = "src/main/resources/input.csv";
    private static final String OUTPUT_FILE_PATH = "src/main/resources/report.csv";

    public static void main(String[] args) {
        Map<String, OperationHandler> map = new HashMap<>();
        map.put("b", new BalanceOperationHandler());
        map.put("s", new SupplyOperationHandler());
        map.put("p", new PurchaseOperationHandler());
        map.put("r", new ReturnOperationHandler());

        OperationStrategy strategy = new OperationStrategy(map);

        ReaderService readerService = new ReaderServiceImpl();
        List<String> lines = readerService.readData(INPUT_FILE_PATH);

        ParseService parseService = new ParseServiceImpl();
        List<Transaction> transactions = parseService.parseLine(lines);

        for (Transaction transaction : transactions) {
            OperationHandler handler = strategy.getByOperation(transaction.getOperation());
            handler.apply(transaction);
        }

        ReportService reportService = new ReportServiceImpl();
        String report = reportService.getReport();

        CreateFileService createFileService = new CreateFileServiceImpl();
        createFileService.createFile(OUTPUT_FILE_PATH);

        WriteToFileService writeToFileService = new WriteToFileServiceImpl();
        writeToFileService.writeToFile(report, OUTPUT_FILE_PATH);
    }
}
