package org.ait.producer.consumer;

import org.ait.producer.consumer.dao.CustomerDAO;
import org.ait.producer.consumer.model.Customer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillingController {
	@RequestMapping("/insertcustomer")
	public String register(@RequestBody Customer customer) {
		CustomerDAO custDAO = new CustomerDAO();
		custDAO.insertCustomer(customer);
		return "success";
	}
}
