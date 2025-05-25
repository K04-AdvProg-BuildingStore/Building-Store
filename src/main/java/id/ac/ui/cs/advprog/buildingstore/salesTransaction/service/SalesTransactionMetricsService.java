package id.ac.ui.cs.advprog.buildingstore.salesTransaction.service;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class SalesTransactionMetricsService {
    private final MeterRegistry meterRegistry;

    public SalesTransactionMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void incrementTransactionCount() {
        meterRegistry.counter("sales_transactions_created_total").increment();
    }
}