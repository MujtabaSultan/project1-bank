package com.project.bank;

import java.time.LocalDateTime;

public interface TransactionFilter {
    boolean test(Transaction transaction);

    static TransactionFilter today() {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        return t -> t.getTimestamp().isAfter(startOfDay);
    }

    static TransactionFilter yesterday() {
        LocalDateTime startOfYesterday = LocalDateTime.now().minusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay();
        return t -> t.getTimestamp().isAfter(startOfYesterday) && t.getTimestamp().isBefore(startOfToday);
    }

    static TransactionFilter lastNDays(int days) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
        return t -> t.getTimestamp().isAfter(cutoff);
    }

    static TransactionFilter lastWeek() {
        return lastNDays(7);
    }

    static TransactionFilter lastMonth() {
        return lastNDays(30);
    }

    static TransactionFilter dateRange(LocalDateTime start, LocalDateTime end) {
        return t -> t.getTimestamp().isAfter(start) && t.getTimestamp().isBefore(end);
    }
}
