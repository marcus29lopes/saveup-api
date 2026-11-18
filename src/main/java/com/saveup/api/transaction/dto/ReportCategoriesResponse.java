package com.saveup.api.transaction.dto;

import java.math.BigDecimal;

public record ReportCategoriesResponse(String categoryName, BigDecimal totalAmount) {
}
