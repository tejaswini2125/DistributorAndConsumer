package org.ait.producer.consumer;

import org.ait.producer.consumer.db.OracleConnection;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TelecomController {

	@RequestMapping("/customer")
	public String getTable() {		
		OracleConnection oc = new OracleConnection();
		
		return oc.execute("select * from teju.customer");
		
	}
	@RequestMapping("/cdrdetails")
	public String getTable1() {		
		OracleConnection oc1 = new OracleConnection();
		return oc1.execute("select * from teju.CDR_DETAILS");
	}
	
	@RequestMapping("/daywisecost")
	public String getPlan() {		
		OracleConnection oc1 = new OracleConnection();
		return oc1.execute("select to_char(star_time,'YYYY-MM-DD') as dateval, sum(cost) as cost from teju.cdr_details" + 
				" group by to_char(star_time,'YYYY-MM-DD')" + 
				" order by to_char(star_time,'YYYY-MM-DD')");
	}
	
	@RequestMapping("/planwisecost")
	public String getPlanwisecost() {		
		OracleConnection oc1 = new OracleConnection();
		return oc1.execute("select  t.tariff_plan, sum(duration)" + 
				"	from teju.cdr_details cdr" + 
				"	join teju.mobile_service ms on ms.mobile_no = cdr.from_mobile_no" + 
				"	join teju.tariff_plan t on t.tariff_id = ms.tariff_id" + 
				"	group by t.tariff_plan");
	}
	
	@RequestMapping("/calldetailscustomer")
	public String calldetailscustomer(String id) {		
		OracleConnection oc1 = new OracleConnection();
		if(id == null) {
			id = "101";
		}
		return oc1.execute("select from_mobile_no, to_mobile_no,call_type, duration, star_time " + 
				"from teju.cdr_details cdr " + 
				"join teju.mobile_service ms on ms.mobile_no = cdr.from_mobile_no " + 
				"where ms.customer_id = '"+id+"'"
				);
	}
	
	@RequestMapping("/custormerhavingmorenumber")
	public String custormerhavingmorenumber(int count) {		
		OracleConnection oc1 = new OracleConnection();
		return oc1.execute("select cu.customer_id, cu.customer_name, count(*) " + 
				"from teju.customer cu " + 
				"join teju.mobile_service ms on cu.customer_id=ms.customer_id " + 
				"group by cu.customer_id, cu.customer_name " + 
				"having count(*) > "+count+""
				);
	}
	
	@RequestMapping("/currenttarifplan")
	public String currenttarifplan(String planid) {		
		OracleConnection oc1 = new OracleConnection();
		return oc1.execute("select cu.customer_id, ms.mobile_no, cu.customer_name, t.tariff_plan, ms.balance" + 
				"	from teju.customer cu" + 
				"	join teju.mobile_service ms on cu.customer_id=ms.customer_id" + 
				"	join teju.tariff_plan t on t.tariff_id = ms.tariff_id" + 
				(planid == null?"":"	where cu.customer_id = '"+planid+"'")
				+" order by cu.customer_id"
				);
	}
	
	@RequestMapping("/totalcallinarea")
	public String totalcallinarea() {		
		OracleConnection oc1 = new OracleConnection();
		return oc1.execute("select l.AREA_NAME, count(*) " + 
				"from teju.cdr_details cdr " + 
				"join teju.location l on cdr.area_code = l.area_code " + 
				"group by l.AREA_NAME"
				);
	}
	
	@RequestMapping("/totalcalltype")
	public String totalcalltype() {		
		OracleConnection oc1 = new OracleConnection();
		return oc1.execute("select call_type, count(*) from teju.cdr_details " + 
				"group by call_type " + 
				"order by call_type"
				);
	}
	
	@RequestMapping("/ageonnetwork")
	public String ageonnetwork() {		
		OracleConnection oc1 = new OracleConnection();
		return oc1.execute("select customer_name, ROUND((SYSDATE-DATE_OF_ACTIVATION),0) as \"age on network in days\"  from teju.customer"
				);
	}
	
	@RequestMapping("/customerdetails")
	public String customerdetails(String mobile) {		
		OracleConnection oc1 = new OracleConnection();
		return oc1.execute("select  CUSTOMER_NAME,mobile_no, DATE_OF_ACTIVATION,ADDRESS_PROOF_ID,ADDRESS_PROOF_TYPE " + 
				",EMAIL_ID  from teju.customer cu " + 
				"join teju.mobile_service ms on cu.customer_id=ms.customer_id " + 
				( mobile!=null?"where ms.mobile_no = '"+mobile+"'":"")
				);
	}	

	@RequestMapping("/advancequery")
	public String advanceQuery(String query) {		
		OracleConnection oc1 = new OracleConnection();
		return oc1.execute(query);
	}
	
	
	@RequestMapping("/billsummary")
	public String billsummary(String id) {		
		OracleConnection oc1 = new OracleConnection();
		if(id == null) {
			id = "101";
		}
		return oc1.execute("select from_mobile_no \"From\", to_mobile_no \"To\",call_type \"Call Type\", duration \"Duration\", star_time \"Time\", cost \"Cost\"" + 
				"				from teju.cdr_details cdr " + 
				"				join teju.mobile_service ms on ms.mobile_no = cdr.from_mobile_no " + 
				"where ms.customer_id = '"+id+"'"
				);
	}
	

}
