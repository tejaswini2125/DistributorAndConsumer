/**
 * 
 */
function initEmployee() {
	callPostRequest("/orders/summary", "get", {}, function(res) {
		var result = eval("[" + res + "]")[0];
		if (!result.errorMessage) {
			$("#order-details-tbl").html("");
			$("#order-details-tbl").append(getSalesSummaryTable(result));
			$(".employeeName").html(loginResult.name);
			$(".employeeId").html(loginResult.employee_id);
		}
	});
}

function missedDistributor() {
	callPostRequest("/distributors/missedorder", "get", {}, function(res) {
		var result = eval("[" + res + "]")[0];
		if (!result.errorMessage) {
			$("#distributor-missed-tbl").html("");
			$("#distributor-missed-tbl").append(getSalesSummaryTable(result));
			$(".employeeName").html(loginResult.name);
			$(".employeeId").html(loginResult.employee_id);
		}
	});
}

function distributorMissed(event){
	operHorizontalTab(event, 'sale-details');
	missedDistributor();
}

function getSalesSummaryTable(result) {
	var table = $("<table>").attr({
		class : 'table table-condensed'
	});
	var thead = $("<thead>");
	table.append(thead);
	var tr = $("<tr>");
	thead.append(tr);
	for (var i = 0; i < result["columns"].length; i++) {
		var th = $("<th>").attr({
			class : 'text-center'
		}).html(result["columns"][i]);
		tr.append(th);
	}
	
	var tbody = $("<tbody>")
	table.append(tbody);
	var rows = result.rows;
	for (var r = 0; r < rows.length; r++) {
		var productId = rows[r][result["columns"][0]];
		tr = $("<tr>").attr({
			productId : productId
		});

		for (var c = 0; c < result["columns"].length; c++) {
			var value = rows[r][result["columns"][c]];
			var td = $("<td>").attr({
				class : 'text-center'
			}).html(value);
			tr.append(td);
		}
		tbody.append(tr)
	}
	return table;
}
