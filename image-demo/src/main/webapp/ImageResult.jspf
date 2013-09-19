<%@page import="it.cnr.isti.cophir.ui.index.IndexSupport"%>
<%  String id = imageResults[i][1];
    String score = imageResults[i][0];
    String objectUrlSmall = randomImages.getThumbnailUrl(id);%>

<div>
<%
StringBuilder tooltip = new StringBuilder();
%>

<div>
<a href="<%=url%>?id=<%=id%>&features=<%=advOptions.getFeaturesAsString()%>&src=res" title="search similar images">similar</a> &nbsp;
<img style="background-color: white; border-color: black; border-width: 10;" src="./images/<%=imageSearchBean.getScoreBar(score)%>" title="score: <%=score%>"> 
<br>
<span>

<%if (objectUrlSmall.startsWith("./")) {%>
<a href="objectUrlSmall" title="Full Size View" target="_blank"><img
							src="<%=objectUrlSmall%>" border="0" alt="" width="200"></a>
<%} else { %>
<img style="border-color: #AACCE1;" src="<%=objectUrlSmall%>" border="0" title="<%=id %>">
<%} %>


</span>
</div>

</div>                 