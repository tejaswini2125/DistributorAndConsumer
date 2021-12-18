/**
 * 
 */

function getProductJTable(result, callback) {
	var table = $("<table>").attr({
		class : 'table table-condensed'
	});
	var thead = $("<thead>");
	table.append(thead);
	var tr = $("<tr>");
	thead.append(tr);
	for (var i = 1; i < result["columns"].length; i++) {
		var th = $("<th>").attr({
			class : 'text-center'
		}).html(result["columns"][i]);
		tr.append(th);
	}
	var th1 = $("<th>").attr({
		class : 'text-center'
	}).html("Quantity");
	tr.append(th1);
	var th2 = $("<th>").attr({
		class : 'text-center'
	}).html("Amount");
	tr.append(th2);
	var tbody = $("<tbody>")
	table.append(tbody);
	var rows = result.rows;
	for (var r = 0; r < rows.length; r++) {
		var productId = rows[r][result["columns"][0]];
		tr = $("<tr>").attr({
			productId : productId
		});

		for (var c = 1; c < result["columns"].length; c++) {
			var value = rows[r][result["columns"][c]];
			var td = $("<td>").attr({
				class : 'text-center'
			}).html(value);
			tr.append(td);
		}
		var inputNum = $("<input>").attr({
			type : 'number',
			min : 0,
			style : "width:60px",
			index : r,
			productId : productId,
			name : 'quantity-product'
		}).addClass("form-control quantity").change(function(e) {
			callback(this, rows);
		});

		td = $("<td>").attr({
			class : 'text-center'
		}).append(inputNum);
		tr.append(td);
		td = $("<td>").attr({
			class : 'text-right each-product-amount',
			name : productId + "-amount"
		}).append("0");
		tr.append(td);
		tbody.append(tr)
	}
	return table;
}
