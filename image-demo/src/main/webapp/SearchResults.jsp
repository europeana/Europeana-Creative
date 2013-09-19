<%@page import="it.cnr.isti.cophir.ui.tools.UITools"%>
<jsp:useBean id="imageSearchBean" scope ="session" class="it.cnr.isti.cophir.ui.bean.SearchBean"/>
<jsp:useBean id="advOptions" scope="session"
    class="it.cnr.isti.cophir.ui.bean.Parameters" />
<jsp:useBean id="randomImages" scope="session"
	class="it.cnr.isti.cophir.ui.bean.RandomImages" />
	
<html>
    <head>
<link rel="icon" href="favicon.ico" />
<link rel="shortcut icon" href="favicon.ico" />
<link rel="stylesheet" type="text/css" href="uiStyle.css">
<script type="text/javascript" src="ui.js"></script>

<title>Europeana Creative - Image Retrieval Demo</title>
</head>
<%
String imageQueryURL = null;
String keywords = "";
String rgb = "";
String imgQueryID = null;
String searchParamName = "id";
long searchTime = 0;

searchTime = imageSearchBean.getSearchTime();

imgQueryID = imageSearchBean.getMediaUri();
System.out.println("imgQueryID: " + imgQueryID);

//TODO:move this business code to the right place
if(imgQueryID != null && !imgQueryID.equals(imageSearchBean.getImageQueryURL())){
	//search by ID
	imageQueryURL = randomImages.getThumbnailUrl(imgQueryID);
}else{
	//search by URL
	imageQueryURL = imageSearchBean.getImageQueryURL();
	searchParamName = "imgUrl";
}

System.out.println("imageQueryURL: " + imageQueryURL);


if (imageQueryURL != null && (imgQueryID == null || imgQueryID.equals(""))) {
	imgQueryID = imageQueryURL;
}

	if (imageSearchBean.getXQueryValues() != null) {
		for (int index = 0; index < imageSearchBean.getXQueryValues().length; index++) {
			if (imageSearchBean.getXQueryValues()[index] != null && !imageSearchBean.getXQueryValues()[index].equals("")&& !imageSearchBean.getXQueryFields()[index].equals("Image") && !imageSearchBean.getXQueryFields()[index].equals("id")) {              
				keywords += imageSearchBean.getXQueryValues()[index];		
			}
		}
	}
	
String beanID = "similaritySearch";
    String url ="Search";
    String[][] imageResults = imageSearchBean.getResults();
    
    int[] resultsRange = imageSearchBean.getResultsRange();
    String range = "";
    
%>
<body onLoad="changeImage('<%=imageQueryURL%>', '<%=imgQueryID%>');">
       
<div class="wrapper" style="min-height: 900px;">
    <table>
        <tr>
        <td>
        <a href="index">
        <!-- <img src="images/cnr.logo.thumb.jpg" border="0" alt="Go to UI home page" align="bottom" title ="Go to UI home page">
        <img src="images/ait.logo.small.jpg" border="0" alt="Go to UI home page" align="bottom" title ="Go to UI home page"></a></td>
         -->
        <img src="images/eucreative.logo.small.jpg" border="0" alt="Go to home page" align="bottom" title ="Go to home page"></a></td>
        <td><%@include file='searchBar.jsp'%></td>
        </tr>
        </table>

<div style="background-color: rgb(240, 247, 255); border-top-color: rgb(107, 144, 218); border-top-style: solid; border-top-width: 1px; padding: 0 5px 0 5px; margin: 10px 0 0 0">
 
             <%
              	if(resultsRange[1] != 0) {
                          	 range = "results <b>" + resultsRange[0] + "</b>-<b>" + resultsRange[1] + "</b>";
              %>
	
            <%
	            	}
	            %>
            <div style="float: right;"><%=range%> &nbsp;&nbsp;&nbsp;search time: <b><%=searchTime%></b> ms</div>
            
            <div style="clear: both"></div>
         
 	</div>

        <div class="content" align="center">
    
        <%
        	if (imageResults == null || imageResults[0][0] == null) {
        %>
            <h3 align="center">No Items found!</h3>
        <%
        	}
                else {
        %>
            <table border="0" align="center" >
                <%
                	int colonne = -1;   
                                for (int i = 0; i < imageResults.length; i++) {
                                    colonne++;
                                    if (colonne%5 == 0) {
                                        out.print("<tr valign=top>");
                                    }
                                    if (imageResults[i][0] != null) {
                %>
                        <td valign="top">
                        	<div id="result_<%=i%>" style="padding: 5px;">

<%@include file='ImageResult.jspf'%>
</div> 

                        </td>
                        
                    <%}
                }%>
            </table>
            <br>
        <%}%>
        <br>
        <table width="100%" border="0">
    <tr>
        <td align="right" width="50%">
            <%
                if(imageSearchBean.prev()) {
            %>
            <a href="Search?<%=searchParamName%>=<%=imgQueryID%>&page=<%=imageSearchBean.getPage() - 1%>&src=page<%=imageSearchBean.getPage() - 1%>" TITLE="previous"><img src="./images/prev.gif" BORDER="0"></a> 
            <%}%>
        </td>
        
        <td align="left" width="50%">
            <%
                if(imageSearchBean.next()) {
            %>
            <a href="Search?<%=searchParamName%>=<%=imgQueryID%>&page=<%=imageSearchBean.getPage() + 1%>&src=page<%=imageSearchBean.getPage() + 1%>" TITLE="next"><img src="./images/next.gif" BORDER="0"></a> 
            <%}%>
        </td>
    </tr>
</table>
		</div>
        <div class="push"></div>
        </div>
        <div class="footer">
        <hr>
		<%@include file='footer.jsp'%>
        
        </div>
    </body>
</html>