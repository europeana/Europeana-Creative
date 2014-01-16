<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Europeana Creative - Component Service</title>
<link href="css/css1.css" rel="stylesheet" type="text/css" />
<link href="css/main.css" rel="stylesheet" type="text/css" />
<link href="css/main_002.css" rel="stylesheet" type="text/css" />
<link href="http://annotorious.github.com/latest/themes/dark/annotorious-dark.css" rel="stylesheet" type="text/css" />
<link href="annotorious-0.6.1/css/semantic-tagging-plugin.css" rel="stylesheet" type="text/css" />
<link href="annotorious-0.6.1/css/anno-parse-plugin.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
<script type="text/javascript" src="http://www.parsecdn.com/js/parse-1.2.13.min.js"></script>
<script type="text/javascript" src="http://annotorious.github.io/latest/annotorious.dev.js"></script>
<script type="text/javascript" src="http://annotorious.github.io/demos/semantic-tagging-plugin.js"></script>
<script type="text/javascript" src="annotorious-0.6.1/anno-parse-plugin.js"></script>
</head>
<body>
<% 
String test = "Geo-mapping-service";
%>
	<div id="wrapper" class="max-width">
		<header id="banner"><!-- role="banner" ? -->
			<img class="display-none" src="img/creative-logo.png" alt=""> <img
				class="bg" src="img/creative-banner.jpg" alt="Europeana Projects">
			<h2>Geographic Mapping Service  - Demo</h2>
		</header>
		<div id="mainblock">
			<div id="content">
				<table width="100%"  margin="0" cellpadding="0" cellspacing="10">
					<tr>
						<td valign="top"><div id="map-canvas" style="width: 800px; height: 730px" valign="top"><h3>Mozart Places in Vienna</h3></div></td>
						<td valign="top" align="center" width="250px">
							<div id="mozart" style="height: 330; vertical-align: top; position:relative; top:0; overflow:hidden;">
								<h5>Details about Mozart Places</h5>
      							<img src="http://62.218.164.177:8080/geomapping/img/mozart.jpeg" width="300" class="annotatable" alt="anno">
      						</div>
							<div id="iframe-wrapper" style="height: 400; vertical-align: top; position:absolute; bottom:0; ">
								<iframe id="iframe-carousel" src="europeanaCarousel.jsp" height="400" frameborder="0" scrolling="no"></iframe>
							</div>
						</td>
					</tr>
				</table>
			<!-- end of content -->
		</div>
		<!-- end of mainblock -->

		<!--Europeana Group Block-->
		<hr />
		<footer id="footer" valign="top">
			<div class="middle f-left">
				<a href="http://www.pro.europeana.eu/web/guest/projects"
					class="f-left">Europeana Network projects</a>
			</div>
			<span class="f-right">co-funded by the European Union<img
				alt="european union flag" src="img/eu-flag.gif" height="20"></span>
		</footer>
	</div>
	<!-- end of wrapper -->
</body>

<script type="text/javascript">
	var semanticPlugitInitialized = false;
	
	function initialize() {
 		var mapOptions = {
 			mapTypeId: google.maps.MapTypeId.ROADMAP
 		};
 		var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
 		var kmlLayer = new google.maps.KmlLayer({
 			url: 'http://62.218.164.177:8080/geomapping/mozart.kml'
 		});
 		kmlLayer.setMap(map);
 		var start = new google.maps.LatLng(48.206615, 16.382010);
 		var end = new google.maps.LatLng(48.204060, 16.365360);
 		var waypts = [];
 		waypts.push({
 			location: new google.maps.LatLng(48.208149, 16.374870),
 			stopover: true
 		});
 		waypts.push({
 			location: new google.maps.LatLng(48.208412, 16.373470),
 			stopover: true
 		});
 		waypts.push({
 			location: new google.maps.LatLng(48.206329, 16.372698),
 			stopover: true
 		});
 		waypts.push({
 			location: new google.maps.LatLng(48.205597, 16.368534),
 			stopover: true
 		});
 		var directionsDisplay = new google.maps.DirectionsRenderer({suppressMarkers: true});
 		directionsDisplay.setMap(map);
 		var directionsService = new google.maps.DirectionsService();
 		var request = {
 			origin: start,
 			destination: end,
 			waypoints: waypts,
 			optimizeWaypoints: false,
 			provideRouteAlternatives: false,
 			travelMode: google.maps.TravelMode.WALKING
 		};
 		directionsService.route(request, function(response, status) {
 			if(status == google.maps.DirectionsStatus.OK) {
 				directionsDisplay.setDirections(response);
 			}
 		});
 		google.maps.event.addListener(kmlLayer, 'click', function (kmlEvent) {
 			setDetails(kmlEvent.featureData.id, kmlEvent.featureData.name, kmlEvent.featureData.description);
 			window.setTimeout( function(){setAnnotatableClasses(); anno.reset();}, 2000);
 		});
	}
	function setAnnotatableClasses() {
		var my_img = document.getElementsByTagName("img");
		for (i=0;i<my_img.length;i++){
			var str=my_img[i].alt;
			if(str == "anno") {
				my_img[i].setAttribute("class", "annotatable");
			}
		}
	  	anno.addPlugin('Parse', { app_id: '5eTcrECtbp3iy0qt6Qpin7LImq8UtoGmfQ0LMXc3', js_key: 'YlDGi93ebuyRBpGAmnTafD3pE5VjDo4XzeV3Csum', debug: 'true' });
	  	if(!semanticPlugitInitialized){
	  		anno.addPlugin('SemanticTagging', { endpoint_url: 'http://samos.mminf.univie.ac.at:8080/wikipediaminer' });
	  		semanticPlugitInitialized = true;
	  	}
	  	
	}
	function setDetails(id, name, text) {
		details = document.getElementById("mozart");
		if(id == "mozarthaus") {
			details.innerHTML = '<a href="http://de.wikipedia.org/w/index.php?title=Mozarthaus_Vienna" target="new"><h5>Mozarthaus Vienna</h5></a>';
			details.innerHTML += '<img src="http://62.218.164.177:8080/geomapping/img/mozarthaus.jpg" valign="top" width="300" class="annotatable" alt="anno">';
			ifr = document.getElementById("iframe-carousel");
			ifrDoc = ifr.contentDocument;
			searchForm = ifrDoc.getElementById("search_form");
			searchTerms = ifrDoc.getElementById("searchTerms");
			searchTerms.value = "Mozart Haydn Beethoven";
			searchForm.submit();
			
		} 
		else if(id == "cementary") {
			details.innerHTML = '<a href="http://de.wikipedia.org/wiki/Sankt_Marxer_Friedhof#Das_Mozartgrab" target="new"><h5>Mozart\'s Tomb</h5></a>';
			details.innerHTML += '<img src="http://62.218.164.177:8080/geomapping/img/cemetery_main.jpg" valign="top" width="300" class="annotatable" alt="anno">';
			ifr = document.getElementById("iframe-carousel");
			ifrDoc = ifr.contentDocument;
			searchForm = ifrDoc.getElementById("search_form");
			searchTerms = ifrDoc.getElementById("searchTerms");
			searchTerms.value = "Mozart-Grab";
			searchForm.submit();
			
		}
		else if(id == "burggarten") {
			details.innerHTML = '<a href="http://de.wikipedia.org/wiki/Burggarten_%28Wien%29" target="new"><h5>Burggarten (Palmenhaus)</h5></a>';
			details.innerHTML += '<img src="http://62.218.164.177:8080/geomapping/img/burggarten_main.jpg" valign="top" width="300" class="annotatable" alt="anno">';
			ifr = document.getElementById("iframe-carousel");
			ifrDoc = ifr.contentDocument;
			searchForm = ifrDoc.getElementById("search_form");
			searchTerms = ifrDoc.getElementById("searchTerms");
			searchTerms.value = "Wien 1, Mozart-Denkmal";
			searchForm.submit();
		}
		else if(id == "stadtpark") {
			details.innerHTML = '<a href="http://de.wikipedia.org/wiki/Kursalon_H%C3%BCbner" target="new"><h5>Kursalon Hübner</h5></a>';
			details.innerHTML += '<img src="http://62.218.164.177:8080/geomapping/img/stadtpark_main.jpg" valign="top" width="300" class="annotatable" alt="anno">';
			ifr = document.getElementById("iframe-carousel");
			ifrDoc = ifr.contentDocument;
			searchForm = ifrDoc.getElementById("search_form");
			searchTerms = ifrDoc.getElementById("searchTerms");
			searchTerms.value = "Wien 1, Stadtpark";
			searchForm.submit();
		}
		else if(id == "stephens_cathedral") {
			details.innerHTML = '<a href="http://de.wikipedia.org/wiki/Constanze_Mozart" target="new"><h5>Constanze Mozart</h5></a>';
			details.innerHTML += '<img src="http://62.218.164.177:8080/geomapping/img/constanze.jpg" valign="top" width="300" class="annotatable" alt="anno">';
			ifr = document.getElementById("iframe-carousel");
			ifrDoc = ifr.contentDocument;
			searchForm = ifrDoc.getElementById("search_form");
			searchTerms = ifrDoc.getElementById("searchTerms");
			searchTerms.value = "Stephansdom";
			searchForm.submit();
		}
		else if(id == "rauhensteingasse") {
			details.innerHTML = '<a href="http://de.wikipedia.org/wiki/Steffl_%28Kaufhaus%29" target="new"><h5>Steffl Store (Kaufhaus Steffl)</h5></a>';
			details.innerHTML += '<img src="http://62.218.164.177:8080/geomapping/img/steffl.jpg" valign="top" width="300" class="annotatable" alt="anno">';
			ifr = document.getElementById("iframe-carousel");
			ifrDoc = ifr.contentDocument;
			searchForm = ifrDoc.getElementById("search_form");
			searchTerms = ifrDoc.getElementById("searchTerms");
			searchTerms.value = "Rauhensteingasse Mozart";
			searchForm.submit();
		}
		else if(id == "theatermuseum") {
			details.innerHTML = '<a href="http://de.wikipedia.org/wiki/Palais_Lobkowitz_%28Wien%29#Geschichte" target="new"><h5>Lobkowitz Palace (1760)</h5></a>';
			details.innerHTML += '<img src="http://62.218.164.177:8080/geomapping/img/palais_lobkowitz_1760.jpg" valign="top" width="300" class="annotatable" alt="anno">';
			ifr = document.getElementById("iframe-carousel");
			ifrDoc = ifr.contentDocument;
			searchForm = ifrDoc.getElementById("search_form");
			searchTerms = ifrDoc.getElementById("searchTerms");
			searchTerms.value = "Österreichisches Theatermuseum Mozart";
			searchForm.submit();
		}
	}

	google.maps.event.addDomListener(window, 'load', initialize);
	anno.addPlugin('Parse', { app_id: '5eTcrECtbp3iy0qt6Qpin7LImq8UtoGmfQ0LMXc3', js_key: 'YlDGi93ebuyRBpGAmnTafD3pE5VjDo4XzeV3Csum', debug: 'true' });
  	
	anno.addPlugin('SemanticTagging', { endpoint_url: 'http://samos.mminf.univie.ac.at:8080/wikipediaminer' });
	semanticPlugitInitialized = true;
	
</script>

</html>

