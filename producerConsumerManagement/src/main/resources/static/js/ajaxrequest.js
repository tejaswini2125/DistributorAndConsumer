
function billChange(item) {
	var id = item.value;
	$("#invoiceNum").html("#" + id + "876" + id)
	billingSummary("bill-summary", "loading...", "/billsummary?id=" + id);
	$('#tele-customer-name').html(item.label);
	callAjax("/currenttarifplan?planid=" + id, "GET", function(response) {
		var json = JSON.parse(response);
		var rows = json["rows"];
		for (var i = 0; i < rows.length; i++) {
			$("#tele-phone-num").html(rows[i]["MOBILE_NO"]);
			$("#tele-tariff-plan").html(rows[i]["TARIFF_PLAN"]);
			$("#tele-customer-id").html(rows[i]["CUSTOMER_ID"]);
			$("#tele-balance").html("&#x20B9;" + rows[i]["BALANCE"]);
		}
	});
}

function executeQuery() {

	var val = document.getElementById("query").value;
	render("advancequery", "loading..", "/advancequery?query=" + val);
}

function callAjax(path, type, callback) {
	var xhttp;
	xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			callback(this.responseText);
		}
	};
	xhttp.open(type, path, true);
	xhttp.send();

}

function render(id, loading, path, chartType, chartId) {
	document.getElementById(id).innerHTML = loading;
	callAjax(path, "GET", function(response) {
		document.getElementById(id).innerHTML = createTable(response);
		if (chartType === "pie") {
			pieChart(chartId, "duration", piePrepare(response));
		} else if (chartType === "line") {
			lineChart(chartId, linePrepare(response), chartType);
		} else if (chartType === "bar") {
			lineChart(chartId, linePrepare(response));
		}
	});

}

function billingSummary(id, loading, path) {
	document.getElementById(id).innerHTML = loading;
	callAjax(path, "GET", function(response) {
		document.getElementById(id).innerHTML = createSummaryTable(response);
	});

}
var usageDetails;
function createSummaryTable(jsonString) {
	var json = JSON.parse(jsonString);
	var tableStructure = "<table class='table table-condensed'>";
	tableStructure += "<thead><tr>";
	for (var i = 0; i < json["columns"].length; i++) {
		tableStructure += "<th class='text-center'>" + json["columns"][i]
				+ "</th>";
	}
	tableStructure += "</tr></thead><tbody>";
	var rows = json["rows"];
	usageDetails = {
		calls : {
			count : 0,
			duration : 0,
			cost : 0
		},
		sms : {
			count : 0,
			duration : 0,
			cost : 0
		},
		data : {
			count : 0,
			duration : 0,
			cost : 0
		}
	}
	var callTypeIndex = "Call Type";
	var costIndex = "Cost";
	var durationIndex = "Duration";

	for (var h = 0; h < json["rows"].length; h++) {
		tableStructure += "<tr>";
		for (var i = 0; i < json["columns"].length; i++) {
			var value = rows[h][json["columns"][i]];
			tableStructure += "<td class='text-center'>" + value + "</td>";
			column = json["columns"][i];
			callType = rows[h][callTypeIndex]
			if (callType == "call") {
				if (column == callTypeIndex) {
					usageDetails.calls.count++;
				}
				if (column == costIndex) {
					usageDetails.calls.cost += parseFloat(value);
				}
				if (column == durationIndex) {
					usageDetails.calls.duration += parseFloat(value);
				}
			} else if (callType == "sms") {
				if (column == callTypeIndex) {
					usageDetails.sms.count++;
				}
				if (column == costIndex) {
					usageDetails.sms.cost += parseFloat(value);
				}

			} else if (callType == "data") {
				if (column == callTypeIndex) {
					usageDetails.data.count++;
				}
				if (column == costIndex) {
					usageDetails.data.cost += parseFloat(value);
				}
				if (column == durationIndex) {
					usageDetails.data.duration += parseFloat(value);
				}
			}

		}
		tableStructure += "</tr>";
	}
	tableStructure += "<tr>";
	for (var i = 0; i < json["columns"].length - 2; i++) {
		tableStructure += "<td class='highrow'></td>";
	}
	var total = usageDetails.calls.cost + usageDetails.sms.cost
			+ usageDetails.data.cost;
	tableStructure += "<td class='highrow text-center'><strong>Subtotal</strong></td>";
	tableStructure += "<td class='highrow text-right'>&#x20B9;" + total
			+ "</td>";
	tableStructure += "</tr>";

	tableStructure += "<tr>";
	for (var i = 0; i < json["columns"].length - 2; i++) {
		tableStructure += "<td class='emptyrow'></td>";
	}

	tableStructure += "<td class='emptyrow text-center'><strong>Tax</strong></td>";
	tableStructure += "<td class='emptyrow text-right'>&#x20B9;"
			+ (total * 0.18 + 0) + "</td>";
	tableStructure += "</tr>";

	tableStructure += "<tr>";
	for (var i = 0; i < json["columns"].length - 2; i++) {
		tableStructure += "<td class='emptyrow'></td>";
	}
	var grandTotal = (total + total * 0.18);
	tableStructure += "<td class='emptyrow text-center'><strong>Total</strong></td>";
	tableStructure += "<td class='emptyrow text-right'>&#x20B9;" + grandTotal
			+ "</td>";
	tableStructure += "</tr>";
	$("#total-bill").html(grandTotal);
	$("#tele-calls-count").html(usageDetails.calls.count);
	$("#tele-sms-count").html(usageDetails.sms.count);
	$("#tele-data-count").html(usageDetails.data.count);

	$("#tele-calls-duration").html(usageDetails.calls.duration);
	$("#tele-data-duration").html(usageDetails.data.duration);

	tableStructure += "<tbody></table>";
	return tableStructure;
}

function createTable(jsonString) {
	var json = JSON.parse(jsonString);
	var tableStructure = "<table>";
	tableStructure += "<thead><tr>";
	for (var i = 0; i < json["columns"].length; i++) {
		tableStructure += "<th>" + json["columns"][i] + "</th>";
	}
	tableStructure += "</tr></thead><tbody>";
	var rows = json["rows"];
	for (var h = 0; h < json["rows"].length; h++) {
		tableStructure += "<tr>";
		for (var i = 0; i < json["columns"].length; i++) {
			tableStructure += "<td>" + rows[h][json["columns"][i]] + "</td>";
		}
		tableStructure += "</tr>";
	}

	tableStructure += "<tbody></table>";
	return tableStructure;
}

function operHorizontalTab(evt, cityName) {
	var i, tabcontent, tablinks;
	tabcontent = document.getElementsByClassName("tabcontent");
	for (i = 0; i < tabcontent.length; i++) {
		tabcontent[i].style.display = "none";
	}
	tablinks = document.getElementsByClassName("tablinks");
	for (i = 0; i < tablinks.length; i++) {
		tablinks[i].className = tablinks[i].className.replace(" active", "");
	}
	document.getElementById(cityName).style.display = "block";
	evt.currentTarget.className += " active";
}
