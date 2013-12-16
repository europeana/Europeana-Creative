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
				<!-- Box for the Service Description -->
					<div class="">
						<h3>Annotation</h3>
						<div class="left">
							<img src="img/indexing.png" height="64" />
						</div>
						<p>Annotation component is a service that implements the management and indexing of UserTags and ImageAnnotations</p>
					</div>

					<div id="table" style="width: 100%;">	
					<table>
						<thead>
						<tr>
							<td class="clsHeaderCell">Method</td>
							<td class="clsHeaderCell">Response</td>
							<td class="clsHeaderCell">Path</td>
							<td class="clsHeaderCell">Function</td>
						</tr>
						</thead>
						<tr>
							<td class="clsCellBorder cls0_0">GET</td>
							<td class="clsCellBorder">TEXT</td>
							<td class="clsCellBorder"><%=test %></td>
							<td class="clsCellBorder">Display the name of the current component</td>
						</tr>
						
						
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
					</table>
					
					</div>
					<!--  end table div -->	
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
</html>

