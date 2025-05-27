package id.ac.ui.cs.advprog.buildingstore.payment.service;

import id.ac.ui.cs.advprog.buildingstore.payment.enums.PaymentStatus;
import id.ac.ui.cs.advprog.buildingstore.payment.model.Payment;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentMetricService {
    private final Counter paymentTotalCounter;
    private final Counter paymentSuccessCounter;
    private final Counter paymentFullCounter;
    private final Counter paymentInstallmentCounter;
    private final DistributionSummary paymentAmountSummary;

    public PaymentMetricService(MeterRegistry meterRegistry) {
        this.paymentTotalCounter = Counter.builder("payment.total")
                .description("Total number of payment attempts")
                .register(meterRegistry);

        this.paymentSuccessCounter = Counter.builder("payment.success")
                .description("Total number of successful payments")
                .register(meterRegistry);

        this.paymentFullCounter = Counter.builder("payment.full")
                .description("Total number of full payments")
                .register(meterRegistry);

        this.paymentInstallmentCounter = Counter.builder("payment.installment")
                .description("Total number of installment payments")
                .register(meterRegistry);

        this.paymentAmountSummary = DistributionSummary.builder("payment.amount")
                .description("Distribution of payment amounts")
                .baseUnit("currency")
                .register(meterRegistry);
    }

    public void recordPaymentAttempt(Payment payment) {
        paymentTotalCounter.increment();
    }

    public void recordSuccessfulPayment(Payment payment) {
        paymentSuccessCounter.increment();
        paymentAmountSummary.record(payment.getAmount().doubleValue());

        if (payment.getStatus() == PaymentStatus.FULL) {
            paymentFullCounter.increment();
        } else if (payment.getStatus() == PaymentStatus.INSTALLMENT) {
            paymentInstallmentCounter.increment();
        }
    }
}