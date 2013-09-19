<jsp:useBean id="advOptions" scope="session"
    class="it.cnr.isti.cophir.ui.bean.Parameters" />
<jsp:useBean id="randomImages" scope="session"
	class="it.cnr.isti.cophir.ui.bean.RandomImages" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">


<%@page import="it.cnr.isti.cophir.ui.tools.UITools"%>
<%@page import="it.cnr.isti.cophir.ui.index.IndexSupport"%>
<%@page import="it.cnr.isti.cophir.ui.bean.MobileCostants"%><html>
<head>
<meta name="keywords" content="image similarity search" />
<link rel="icon" href="favicon.ico" />
<link rel="shortcut icon" href="favicon.ico" />
<link rel="stylesheet" type="text/css" href="uiStyle.css">

<script type="text/javascript" src="ui.js"></script>
<title>Europeana Creative - Image Retrieval Demo</title>
</head>
<body>
<!-- 
<body onload="showMoreSearchOptions();">
 -->
<%String keywords = "";%>
<div class="wrapper">
		<div id="header"><br>
			<div style="margin-bottom: 10px; vertical-align: middle;">
			<img class="header"	src="images/cnr.logo.jpg" border="0" alt="">
			<img class="header" src="images/ait.logo.jpg" border="0" alt="">
			</div>
			
			<div align="center">
			<!-- 
			<a href="./dataset.html">Dataset description</a> 
			 -->
			<table><tr><td>
			<%@include file='searchBar.jsp'%>
			</td>
			</tr></table>
		</div>
		<script type="text/javascript" language="javascript">document.getElementById('moreSearchOptionsButton').style.display = '';</script>		
		
     	</div>
        <div id="content" align="center">
        	<table>
        		<%
        			randomImages.openProps(advOptions.getDatasetFile());
		        	for (int i = 0; i < 12; i++) {
		        		if (i % 4 == 0) 
		        		out.print("<tr>");%>
						<td colspan="1" align="center" style="padding-right: 10px;">
						<%
							String id = randomImages.getRandomId();
							String objectUrl = randomImages.getThumbnailUrl(id);
						%>
						<div align="left"><a href="./Search?id=<%=id%>&features=<%=advOptions.getFeaturesAsString()%>&src=rnd"
							title="search similar images">similar</a> &nbsp;
							<div>
							<a href="<%=objectUrl%>" title="Vie image from original location" target="_blank"><img
							src="<%=objectUrl%>" border="0" alt="" width="200"></a></div>
							</div>
						</td>
						<%} 
		        		//randomImages.closeProps();
						%>
					</table>

        <tr>
		<td colspan="6" align="center"><a class="otherRandomImages"
			href="./index" title="More random images"><img
			src="./images/view-refresh.png" border="0" alt=""></a></td>
	</tr>
		</table>
        </div>
        <div class="push"></div>
        </div>
        <div class="footer">
        
        </div>
</body>
</html>
