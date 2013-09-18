/*function changeImage(imageURL) {
	  document.getElementById('advSearch').style.display = '';
    document.getElementById('queryImage').src=imageURL;
}*/

function changeImage(imageURL, objectID) {
	//alert(imageURL + " - " + objectID);
	if (document.getElementById('objId').value==objectID || objectID == "" || imageURL == "null" || objectID == "null") {
		document.getElementById('advSearch').style.display = 'none';
		//document.getElementById('comboInfo').style.display = 'none';
		  document.getElementById('queryImage').src='queryImage';
		  document.getElementById('objId').value='';
		  document.getElementById('objId').name="disabled";
	}
	else {
	  document.getElementById('advSearch').style.display = '';
	  //document.getElementById('comboInfo').style.display = '';
	  document.getElementById('queryImage').src=imageURL;
	  document.getElementById('objId').value=objectID;
	  document.getElementById('objId').name="id";
	  
	  //document.getElementById('imageQueryCheckbox').checked='checked';
	  document.getElementById('objId').name="id";
	  document.getElementById('queryImage').style.display = '';
	  
	  if (document.getElementById('imageToUpload') != null) {
		  document.getElementById('imageToUpload').value="";
	  }
	}
	if (imageURL != "null" && imageURL != "" && imageURL == objectID) {
		document.getElementById('objId').name="url";
	}
}

function isIndexPage() {
	var sPath = window.location.pathname;
	if (sPath.indexOf('index') < 0 ) return false;
	return true;
}

function getFeatures(features, feature) {
	if (features.indexOf(feature) < 0 ) {
		if (features.length > 0)
		features = feature + "_" + features;
		else 
			features = feature;
	} else {
		features = features.replace(feature+"_", "");
		features = features.replace("_"+ feature, "");
		features = features.replace(feature, "");
	}
	return features;
}

function setFeatures() {
	underscore = "";
	features = "";
//	if (document.getElementById('bof').checked) {
//		features+=underscore + document.getElementById('bof').value;
//		underscore = "_";	
//	}
//	if (document.getElementById('mp7SC').checked) {
//		features+=underscore + document.getElementById('mp7SC').value;
//		underscore = "_";	
//	}
//	if (document.getElementById('mp7CS').checked) {
//		features+=underscore + document.getElementById('mp7CS').value;
//		underscore = "_";	
//	}
//	if (document.getElementById('mp7CL').checked) {
//		features+=underscore + document.getElementById('mp7CL').value;
//		underscore = "_";	
//	}
//	if (document.getElementById('mp7EH').checked) {
//		features+=underscore + document.getElementById('mp7EH').value;
//		underscore = "_";	
//	}
//	if (document.getElementById('mp7HT').checked) {
//		features+=underscore + document.getElementById('mp7HT').value;
//		underscore = "_";	
//	}
	document.getElementById('features').value=features;
	
}

function imageQuery() {
	//alert(imageURL + " - " + objectID);
	if (!document.getElementById('imageQueryCheckbox').checked) {
		  document.getElementById('objId').name="disabled";
		  document.getElementById('queryImage').style.display = 'none';
	}
	else {
	  document.getElementById('objId').name="id";
	  document.getElementById('queryImage').style.display = '';
	}
}

function gpsQuery() {
	if (!document.getElementById('gpsCheckbox').checked) {
		  document.getElementById('latField').name="disabled";
		  document.getElementById('lngField').name="disabled";
		  document.getElementById('lat').name="disabled";
		  document.getElementById('lng').name="disabled";
	}
	else {
		document.getElementById('latField').name="field1";
		document.getElementById('lngField').name="field2";
		  document.getElementById('lat').name="value1";
		  document.getElementById('lng').name="value2";
	}
}

function repeatQuery() {	
	if ((document.getElementById('keywords').value != "") || (document.getElementById('objId').value != "")) {
		document.form1.submit();
	}
	else {
		location.href='index';
	}
}

//*****advanced options*****
function onLoad(aggregateFunctionForUI) {
    setAggr_func(aggregateFunctionForUI);
}

function showAdvOpt() {
	  if (document.getElementById("advOpt").style.display == 'none') 
		  document.getElementById("advOpt").style.display = '';
	  else 
		  document.getElementById("advOpt").style.display = 'none';
}

function showMoreSearchOptions() {
	  if (document.getElementById("moreSearchOptions").style.display == 'none') {
		  document.getElementById("searchbar").method="POST";
		  document.getElementById("searchbar").enctype="multipart/form-data";
		  document.getElementById("moreSearchOptions").style.display = '';
	  	  document.getElementById("expandSearch").src="images/downarrow.png";
	  }
	  else {
		  document.getElementById("searchbar").method="GET";
		  document.getElementById("searchbar").enctype=""
		  document.getElementById("moreSearchOptions").style.display = 'none';
		  document.getElementById('imageToUpload').value="";
		  document.getElementById("expandSearch").src="images/downarrow.png";
	  }
}

function selectTab(tabID) {
  	searchMode = new Array("full", "img", "video");
  	for (i = 0; i < 3; i++) {
  		if (searchMode[i] == tabID) {
  		
  			document.getElementById(searchMode[i]+"txt").style.display = '';

  			document.getElementById(searchMode[i]).style.display = 'none';
  		}
  		else {
  			document.getElementById(searchMode[i]+"txt").style.display = 'none';
  			document.getElementById(searchMode[i]).style.display = '';
  		}
  	}
  	if ( tabID != "img") {
  		if (document.getElementById("lat") != null) {
  	    document.getElementById("lat").value = "";
  	    document.getElementById("lng").value = "";
  		}
  	}

}

function getWindowHeight() {
	var windowHeight = 0;
	if (typeof(window.innerHeight) == 'number') {
		windowHeight = window.innerHeight;
	}
	else {
		if (document.documentElement && document.documentElement.clientHeight) {
			windowHeight = document.documentElement.clientHeight;
		}
		else {
			if (document.body && document.body.clientHeight) {
				windowHeight = document.body.clientHeight;
			}
		}
	}
	return windowHeight;
}

function setAggr_func(aggregateFunctionForUI) {
    if (aggregateFunctionForUI == "sum") {
        document.getElementById('aggr_func_sum').checked = "checked";
    }
    else if (aggregateFunctionForUI == "wsum") {
        document.getElementById('aggr_func_wsum').checked = "checked";
    }
    else if (aggregateFunctionForUI == "wAND") {
        document.getElementById('aggr_func_wAND').checked = "checked";
    }
    else if (aggregateFunctionForUI == "fAND") {
        document.getElementById('aggr_func_fAND').checked = "checked";
    }
    else if (aggregateFunctionForUI == "fOR") {
        document.getElementById('aggr_func_fOR').checked = "checked";
    }
    
    if (!document.getElementById('aggr_func_fAND').checked) {
    	document.getElementById('imageWeight').disabled="";
    	document.getElementById('textWeight').disabled="";
    	document.getElementById('gpsWeight').disabled="";
    }
    else {
    	document.getElementById('imageWeight').disabled="disabled";
    	document.getElementById('textWeight').disabled="disabled";
    	document.getElementById('gpsWeight').disabled="disabled";
    }
}

function showSimCacheOpt() {
	  if (document.getElementById("simCacheOpt").style.display == 'none') 
		  document.getElementById("simCacheOpt").style.display = '';
	  else 
		  document.getElementById("simCacheOpt").style.display = 'none';
}

function selectImagesByLicense() {
	licenses = new Array();
	if (!document.getElementById('commerciallyLicense').checked && !document.getElementById('modifyLicense').checked) {
	//alert("00");
	}
	else if (document.getElementById('commerciallyLicense').checked && !document.getElementById('modifyLicense').checked) {
		licenses[0]="4";
		licenses[1]="5";
		licenses[2]="6";
		//alert("01");
	}
	else if (!document.getElementById('commerciallyLicense').checked && document.getElementById('modifyLicense').checked) {
		licenses[0]="1";
		licenses[1]="2";
		licenses[2]="4";
		licenses[3]="5";
		//alert("10");
	}
	else if (document.getElementById('commerciallyLicense').checked && document.getElementById('modifyLicense').checked) {
		licenses[0]="4";
		licenses[1]="5";
		//alert("11");
	}
	
	index = 0;
	while(document.getElementById(divID='result_'+index) != null) {
		for (i =0; i <= 6; i++) {
			elementID = index +'_'+i;
			if (document.getElementById(elementID) != null) {
				document.getElementById(elementID).border='0';
				break;
			}
		}
		for (i =0; i < licenses.length; i++) {
			elementID = index +'_'+licenses[i];
			if (document.getElementById(elementID) != null) {
				document.getElementById(elementID).border='5';
				break;
			}
		}
		index++;
	}
}

function drm() {
	licenses = new Array();
	if (!document.getElementById('commerciallyLicense').checked && !document.getElementById('modifyLicense').checked) {
	//alert("00");
	}
	else if (document.getElementById('commerciallyLicense').checked && !document.getElementById('modifyLicense').checked) {
		licenses[0]="<photo license='4' />";
		licenses[1]="<photo license='5' />";
		licenses[2]="<photo license='6' />";
		//alert("01");
	}
	else if (!document.getElementById('commerciallyLicense').checked && document.getElementById('modifyLicense').checked) {
		licenses[0]="<photo license='1' />";
		licenses[1]="<photo license='2' />";
		licenses[2]="<photo license='4' />";
		licenses[3]="<photo license='5' />";
		//alert("10");
	}
	else if (document.getElementById('commerciallyLicense').checked && document.getElementById('modifyLicense').checked) {
		licenses[0]="<photo license='4' />";
		licenses[1]="<photo license='5' />";
		//alert("11");
	}
	
	licenseQuery = "";
	for (i =0; i < licenses.length; i++) {
		licenseQuery += licenses[i] + " ";
	}
	//alert(licenseQuery);
	
	if (document.getElementById('keywords') != null ) {
		//alert("value0 != null");
		value2 = document.getElementById('keywords').value;
		//alert(value2);
		if (value2 != "") {
            if (licenses.length > 0) {
                value2 = "<.and>" + value2 + "<.or>" + licenseQuery + "</.or></.and>";
            }
			//alert(value2);
		}
		alert(value2);
		document.getElementById('keywords').value = value2;
	}
}

//*****google maps*******
function load() {
    load(null);
  }

function load(kmlFileURL) {
    if (GBrowserIsCompatible()) {
      var map = new GMap2(document.getElementById("map"));
      map.addControl(new GSmallMapControl());
      map.addControl(new GMapTypeControl());
      map.enableScrollWheelZoom();
      var defaultLat = 43.71432;
      var defaultLng = 10.39722;
      if (document.getElementById("lat") != null && document.getElementById("lat").value != "") {
    	  defaultLat = document.getElementById("lat").value;
      }
      if (document.getElementById("lng") != null && document.getElementById("lng").value != "") {
    	  defaultLng = document.getElementById("lng").value;
      }
      var center = new GLatLng(defaultLat, defaultLng);
      map.setCenter(center, 5);
      geocoder = new GClientGeocoder();
      var marker = new GMarker(center, {draggable: true});
      map.addOverlay(marker);
     
      //document.getElementById("lat").value = center.lat().toFixed(5);
      //document.getElementById("lng").value = center.lng().toFixed(5);

	  GEvent.addListener(marker, "dragend", function() {
     var point = marker.getPoint();
	      map.panTo(point);
     document.getElementById("lat").value = point.lat().toFixed(5);
     document.getElementById("lng").value = point.lng().toFixed(5);
     document.getElementById('gpsCheckbox').checked='checked';
 	gpsQuery();
      });
	  
	  GEvent.addListener(map, "click", function(overlay,point) {
		  marker.setPoint(point);
	     var point = marker.getPoint();
		      map.panTo(point);
	     document.getElementById("lat").value = point.lat().toFixed(5);
	     document.getElementById("lng").value = point.lng().toFixed(5);
	     document.getElementById('gpsCheckbox').checked='checked';
	    	gpsQuery();

	      });


	  /* GEvent.addListener(map, "moveend", function() {
		  map.clearOverlays();
		  var center = map.getCenter();
		  var marker = new GMarker(center, {draggable: true});
		  map.addOverlay(marker);
		  document.getElementById("lat").value = center.lat().toFixed(5);
	   document.getElementById("lng").value = center.lng().toFixed(5);


	GEvent.addListener(marker, "dragend", function() {
    var point =marker.getPoint();
	     map.panTo(point);
    document.getElementById("lat").value = point.lat().toFixed(5);
	     document.getElementById("lng").value = point.lng().toFixed(5);

      });

      });*/
	  
      if (kmlFileURL != null && kmlFileURL != "null") {
    	  //alert(kmlFileURL);
    	map.addOverlay(new GGeoXml(kmlFileURL));
      }

    }
  }

function showAddress(address) {
	document.getElementById('gpsCheckbox').checked='checked';
	gpsQuery();
	   var map = new GMap2(document.getElementById("map"));
     map.addControl(new GSmallMapControl());
     map.addControl(new GMapTypeControl());
     map.enableScrollWheelZoom();
     if (geocoder) {
      geocoder.getLatLng(
        address,
        function(point) {
          if (!point) {
            alert(address + " not found");
          } else {
		  document.getElementById("lat").value = point.lat().toFixed(5);
	   document.getElementById("lng").value = point.lng().toFixed(5);
	   document.getElementById('gpsCheckbox').checked='checked';
   	gpsQuery();
		 map.clearOverlays()
			map.setCenter(point, 13);
 var marker = new GMarker(point, {draggable: true});  
		 map.addOverlay(marker);

		GEvent.addListener(marker, "dragend", function() {
    var pt = marker.getPoint();
	     map.panTo(pt);
    document.getElementById("lat").value = pt.lat().toFixed(5);
	     document.getElementById("lng").value = pt.lng().toFixed(5);
	     document.getElementById('gpsCheckbox').checked='checked';
	    	gpsQuery();
      });
		
		GEvent.addListener(map, "click",function(overlay,point) {
			  marker.setPoint(point);
		     var point = marker.getPoint();
			      map.panTo(point);
		     document.getElementById("lat").value = point.lat().toFixed(5);
		     document.getElementById("lng").value = point.lng().toFixed(5);
		     document.getElementById('gpsCheckbox').checked='checked';
		    	gpsQuery();

		      });


	/* GEvent.addListener(map, "moveend", function() {
		  map.clearOverlays();
  var center = map.getCenter();
		  var marker = new GMarker(center, {draggable: true});
		  map.addOverlay(marker);
		  document.getElementById("lat").value = center.lat().toFixed(5);
	   document.getElementById("lng").value = center.lng().toFixed(5);

	 GEvent.addListener(marker, "dragend", function() {
   var pt = marker.getPoint();
	    map.panTo(pt);
  document.getElementById("lat").value = pt.lat().toFixed(5);
	   document.getElementById("lng").value = pt.lng().toFixed(5);
      });

      });*/

          }
        }
      );
    }
  }

function showMaps() {
	  if (document.getElementById("maps").style.display == 'none') 
	  document.getElementById("maps").style.display = '';
  else 
	  document.getElementById("maps").style.display = 'none';
}
//****end google maps****
