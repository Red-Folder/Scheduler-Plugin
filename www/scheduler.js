/*
 * Copyright 2014 Red Folder Consultancy Ltd
 *   
 * Licensed under the Apache License, Version 2.0 (the "License");   
 * you may not use this file except in compliance with the License.   
 * You may obtain a copy of the License at       
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0   
 *
 * Unless required by applicable law or agreed to in writing, software   
 * distributed under the License is distributed on an "AS IS" BASIS,   
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   
 * See the License for the specific language governing permissions and   
 * limitations under the License.
 */



	function SchedulerFactory() { };

	SchedulerFactory.prototype.create = function () {
		var exec = cordova.require("cordova/exec");
		
			
		var Scheduler = function () {
		   var _self = this;
		   
		   this.addAlarm = function(what, when, showOption, extras, successCallback, failureCallback) {
			  return exec(	successCallback,
							failureCallback,      
							'SchedulerPlugin', 
							'addAlarm',      
							[what, _self.convertToUTC(when), showOption, extras]);
		   };
		   
		   this.updateAlarm = function(id, what, when, showOption, extras, successCallback, failureCallback) {
			  return exec(	successCallback,
							failureCallback,      
							'SchedulerPlugin', 
							'updateAlarm',      
							[id, what, _self.convertToUTC(when), showOption, extras]);
		   };
		   
		   this.cancelAlarm = function(id, successCallback, failureCallback) {
			  return exec(	successCallback,
							failureCallback,      
							'SchedulerPlugin', 
							'cancelAlarm',      
							[id]);
		   };
		   
		   this.getAlarms = function(successCallback, failureCallback) {
			  return exec(	function(data) {
			  					var alarms = new Array();
			                    for (var x in data) {
			                       alarms[x] = _self.convertToJSON(data[x]);
			                    };
			                    
			  					successCallback(alarms);
			  				},
							failureCallback,      
							'SchedulerPlugin', 
							'getAlarms',      
							[]);
		   };

		   this.getAlarm = function(id, successCallback, failureCallback) {
			  return exec(	function(data) {	
			  					successCallback(_self.convertToJSON(data));
			  				},
							failureCallback,      
							'SchedulerPlugin', 
							'getAlarm',      
							[id]);
		   };
		   
		   this.getDefaultClass = function(successCallback, failureCallback) {
			  return exec(	successCallback,
							failureCallback,      
							'SchedulerPlugin', 
							'getDefaultClass',      
							[]);
		   };
		   
		   this.getActivityExtras = function(successCallback, failureCallback) {
			  return exec(	successCallback,
							failureCallback,      
							'SchedulerPlugin', 
							'getActivityExtras',      
							[]);
		   };
		   
		
		   this.convertToUTC = function(originalDate) {
			  return new Date(originalDate.getTime() - _self.getUTCDelta());
		   }; 

		   this.convertFromUTC = function(originalDate) {
			  return new Date(originalDate.getTime() + _self.getUTCDelta());
		   }; 
		
		   this.getUTCDelta = function() {
		      var now = new Date();
			  return (now.getTimezoneOffset()*60*1000);
		   };
		
		   this.convertToJSON = function(fromPlugin) {
		      var alarm = {};
		      
		      alarm.id = fromPlugin.id;
		      alarm.classname = fromPlugin.classname;
		      alarm.when = _self.convertFromUTC(new Date(fromPlugin.when));
		      alarm.showoption = fromPlugin.showoption;
		      alarm.extras = fromPlugin.extras;
		      
		      return alarm;
		   };
		};
		
		var scheduler = new Scheduler();
		return scheduler;
		
	};
   
   	module.exports = new SchedulerFactory().create();



