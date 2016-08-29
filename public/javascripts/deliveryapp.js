'use strict';

var app = angular.module('deliveryapp', ['ngWebsocket']);

var wshost = "ws://localhost:9000/admin/orders/ws";

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
			                    '<div class="col-sm-12">{{order.address}}</div>'+
			                '</strong>'+
			            '</div>'+
			        '</div>'+
			        '<div class="panel-body" style="padding: 0px;">'+
			            '<div class="container-fluid">'+
			                '<table class="table table-striped" style="margin-bottom: 0px;">'+
			                    '<tbody>'+
			                        '<tr ng-repeat="entry in order.entries">'+
			                        	'<div ng-if="dishdetail.showOnDispatchingManager">'+
				                            '<td><span ng-if="entry.isSide" class="fa fa-angle-right"></span> {{entry.itemName}}</td>'+
				                            '<td style="text-align: center;">{{entry.quantity}}</td>'+
				                            '<td style="text-align: center;" ng-if="order.type == \'DISPATCHING_ORDER\'">'+
			                                    '<div ng-if="entry.isDone"><span class="fa fa-check" style="color: #44917b;"></span></div>'+
			                                    '<div ng-if="!entry.isDone"><span class="fa fa-circle-o" style="color: #884400;"></span></div>'+
				                            '</td>'+
				                        '</div>'+
			                        '</tr>'+
			                    '</tbody>'+
			                '</table>'+
			            '</div>'+
			            '<div style="text-align: center; width: 100%; margin-bottom: 15px; font-size: 18px;" ng-if="order.type==\'RECEIVED_ORDER\'">'+
			            	'<button class="btn btn-danger" style="padding: 18px; width: 45%; margin: 0px 5px;" ng-click="acceptOrder(order.assocateId, true)">Priority</button>'+
			            	'<button class="btn btn-primary" style="padding: 18px; width: 45%; margin: 0px 5px;" ng-click="acceptOrder(order.assocateId, false)">Accept</button>'+
			            '</div>'+
			            '<div style="text-align: center; width: 100%; margin-bottom: 15px; font-size: 18px;" ng-if="order.type==\'DISPATCHING_ORDER\' && checkOrderStatus($index)">'+
			            	'<button class="btn btn-success" style="padding: 18px; width: 45%; margin: 0px 5px;" ng-click="acceptOrder(order.assocateId, true)">Done</button>'+
			            '</div>'+
			        '</div>'+
			    '</div>'
		,
		link: function(scope) {
			scope.acceptOrder = function(orderId, priority) {
				var ws = $websocket.$get(wshost);
				if (priority) {
					ws.$emit('ACCEPT_ORDER_PRIORITY', {"orderId": orderId});
				} else {
					ws.$emit('ACCEPT_ORDER', {"orderId": orderId});
				}
			}

			scope.checkOrderStatus = function() {
				var orderDone = true;
				for (var i=0; i<scope.order.entries.length; i++) {
					if (scope.order.entries[i].isDone == false) {
						orderDone = false;
						break;
					}
				}
				return orderDone;
			}
		}
	}
});

app.controller('kitchenController', function($scope, $http, $window, $websocket) {
	$scope.incomingorders = [];
	$scope.outgoingorders = [];

	var ws = $websocket.$new({
        url: wshost,
    });

    $http.get('/admin/orders/getDispatcherOrders')
    	.then(function(response) {
    		$scope.incomingorders = response.data.receivedOrderCards;
    		$scope.outgoingorders = response.data.dispatchingOrderCards;
    		updateElapsed();
	});

	ws.$on('REMOVE_CARD', function(message) {
		if (message.type == "COOKING_ITEM") {
			for (var i=0; i<$scope.incomingorders.length; i++) {
				if ($scope.incomingorders[i].cardId == message.cardId) {
					$scope.$apply(function() {
						$scope.incomingorders.splice(i, 1);
					});
					break;
				}
			}
		} else if (message.type == "COOKING_ORDER") {
			for (var i=0; i<$scope.outgoingorders.length; i++) {
				if ($scope.outgoingorders.cardId == message.cardId) {
					$scope.$apply(function() {
						$scope.outgoingorders.splice(i, 1);
					});
					break;
				}
			}
		}
	});

	ws.$on('ADD_COOKING_ORDER_CARD', function(message) {
		if (message.type == "incoming") {
			$scope.$apply(function() {
				$scope.incomgingorders.push(message);
			});
		} else if (message.type == "outgoing") {
			$scope.$apply(function() {
				$scope.outgoingorders.push(message);
			});
		}
	});

	ws.$on('MARK_COOKING_ITEM_DONE', function(message) {
		if (message.type == "COOKING_ORDER") {
			var found = false;
			for (var i=0; i<$scope.outgoingorders.length; i++) {
					if ($scope.outgoingorders[i].cardId == message.cardId) {
						for (var j=0; j<$scope.outgoingorders[i].entries.length;j++) {
							if ($scope.outgoingorders[i].entries[j].entryId == message.entryId) {
								$scope.$apply(function() {
									$scope.outgoingorders[i].entries[j].isDone = true;
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

	var updateElapsed = function() {
		var currenttime = new Date();
		var buzzer = false;
		for (var i=0; i<$scope.incomingorders.length; i++) {
			var ordertime = new Date($scope.incomingorders[i].addTime);
			$scope.safeApply(function() {
				$scope.incomingorders[i].elapsedTime = ((currenttime-ordertime)/60/1000).toFixed(0);
				if ($scope.incomingorders[i].elapsedTime >= 5 && $scope.incomingorders[i].late != true) {
					$scope.incomingorders[i].late = true;
					buzzer = true;
				}
			});
		}
		if (buzzer) {
			playsound('late');
		}

	}
	var timeUpdate = setInterval(updateElapsed, 60000);
});