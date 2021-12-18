package org.ait.producer.consumer;

import java.util.Map;

import org.ait.producer.consumer.dao.DistributorDao;
import org.ait.producer.consumer.dao.ProducerActorsDao;
import org.ait.producer.consumer.model.TypeOfUser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @RequestMapping("/login")
    public String index(@RequestBody Map<String, String> values){
    	String userId= values.get("userId");
    	String pwd = values.get("pwd");
    	String type = values.get("type");
    	String result = "";
    	if(TypeOfUser.DISTRIBUTOR == TypeOfUser.valueOf(type)) {
    		DistributorDao distributorDao =  new DistributorDao();
    		result = distributorDao.login(userId, pwd);    		
    	} else if(TypeOfUser.EMPLOYEE == TypeOfUser.valueOf(type)){
    		ProducerActorsDao producerActorsDao = new ProducerActorsDao();
    		result = producerActorsDao.login(userId, pwd);
    	}
        return result;
    }

}