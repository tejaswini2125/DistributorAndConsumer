/**
 * 
 */
function initDistributor() {
	callPostRequest("/product", "get", {}, function(res) {
		var result = eval("[" + res + "]")[0];
		if (!result.errorMessage) {
			$("#productItems").html("");
			$("#productItems").append(
					getProductJTable(result, function(object, rows) {
						var total = addAmount("productItems", object, rows);
						$("#total-bill").html(total);
					}));
			callPostRequest("/distributor/order", "post", {
				distributorId : loginResult.distributor_id
			}, function(res) {
				updateOrderPage(res, "submitOrder", "productItems")
			});
		} else {
			$("#loginerror").show();
			$("#loginerror").html("Failed: " + res.errorMessage);
		}
	});
}
var keepOrderTrack = {};

function addAmount(id, object, rows) {
	var index = parseInt(object.getAttribute("index"));
	var productId = object.getAttribute("productId");
	var quantity = parseInt($(object).val());
	var saleAmount = parseFloat(rows[index]["sale price"]);
	$("#" + id + " [name='" + productId + "-amount']").html(
			(saleAmount * quantity).toFixed(2) + "");
	var sum = 0;
	$("#" + id + " .each-product-amount").each(function(index, doc) {
		sum += parseFloat($(doc).html());
	});

	return sum.toFixed(2)
}

function submitOrder() {
	var orderedQuantity = [];
	$("#productItems table tbody tr").each(function(index, doc) {
		var productId = $(doc).attr("productId");
		var quantity = $(doc).find("[name='quantity-product']").val();
		if (quantity) {
			orderedQuantity.push({
				productId : productId,
				quantity : parseInt(quantity)
			});
		}

	});
	callPostRequest("/distributor/order", "put", {
		distributorId : loginResult.distributor_id,
		saleOrder : orderedQuantity
	}, function(res) {
		updateOrderPage(res, "submitOrder", "productItems")
	})
}

function returnOrder() {
	var returnQuantity = [];
	$("#returnProductItems table tbody tr").each(function(index, doc) {
		var productId = $(doc).attr("productId");
		var quantity = $(doc).find("[name='quantity-product']").val();
		if (quantity) {
			returnQuantity.push({
				productId : productId,
				quantity : parseInt(quantity)
			});
		}
	});

	callPostRequest("/distributor/return", "put", {
		distributorId : loginResult.distributor_id,
		saleOrder : returnQuantity
	}, function(res) {
		updateOrderPage(res, "returnOrder", "returnProductItems")
	});

}

function updateOrderPage(res, buttonId, containerId) {
	var result = eval("[" + res + "]")[0];
	if (!result.errorMessage) {
		if (result.rows && result.rows.length > 0) {
			$("#" + buttonId).prop('disabled', true);
			$("#" + buttonId).addClass("disabled")
			var rows = result.rows;
			for (var i = 0; i < rows.length; i++) {
				if (i == 0) {
					$("#orderDetails").show();
					$("#orderId").html(rows[i]["order_id"]);
					$("#orderDate").html(rows[i]["ordered_date"]);
					$("#expectedDate").html(rows[i]["expected_delivery_date"]);
				}
				var inputNum = $(
						"#" + containerId + " [productId='"
								+ rows[i]["product_id"] + "']").find(
						"[name='quantity-product']");
				inputNum.val(rows[i]["quantity"]);
				inputNum.change();
			}
			$("#" + containerId).find("[name='quantity-product']").prop(
					'disabled', true);
		} else {
			$("#orderDetails").hide();
			$("#" + buttonId).prop('disabled', false);
			$("#" + buttonId).removeClass("disabled");
			$("#" + containerId).find("[name='quantity-product']").prop(
					'disabled', false);
			$("#" + containerId).find("[name='quantity-product']").val("0");
			$("#" + containerId).find("[name='quantity-product']").change();
			$("#" + containerId).find("[name='quantity-product']").val("");
		}
	} else {
		alert(result.errorMessage);
	}
}

function receivedDetails() {
	callPostRequest("/product", "get", {}, function(res) {
		var result = eval("[" + res + "]")[0];
		if (!result.errorMessage) {
			$("#receivedProductItems").html("");
			$("#receivedProductItems").append(
					getProductJTable(result, function(object, rows) {
						var total = addAmount("receivedProductItems", object,
								rows);
						$("#total-received-bill").html(total);
					}));
			callPostRequest("/distributor/assign", "post", {
				distributorId : loginResult.distributor_id
			}, function(res) {
				updateOrderPage(res, "receiveOrder", "receivedProductItems");
			});
		} else {
			$("#loginerror").show();
			$("#loginerror").html("Failed: " + res.errorMessage);
		}
	});
}

function returnDetails() {
	callPostRequest("/product", "get", {}, function(res) {
		var result = eval("[" + res + "]")[0];
		if (!result.errorMessage) {
			$("#returnProductItems").html("");
			$("#returnProductItems").append(
					getProductJTable(result, function(object, rows) {
						var total = addAmount("returnProductItems", object,
								rows);
						$("#total-return-bill").html(total)
					}));
			callPostRequest("/distributor/return", "post", {
				distributorId : loginResult.distributor_id
			}, function(res) {
				updateOrderPage(res, "returnOrder", "returnProductItems");
			});
		} else {
			$("#loginerror").show();
			$("#loginerror").html("Failed: " + res.errorMessage);
		}
	});
}

function assignOrderDetails() {
	callPostRequest("/distributors", "get", {}, function(res) {
		var result = eval("[" + res + "]")[0];
		if (!result.errorMessage) {
			$(".distributors-name").html("");
			var rows = result.rows;
			if (rows) {
				for (var i = 0; i < rows.length; i++) {
					var option = $("<option>").attr({
						value : rows[i].distributor_id,

					}).html(rows[i].name)
					$(".distributors-name").append(option)
				}
				getOrderDetails(rows[0].distributor_id)
			}
		}

	});
}

function getOrderDetails(id) {
	callPostRequest("/product", "get", {}, function(res) {
		var result = eval("[" + res + "]")[0];
		if (!result.errorMessage) {
			$("#order-assign-tbl").html("");
			$("#order-assign-tbl").append(
					getProductJTable(result, function(object, rows) {
						var total = addAmount("order-assign-tbl", object, rows)
						$("#total-assign-amount").html(total);
					}));
			callPostRequest("/distributor/order", "post", {
				distributorId : id
			}, function(res) {
				updateOrderPage(res, "assignOrder", "order-assign-tbl");
				$("#order-assign-tbl").find("[name='quantity-product']").prop(
						'disabled', false);

				$("#assignOrder").prop('disabled', false);
				$("#assignOrder").removeClass("disabled");
			});
		}
	});
}

function assignOrder() {
	var distributorId = $("#assign-distributor-id").val();
	var assignOrderQuantity = [];
	$("#order-assign-tbl table tbody tr").each(function(index, doc) {
		var productId = $(doc).attr("productId");
		var quantity = $(doc).find("[name='quantity-product']").val();
		if (quantity) {
			assignOrderQuantity.push({
				productId : productId,
				quantity : parseInt(quantity)
			});
		}
	});

	callPostRequest("/distributor/assign", "put", {
		distributorId : distributorId,
		saleOrder : assignOrderQuantity
	}, function(res) {
		updateOrderPage(res, "assignOrder", "order-assign-tbl");
		/*
		 * $("#order-assign-tbl").find("[name='quantity-product']").prop(
		 * 'disabled', false);
		 * 
		 * $("#assignOrder").prop('disabled', false);
		 * $("#assignOrder").removeClass("disabled");
		 */
	});

}

function billSummary() {

	callPostRequest("/orders/bill", "post", {
		distributorId : loginResult.distributor_id
	}, function(res) {
		var result = eval("[" + res + "]")[0];
		if (!result.errorMessage) {
			$("#billItems").html("");
			$("#billItems").append(getSalesSummaryTable(result));
			var rows = result.rows;
			var sum = 0;
			for(var i=0; i< rows.length; i++) {
				sum += parseFloat(rows[i]["total amount"]);
			}
			$("#total-bill-amount").html(sum.toFixed(2)+"");
		}
	})
}
