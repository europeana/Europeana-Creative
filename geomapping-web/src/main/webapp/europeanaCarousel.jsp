<%@page import="eu.europeana.api.client.result.EuropeanaApi2Results"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="searchBean" class="eu.europeana.creative.geomapping.EuropeanaSearchBean" scope="session"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Europeana Carousel</title>
<link href="css/css.css" rel="stylesheet" type="text/css" />
<link href="css/css1.css" rel="stylesheet" type="text/css" />
<link href="css/main.css" rel="stylesheet" type="text/css" />
<link href="css/main_002.css" rel="stylesheet" type="text/css" />
<link href="css/main_003.css" rel="stylesheet" type="text/css" />
<link href="css/main_004.css" rel="stylesheet" type="text/css" />
<link href="http://annotorious.github.com/latest/themes/dark/annotorious-dark.css" rel="stylesheet" type="text/css" />
<link href="annotorious-0.6.1/css/semantic-tagging-plugin.css" rel="stylesheet" type="text/css" />
<link href="annotorious-0.6.1/css/anno-parse-plugin.css" rel="stylesheet" type="text/css" />
<link href="bootstrap/dist/css/bootstrap.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
<script type="text/javascript" src="http://www.parsecdn.com/js/parse-1.2.13.min.js"></script>
<script type="text/javascript" src="bootstrap/dist/js/bootstrap.js"></script>
<script type="text/javascript" src="http://annotorious.github.io/latest/annotorious.dev.js"></script>
<script type="text/javascript" src="http://annotorious.github.io/demos/semantic-tagging-plugin.js"></script>
<script type="text/javascript" src="annotorious-0.6.1/anno-parse-plugin.js"></script>
</head>
<body>

<% 
String searchTerms = request.getParameter("searchTerms");
String country = request.getParameter("country");
EuropeanaApi2Results results = null;

if(country != null && !"".equals(country.trim())){
	out.println("Country set to: " + country);
	results = searchBean.getEuropeanaResults(searchTerms, country);
}
%>
	
	<form action="./europeanaCarousel.jsp" method="get">
		Search: <input id="searchTerms" type="text" name="searchTerms" value="Wolfgang Amadeus Mozart">
		<input type="hidden" name="lang" value="de">
		<input type="hidden" name="country" value="austria">
		<input type="hidden" name="city" value="Vienna">
		
		<input type="submit" value="Search">
	</form>
	<div id="mozart-carousel" class="carousel slide" data-ride="carousel"><h3>Europeana Results</h3>
		<!-- Indicators -->
  		<ol class="carousel-indicators">
    		<li data-target="#mozart-carousel" data-slide-to="0" class="active"></li>
    		<li data-target="#mozart-carousel" data-slide-to="1"></li>
    		<li data-target="#mozart-carousel" data-slide-to="2"></li>  		
  		</ol>

		<% 
		if (results != null){
			out.println("items found" + results.getItemsCount());
			if(results.getItemsCount() > 0)
				out.println("firstItem: " + results.getAllItems().get(0).getTitle());
		}
		%>

		<!-- Wrapper for slides -->
		<div class="carousel-inner">
			<div class="item active">
				<img src="img/mozart.jpeg" height="300">
      			<div class="carousel-caption">
        			Wolfgang Amadeus Mozart in <%=country %>
      			</div>
    		</div>
    		<div class="item">
    			<img src="img/mozart2.jpg" height="300">
    			<div class="carousel-caption">
    				Mozart in <%=country %>
    			</div>
    		</div>
    		<div class="item">
    			<img src="img/mozart3.jpg" height="300">
    			<div class="carousel-caption">
    				Young Mozart in <%=country %>
    			</div>
    		</div>
  		</div>

  		<!-- Controls -->
  		<a class="left carousel-control" href="#mozart-carousel" data-slide="prev">
    		<span class="glyphicon glyphicon-chevron-left"></span>
  		</a>
  		<a class="right carousel-control" href="#mozart-carousel" data-slide="next">
    		<span class="glyphicon glyphicon-chevron-right"></span>
  		</a>
	</div>
</body>
</html>