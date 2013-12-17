<!DOCTYPE html>
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
<link rel="stylesheet" href="http://annotorious.github.com/latest/themes/dark/annotorious-dark.css" type="text/css" />
<link href="annotorious-0.6.1/css/semantic-tagging-plugin.css" rel="stylesheet" type="text/css" />
<link href="annotorious-0.6.1/css/anno-parse-plugin.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
<script src="http://www.parsecdn.com/js/parse-1.2.13.min.js"></script>
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
				<table><tr>
				<td><div id="map-canvas" style="width: 800px; height: 700px"><h3>Mozart Places in Vienna</h3></div></td>
				<td valign="top"><div id="mozart"><h3>Details about Mozart Places</h3>
					<img src="img/mozart.jpeg" class="annotatable" alt="anno">
				</div></td>
				<tr></table>
			</div>
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
			details.innerHTML += '<br><br><img src="http://www.viennaconcerts.com/img/concerts/mozarthaushall.jpg" width="300" class="annotatable" alt="anno">';
		} 
		else if(id == "cementary") {
			details.innerHTML += '<br><br><img src="http://bilddatenbank.khm.at/images/500/FS_PA84282alt.jpg" width="300" class="annotatable" alt="anno">';
		}
		else if(id == "burggarten") {
			details.innerHTML += '<br><br><img src="http://media-cdn.tripadvisor.com/media/photo-s/03/d8/54/dc/mozart-statue.jpg" width="300" class="annotatable" alt="anno">';
		}
		else if(id == "stadtpark") {
			details.innerHTML += '<br><br><img src="http://viena.viajandopor.com/archivos/imagenes-nuevas/mozart-stadtpark.jpg" width="300" class="annotatable" alt="anno">';
		}
		else if(id == "stephens_cathedral") {
			details.innerHTML += '<br><br><img src="http://www.huntsearch.gla.ac.uk/artimages/43746.jpg" width="300" class="annotatable" alt="anno">';
		}
		else if(id == "rauhensteingasse") {
			details.innerHTML += '<br><br><img src="http://upload.wikimedia.org/wikipedia/commons/4/4e/Rauhensteingasse_z03.JPG" width="300" class="annotatable" alt="anno">';
		}
		else if(id == "theatermuseum") {
			details.innerHTML += '<br><br><img src="http://bilddatenbank.khm.at/images/500/FS_PA108357.jpg" width="300" class="annotatable" alt="anno">';
		}
	}
	</script>
	<script type="text/javascript" src="http://annotorious.github.io/latest/annotorious.dev.js"></script>
	<script type="text/javascript" src="http://annotorious.github.io/demos/semantic-tagging-plugin.js"></script>
	<script type="text/javascript" src="annotorious-0.6.1/anno-parse-plugin.js"></script>
	<script type="text/javascript">
		google.maps.event.addDomListener(window, 'load', initialize);	
  		anno.addPlugin('Parse', { app_id: '5eTcrECtbp3iy0qt6Qpin7LImq8UtoGmfQ0LMXc3', js_key: 'YlDGi93ebuyRBpGAmnTafD3pE5VjDo4XzeV3Csum', debug: 'true' });
		anno.addPlugin('SemanticTagging', { endpoint_url: 'http://samos.mminf.univie.ac.at:8080/wikipediaminer' });
	</script>

</html>

