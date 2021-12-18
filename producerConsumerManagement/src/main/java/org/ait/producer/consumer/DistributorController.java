package org.ait.producer.consumer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.ait.producer.consumer.dao.AssignOrderDAO;
import org.ait.producer.consumer.dao.DistributorDao;
import org.ait.producer.consumer.dao.ProductDAO;
import org.ait.producer.consumer.dao.ReturnOrderDAO;
import org.ait.producer.consumer.dao.SaleOrderDAO;
import org.ait.producer.consumer.utils.ResultConversionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DistributorController {

	@GetMapping("/product")
	public String index() {
		ProductDAO productDAO = new ProductDAO();
		String productDetails = productDAO.getAllProductDetails();
		return productDetails;
	}
	
	@GetMapping("/distributors")
	public String getAllDistributors() {
		DistributorDao distributorDao = new DistributorDao();
		String distributorDetails = distributorDao.getAllDistributors();
		return distributorDetails;
	}
	
	@PutMapping("/distributor/order")
	public String placeOrder(@RequestBody Map<String,Object> saleOrder) {
		SaleOrderDAO saleOrderDAO = new SaleOrderDAO();
		String saleOrderId = UUID.randomUUID().toString();
		String distributorId = (String)saleOrder.get("distributorId");
		String result = saleOrderDAO.insertCustomer(distributorId, saleOrderId, LocalDate.now(),  (List<Map<String, Object>>) saleOrder.get("saleOrder"));
		if(ResultConversionUtils.SUCCESS.equals(result)) {
			return getPlaceOrderDetails(saleOrder);
		} else {
			return result;
		}
	}
	
	@PostMapping("/distributor/order")
	public String getPlaceOrderDetails(@RequestBody Map<String,Object> saleOrder) {
		SaleOrderDAO saleOrderDAO = new SaleOrderDAO();
		String distributorId = (String)saleOrder.get("distributorId");
		String result = saleOrderDAO.getSaleOrderDetails(distributorId, LocalDate.now());
		return result;
	}
	
	@GetMapping("/distributors/missedorder")
	public String getMissedOrder() {
		SaleOrderDAO productDAO = new SaleOrderDAO();
		String productDetails = productDAO.distributorMissedOrder(LocalDate.now());
		return productDetails;
	}
	
	@PutMapping("/distributor/return")
	public String returnOrder(@RequestBody Map<String,Object> saleOrder) {
		ReturnOrderDAO retunOrderDAO = new ReturnOrderDAO();
		String saleOrderId = UUID.randomUUID().toString();
		String distributorId = (String)saleOrder.get("distributorId");
		String result = retunOrderDAO.insertReturnOrder(distributorId, saleOrderId, LocalDate.now(),  (List<Map<String, Object>>) saleOrder.get("saleOrder"));
		if(ResultConversionUtils.SUCCESS.equals(result)) {
			return getReturnOrderDetails(saleOrder);
		} else {
			return result;
		}
	}
	
	@PostMapping("/distributor/return")
	public String getReturnOrderDetails(@RequestBody Map<String,Object> saleOrder) {
		ReturnOrderDAO saleOrderDAO = new ReturnOrderDAO();
		String distributorId = (String)saleOrder.get("distributorId");
		String result = saleOrderDAO.getReturnOrderDetails(distributorId, LocalDate.now());
		return result;
	}
	
	@PutMapping("/distributor/assign")
	public String assignOrder(@RequestBody Map<String,Object> saleOrder) {
		AssignOrderDAO assignOrderDao= new AssignOrderDAO();
		String saleOrderId = UUID.randomUUID().toString();
		String distributorId = (String)saleOrder.get("distributorId");
		String result = assignOrderDao.insertAssignOrder(distributorId, saleOrderId, LocalDate.now(),  (List<Map<String, Object>>) saleOrder.get("saleOrder"));
		if(ResultConversionUtils.SUCCESS.equals(result)) {
			return getAssignsOrderDetails(saleOrder);
		} else {
			return result;
		}
	}
	
	@PostMapping("/distributor/assign")
	public String getAssignsOrderDetails(@RequestBody Map<String,Object> saleOrder) {
		AssignOrderDAO saleOrderDAO = new AssignOrderDAO();
		String distributorId = (String)saleOrder.get("distributorId");
		String result = saleOrderDAO.getAssignDetails(distributorId, LocalDate.now());
		return result;
	}

}