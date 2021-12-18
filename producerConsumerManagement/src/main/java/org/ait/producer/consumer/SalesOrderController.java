package org.ait.producer.consumer;

import java.time.LocalDate;
import java.util.Map;

import org.ait.producer.consumer.dao.SaleOrderDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SalesOrderController {

	@GetMapping("/orders/summary")
	public String placeOrder() {
		SaleOrderDAO saleOrderDAO = new SaleOrderDAO();
		return saleOrderDAO.getSaleOrderSummary(LocalDate.now());		
	}
	
	@PostMapping("/orders/bill")
	public String billSummary(@RequestBody Map<String, String> billContext) {
		SaleOrderDAO saleOrderDAO = new SaleOrderDAO();
		String distributorId = (String)billContext.get("distributorId");
		return saleOrderDAO.totalBillSummary(distributorId, LocalDate.now());		
	}
}
