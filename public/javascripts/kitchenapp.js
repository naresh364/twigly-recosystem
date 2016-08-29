'use strict';

var app = angular.module('kitchenapp', ['ngWebsocket']);

var host = window.location.hostname;
var wshost = 'wss://'+host+'/admin/orders/ws';
if (window.location.port != "") {
	wshost = 'ws://'+host+':'+window.location.port+'/admin/orders/ws';
}

var shouldReload = false;

app.directive('dish', function($websocket) {
	return {
		restrict: 'E',
		require: 'ngModel',
		scope: {
			dish: '=ngModel'
		},
		template: '<div ng-if="dish.entries.length>1">'+
						'<table class="table card">'+
							'<tr>'+
								'<td style="padding: 0px;">'+
									'<table class="table" style="margin-bottom: 0px;">'+
										'<tr ng-repeat="dishdetail in dish.entries | filter: cookingEntryFilter" class="{{dishdetail.optionName}}">'+
											'<td width="50%"><span ng-if="dishdetail.isSide"><span class="fa fa-angle-right"></span></span> {{dishdetail.itemName}} ({{dishdetail.orderId}})</td>'+
											'<td width="10%" style="text-align: left;">{{dishdetail.quantity}}</td>'+
											'<td width="20%" style="text-transform: uppercase; font-weight: bold;"><span ng-if="dishdetail.isSide"><span class="fa fa-angle-right"></span></span> {{dishdetail.optionName}}</td>'+
											'<td width="20%" style="text-align: right;">'+
												'<button ng-if="dishdetail.status!=1" class="btn btn-success" style="background: #44917b;" ng-click="dishdetail.status=1;markDone(dishdetail.entryId)">Done</button>'+
												'<img ng-if="dishdetail.status==1" ng-src="/assets/img/loading-sm.gif">'+
											'</td>'+
										'</tr>'+
									'</table>'+
								'</td>'+
							'</tr>'+
						'</table>'+
					'</div>'+
					'<div ng-if="dish.entries.length==1">'+
						'<table class="table">'+
							'<tr class="card">'+
								'<td width="40%" class="cardheader" style="border-right: 2px solid #CCC;">{{dish.size}} x <br> {{dish.name}}</td>'+
								'<td width="36%">Order No.: {{dish.entries[0].orderId}}</td>'+
								'<td width="12%" class="carditemcount" style="text-align: right;">{{dish.size}}</td>'+
								'<td width="12%" style="text-align: right;">'+
									'<button ng-if="dish.entries[0].status!=1" class="btn btn-success" style="background: #44917b;" ng-click="dish.entries[0].status=1;markDone(dish.entries[0].entryId)">Done</button>'+
									'<img ng-if="orderDetails.data[0].status==1" ng-src="/assets/img/loading-sm.gif">'+
								'</td>'+
							'</tr>'+
						'</table>'+
					'</div>',
		link: function(scope) {
			scope.markDone = function(entryId) {
				var ws = $websocket.$get(wshost);
				ws.$emit('MARK_COOKING_ITEM_DONE', {"entryId": entryId});
			}

			scope.cookingEntryFilter = function(item) {
				return item.showOnCookingManager == true && item.isCookingDone == false;
			}
		}
	}
});


app.directive('sandwich', function($websocket) {
	return {
		restrict: 'E',
		require: 'ngModel',
		scope: {
			dish: '=ngModel'
		},
		template: '<div ng-if="dish.entries.length>1">'+
						'<table class="table card">'+
							'<tr>'+
								'<td style="padding: 0px;">'+
									'<table class="table" style="margin-bottom: 0px;">'+
										'<tr ng-repeat="dishdetail in dish.entries | filter: cookingEntryFilter" class="{{dishdetail.optionName}}">'+
											'<td width="20%" style="text-align: right;">'+
												'<button ng-if="dishdetail.status!=1" class="btn btn-success" style="background: #44917b;" ng-click="dishdetail.status=1;markDone(dishdetail.entryId)">Done</button>'+
//												'<img ng-if="dishdetail.status!=1" ng-src="/assets/img/arrow.png" ng-swipe-right="dishdetail.status=1;markDone(dishdetail.entryId)">'+
                                                '<img ng-if="dishdetail.status==1" ng-src="/assets/img/loading-sm.gif">'+
											'</td>'+
											'<td width="50%"><span ng-if="dishdetail.isSide"><span class="fa fa-angle-right"></span></span> {{dishdetail.itemName}} ({{dishdetail.orderId}})</td>'+
											'<td width="10%" style="text-align: left;">{{dishdetail.quantity}}</td>'+
											'<td width="20%" style="text-transform: uppercase; font-weight: bold;"><span ng-if="dishdetail.isSide"><span class="fa fa-angle-right"></span></span> {{dishdetail.optionName}}</td>'+
										'</tr>'+
									'</table>'+
								'</td>'+
							'</tr>'+
						'</table>'+
					'</div>'+
					'<div ng-if="dish.entries.length==1">'+
						'<table class="table">'+
							'<tr class="card">'+
								'<td width="40%" class="cardheader" style="border-right: 2px solid #CCC;">{{dish.size}} x <br> {{dish.name}}</td>'+
								'<td width="36%">Order No.: {{dish.entries[0].orderId}}</td>'+
								'<td width="12%" class="carditemcount" style="text-align: right;">{{dish.size}}</td>'+
								'<td width="12%" style="text-align: right;">'+
									'<button ng-if="dish.entries[0].status!=1" class="btn btn-success" style="background: #44917b;" ng-click="dish.entries[0].status=1;markDone(dish.entries[0].entryId)">Done</button>'+
//									'<img ng-if="dishdetail.status!=1" ng-src="/assets/img/arrow.png" class="btn btn-success" style="background: #44917b;" ng-swipe-right="dishdetail.status=1;markDone(dishdetail.entryId)">'+
									'<img ng-if="orderDetails.data[0].status==1" ng-src="/assets/img/loading-sm.gif">'+
								'</td>'+
							'</tr>'+
						'</table>'+
					'</div>',
		link: function(scope) {
			scope.markDone = function(entryId) {
				var ws = $websocket.$get(wshost);
				ws.$emit('MARK_COOKING_ITEM_DONE', {"entryId": entryId});
			}

			scope.cookingEntryFilter = function(item) {
				return item.showOnCookingManager == true && item.isCookingDone == false;
			}
		}
	}
});

app.directive('order', function($websocket) {
	return {
		restrict: 'E',
		require: 'ngModel',
		scope: {
			order: '=ngModel'
		},
		template: '<div class="panel panel-default">'+
			        '<div class="panel-heading" style="border: 0px !important;">'+
			            '<div class="row">'+
			                '<strong>'+
			                    '<div class="col-sm-2">#{{order.associateId}}</div>'+
			                    '<div class="col-sm-6 {{order.is_first_order}}">{{order.name}} ({{order.mobile_number}})</div>'+
			                    '<div class="col-sm-2">{{order.addTime | date: "h:mm a"}}</div>'+
			                    '<div class="col-sm-2" ng-class="{late: order.late}"><span class="fa fa-clock-o"></span> {{order.elapsedTime}}</div>'+
			                '</strong>'+
			            '</div>'+
			        '</div>'+
			        '<div class="panel-body" style="padding: 0px;">'+
			            '<div class="container-fluid">'+
			                '<table class="table" style="margin-bottom: 0px;">'+
			                    '<tbody>'+
			                        '<tr ng-repeat="entry in order.entries | filter: cookingEntryFilter"  class="{{entry.optionName}}">'+
			                            '<div ng-if="entry.showOnCookingManager">'+
				                            '<td><span ng-if="entry.isSide"><span class="fa fa-angle-right"></span></span> {{entry.itemName}}</td>'+
				                            '<td style="text-align: left;">{{entry.quantity}}</td>'+
				                            '<td style="text-align: left;">{{entry.optionName}}</td>'+
				                            '<td style="text-align: center;">'+
			                                    '<div ng-if="entry.isCookingDone"><span class="fa fa-check" style="color: #44917b;"></span></div>'+
			                                    '<div ng-if="!entry.isCookingDone"><span class="fa fa-circle-o" style="color: #884400;"></span></div>'+
				                            '</td>'+
				                        '<div>'+
			                        '</tr>'+
			                    '</tbody>'+
			                '</table>'+
			            '</div>'+
			        '</div>'+
			    '</div>',
		link: function(scope) {
			scope.cookingEntryFilter = function(item) {
				return item.showOnCookingManager == true;
			}
		}
	}
});

app.controller('kitchenController', function($scope, $http, $window, $websocket, $interval) {
	$scope.dishdetails = [];
	$scope.orderdetails = [];

	$scope.ws = $websocket.$new({
        url: wshost,
        reconnect: true,
        reconnectInterval: 10000
    });

    $http.get('/admin/orders/getCookingOrders')
    	.then(function(response) {
    		$scope.dishdetails = response.data.cookingItemCards;
    		$scope.orderdetails = response.data.cookingOrderCards;
    		updateElapsed();
	});

    $scope.ws.$on('$message', function(message) {
    	$window.console.log(message);
    });

    $scope.safeApply = function(fn) {
	  	var phase = this.$root.$$phase;
	  	if(phase == '$apply' || phase == '$digest') {
	    	if(fn && (typeof(fn) === 'function')) {
	      		fn();
	    	}
	  	} else {
	    	this.$apply(fn);
	  	}
	};

	$scope.ws.$on('ADD_COOKING_ITEM_CARD', function(message) {
		$scope.safeApply(function() {
			$scope.dishdetails.push(message);
			$window.console.log($scope.dishdetails);
		});
	});
	
	$scope.ws.$on('ADD_COOKING_ITEM_ENTRY', function(message) {
		for (var i=0; i<$scope.dishdetails.length; i++) {
			if ($scope.dishdetails[i].cardId == message.cardId) {
				$scope.safeApply(function() {
					$scope.dishdetails[i].entries = $scope.dishdetails[i].entries.concat(message.entries);
					$scope.dishdetails[i].size += message.size;
				});
				break;
			}
		}
	});

	$scope.ws.$on('ADD_COOKING_ORDER_ENTRY', function(message) {
		for (var i=0; i<$scope.orderdetails.length; i++) {
			if ($scope.orderdetails[i].cardId == message.cardId) {
				$scope.safeApply(function() {
					$scope.orderdetails[i].entries = $scope.orderdetails[i].entries.concat(message.entries);
				});
				break;
			}
		}
	});

	$scope.ws.$on('REMOVE_CARD', function(message) {
		if (message.type == "COOKING_ITEM") {
			for (var i=0; i<$scope.dishdetails.length; i++) {
				if ($scope.dishdetails[i].cardId == message.cardId) {
					$scope.safeApply(function() {
						$scope.dishdetails.splice(i, 1);
					});
					break;
				}
			}
		} else if (message.type == "COOKING_ORDER") {
			for (var i=0; i<$scope.orderdetails.length; i++) {
				if ($scope.orderdetails[i].cardId == message.cardId) {
					$scope.safeApply(function() {
						$scope.orderdetails.splice(i, 1);
					});
					break;
				}
			}
		}
	});

	$scope.ws.$on('REMOVE_ENTRY', function(message) {
		if (message.type == "COOKING_ITEM") {
			var found = false;
			for (var i=0; i<$scope.dishdetails.length; i++) {
				if ($scope.dishdetails[i].cardId == message.cardId) {
					var entrytoremove = null;
					var otherentries = 0;
					for (var j=0; j<$scope.dishdetails[i].entries.length; j++) {
						if ($scope.dishdetails[i].entries[j].entryId == message.entryId) {
							found = true;
							entrytoremove = j;
						}
						if ($scope.dishdetails[i].entries[j].entryId.split("_")[0] == message.entryId.split("_")[0]) {
							otherentries++;
						}
					}
					if (entrytoremove != null && entrytoremove > -1) {
						$scope.safeApply(function() {
							if (otherentries == 1) {
								$scope.dishdetails[i].size -= $scope.dishdetails[i].entries[entrytoremove].quantity;
							}
							$scope.dishdetails[i].entries.splice(entrytoremove, 1);
						});
					}
				}
				if (found) {
					break;
				}
			}
		} else if (message.type == "COOKING_ORDER") {
			var found = false;
			for (var i=0; i<$scope.orderdetails.length; i++) {
				if ($scope.orderdetails[i].cardId == message.cardId) {
					for (var j=0; j<$scope.orderdetails[i].entries.length; j++) {
						if ($scope.orderdetails[i].entries[j].entryId == message.entryId) {
							$scope.safeApply(function() {
								$scope.orderdetails[i].entries.splice(j, 1);
							});
							found = true;
							break;
						}
					}
				}
				if (found) {
					break;
				}
			}
		}
	});

	$scope.ws.$on('MARK_COOKING_ITEM_DONE', function(message) {
		if (message.type == "COOKING_ORDER") {
			var found = false;
			for (var i=0; i<$scope.orderdetails.length; i++) {
					if ($scope.orderdetails[i].cardId == message.cardId) {
						for (var j=0; j<$scope.orderdetails[i].entries.length;j++) {
							if ($scope.orderdetails[i].entries[j].entryId == message.entryId) {
								$scope.safeApply(function() {
									$scope.orderdetails[i].entries[j].isCookingDone = true;
								});
								found = true;
								break;
							}
						}
					}
				if (found) {
					break;
				}
			}
		} else if (message.type == "COOKING_ITEM") {
			var found = false;
			for (var i=0; i<$scope.dishdetails.length; i++) {
				if ($scope.dishdetails[i].cardId == message.cardId) {
					var entrytoremove = null;
					var otherentries = 0;
					for (var j=0; j<$scope.dishdetails[i].entries.length; j++) {
						if ($scope.dishdetails[i].entries[j].entryId == message.entryId) {
							found = true;
							entrytoremove = j;
						}
						if ($scope.dishdetails[i].entries[j].entryId.split("_")[0] == message.entryId.split("_")[0]) {
							if ($scope.dishdetails[i].entries[j].showOnCookingManager) {
								otherentries++;
							}
						}
					}
					if (entrytoremove != null && entrytoremove > -1) {
						$scope.safeApply(function() {
							if (otherentries == 1) {
								$scope.dishdetails[i].size -= $scope.dishdetails[i].entries[entrytoremove].quantity;
							}
							$scope.dishdetails[i].entries.splice(entrytoremove, 1);
						});
					}
				}
				if (found) {
					break;
				}
			}
		}
	});

	$scope.ws.$on('ADD_COOKING_ORDER_CARD', function(message) {
		$scope.safeApply(function() {
			$scope.orderdetails.push(message);
		});
	});

	$scope.ws.$on('PING', function(message) {
		var ws = $websocket.$get(wshost);
		ws.$emit('PONG', "PONG");
		shouldReload = false;
	});

	$scope.ws.$on('RELOAD', function(message) {
		$window.location.reload();
		shouldReload = false;
	});

	$scope.safeApply = function(fn) {
	  	var phase = this.$root.$$phase;
	  	if(phase == '$apply' || phase == '$digest') {
	    	if(fn && (typeof(fn) === 'function')) {
	      		fn();
	    	}
	  	} else {
	    	this.$apply(fn);
	  	}
	};

	var playsound = function(name) {
		var thissound = document.getElementById(name);
		thissound.play();
	}

	var callForReload = function() {
		if (shouldReload) {
			$window.location.reload();
		}
		console.log("should reload = "+shouldReload);
		shouldReload = true;
	}

	var updateElapsed = function() {
		var currenttime = new Date();
		var buzzer = false;
		for (var i=0; i<$scope.orderdetails.length; i++) {
			var expectedDispatchTime = new Date($scope.orderdetails[i].expectedDispatchTime);
			$scope.safeApply(function() {
				$scope.orderdetails[i].elapsedTime = ((expectedDispatchTime-currenttime)/60/1000).toFixed(0);
				if ($scope.orderdetails[i].elapsedTime <= 5 && $scope.orderdetails[i].late != true) {
					$scope.orderdetails[i].late = true;
					buzzer = true;
				}
			});
		}
		if (buzzer) {
			playsound('late');
		}

	}
	var timeUpdate = setInterval(updateElapsed, 60000);

	var checkForReload = setInterval(callForReload, 30000);

	$scope.getCookingTotal = function(inputList) {
		var total = 0;
		for (var i=0; i<inputList.length; i++) {
			if (!inputList[i].allItemsCooked) {
				total += inputList[i].size;
			}
		}
		return total;
	}

	$scope.getCookingTotalSandwiches = function(inputList) {
		var total = 0;
		for (var i=0; i<inputList.length; i++) {
			if (!inputList[i].allItemsCooked && inputList[i].isSandwich) {
				total += inputList[i].size;
			}
		}
		return total;
	}

	$scope.getOrderTotal = function(inputList) {
		var total = 0;
		for (var i=0; i<inputList.length; i++) {
			if (!inputList[i].allItemsCooked) {
				total++;
			}
		}
		return total;
	}


});
