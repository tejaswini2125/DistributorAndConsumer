/**
 * 
 */
 var userId = "";
 var loginResult = {}
 function login(){
 	userId = $("#userid").val();
 	var pwd = $("#pwd").val();
 	var type = $("#type").val();
 	$("#loginerror").hide();
 	callPostRequest("/login", "post", {userId: userId, pwd:pwd, type: type}, function(res){
 	 var result = eval("["+res+"]")[0];
 	 if(!result.errorMessage){	
 		$("#homeWindow").show();
 		$("#loginwindow").hide();
 	 	if("DISTRIBUTOR" === type){ 	 		
 	 		showWindow("distributorwindow");
	 	 	loginResult = result.rows[0]; 
	 	 	openDefaultTab("distributorDefaultOpen");
	 	 	document.getElementById("logoutUser").innerHTML = loginResult.distributor_id;
	 	 	updateDistributor();
	 	 } else if("EMPLOYEE" === type){
	 		loginResult = result.rows[0]; 
	 		showWindow("employeeWindow");
	 		openDefaultTab("defaultOpenEmployeeWindow");
	 		initEmployee();
	 	 	document.getElementById("logoutUser").innerHTML = loginResult.employee_id;
	 	 }
	 	 $( "#menu" ).menu({
		      items: "> :not(.ui-widget-header)"
		    });
		 $("#menu").hide();
 	 } else {
 	 	$("#loginerror").show();
 	 	$("#loginerror").html("Failed to login: "+res);
 	 }
 	});
 }
 var windows = ["employeeWindow", "distributorwindow"];
 function showWindow(displayWindow){
 	for(var i in windows){
 		$("#"+windows[i]).hide();
 	}
 	$("#"+displayWindow).show();	 	
 }
 
 function updateDistributor(){
 	$(".distributorName").html(loginResult.name);
 	$(".distributorId").html(loginResult.distributor_id);
 	$('#submitOrder').button();
 	initDistributor();
 }
 
 function callPostRequest(url, method, requestbody, callback){
   var xhr = new XMLHttpRequest();
   xhr.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
    	callback(this.responseText);
    }
  };
   xhr.open(method, url);
   xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
   xhr.send(JSON.stringify(requestbody));
}
var show;
function showLogout(){
	if(show){
		$("#menu").hide();
		show=false;
	} else {
		$("#menu").show();
		show=true;
	}
}

function openDefaultTab(defaultTab) {
	document.getElementById(defaultTab).click();
}

function clickLogout(){
	showLogout();
	$("#loginwindow").show();
	$("#homeWindow").hide();
}