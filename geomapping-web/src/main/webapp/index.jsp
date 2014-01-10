<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Europeana Creative - Component Service</title>
<link href="css/css.css" rel="stylesheet" type="text/css" />
<link href="css/css1.css" rel="stylesheet" type="text/css" />
<link href="css/main.css" rel="stylesheet" type="text/css" />
<link href="css/main_002.css" rel="stylesheet" type="text/css" />
<link href="css/main_003.css" rel="stylesheet" type="text/css" />
<link href="css/main_004.css" rel="stylesheet" type="text/css" />
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
						<td><div id="map-canvas" style="width: 800px; height: 730px"><h3>Mozart Places in Vienna</h3></div></td>
						<td valign="top" align="center" width="250px">
							<div id="mozart"><h5>Details about Mozart Places</h5>
      							<img src="img/mozart.jpeg" class="annotatable" alt="anno">
      						</div>
							<div id="iframe-wrapper">
								<iframe id="iframe-carousel" src="europeanaCarousel.jsp" height="400" frameborder="0" scrolling="no"></iframe>
							</div>
						</td>
					</tr>
				</table>
			<!-- end of content -->
		</div>
		<!-- end of mainblock -->

		<hr />
		<!--Europeana Group Block-->
		<footer id="footer">
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
	function initialize() {
 		var mapOptions = {
 			mapTypeId: google.maps.MapTypeId.ROADMAP
 		};
 		var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
 		var kmlLayer = new google.maps.KmlLayer({
 			url: 'https://dl.dropboxusercontent.com/u/45528256/mozart.kml'
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
 			window.setTimeout(function(){setAnnotatableClasses(); setDetails(kmlEvent.featureData.id, kmlEvent.featureData.name, kmlEvent.featureData.description); anno.reset();
 			;},1000);
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
	}
	function setDetails(id, name, text) {
		details = document.getElementById("mozart");
		details.innerHTML = "<h3>" + name + "</h3>";
		if(id == "mozarthaus") {
			details.innerHTML += '<br><br><img src="img/Boesendorfer_Saal_Wien,_Mozart_House_Vienna.jpg" width="300" class="annotatable" alt="anno">';
			ifr = document.getElementById("iframe-carousel");
			ifrDoc = ifr.contentDocument;
			searchForm = ifrDoc.getElementById("search_form");
			searchTerms = ifrDoc.getElementById("searchTerms");
			searchTerms.value = "Mozart Haydn Beethoven";
			searchForm.submit();
		} 
		else if(id == "cementary") {
			details.innerHTML += '<br><br><img src="http://bilddatenbank.khm.at/images/500/FS_PA84282alt.jpg" width="300" class="annotatable" alt="anno">';
			ifr = document.getElementById("iframe-carousel");
			ifrDoc = ifr.contentDocument;
			searchForm = ifrDoc.getElementById("search_form");
			searchTerms = ifrDoc.getElementById("searchTerms");
			searchTerms.value = "Mozart-Grab";
			searchForm.submit();
		}
		else if(id == "burggarten") {
			details.innerHTML += '<br><br><img src="http://commons.wikimedia.org/wiki/File:WienBurggartenMozart.jpg" width="300" class="annotatable" alt="anno">';
			ifr = document.getElementById("iframe-carousel");
			ifrDoc = ifr.contentDocument;
			searchForm = ifrDoc.getElementById("search_form");
			searchTerms = ifrDoc.getElementById("searchTerms");
			searchTerms.value = "Wien 1, Mozart-Denkmal";
			searchForm.submit();
		}
		else if(id == "stadtpark") {
			details.innerHTML += '<br><br><img src="http://commons.wikimedia.org/wiki/File:Kursalon_Vienna_June_2006_462.jpg" width="300" class="annotatable" alt="anno">';
			ifr = document.getElementById("iframe-carousel");
			ifrDoc = ifr.contentDocument;
			searchForm = ifrDoc.getElementById("search_form");
			searchTerms = ifrDoc.getElementById("searchTerms");
			searchTerms.value = "Wien 1, Stadtpark";
			searchForm.submit();
		}
		else if(id == "stephens_cathedral") {
			details.innerHTML += '<br><br><img src="http://commons.wikimedia.org/wiki/File:Costanze_Mozart_by_Lange_1782.jpg" width="300" class="annotatable" alt="anno">';
			ifr = document.getElementById("iframe-carousel");
			ifrDoc = ifr.contentDocument;
			searchForm = ifrDoc.getElementById("search_form");
			searchTerms = ifrDoc.getElementById("searchTerms");
			searchTerms.value = "Stephansdom";
			searchForm.submit();
		}
		else if(id == "rauhensteingasse") {
			details.innerHTML += '<br><br><img src="http://commons.wikimedia.org/wiki/File:Rauhensteingasse_z03.JPG" width="300" class="annotatable" alt="anno">';
			ifr = document.getElementById("iframe-carousel");
			ifrDoc = ifr.contentDocument;
			searchForm = ifrDoc.getElementById("search_form");
			searchTerms = ifrDoc.getElementById("searchTerms");
			searchTerms.value = "Rauhensteingasse Mozart";
			searchForm.submit();
		}
		else if(id == "theatermuseum") {
			details.innerHTML += '<br><br><img src="http://commons.wikimedia.org/wiki/File:Palais_Lobkowitz_Vienna_Oct._2006_006.jpg" width="300" class="annotatable" alt="anno">';
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
</script>

</html>

