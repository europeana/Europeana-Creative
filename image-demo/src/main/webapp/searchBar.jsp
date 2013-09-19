<%@page import="java.util.ArrayList"%>

<!--
<iframe src="" frameborder="0" width="0" height="0" id="setFrame" name="setFrame"></iframe>
 
 	 <table>
			 <tr id="comboInfo"  style="display: none;">
				<td align="left" style="padding-right: 5px;" title="Bag of Features"><input type="checkbox" style="margin-right: 1px;" name="bof" id="bof" value="BOF" title="use BoF" checked onclick="window.frames[0].location.href='SetAdvancedOptions?feature=BOF';  setFeatures();">objects</input></td>
				<td style="border: solid 1px #CCCC77;" title="MPEG-7">
				<table><tr>
				<td align="left" style="padding-right: 5px;"><input type="checkbox" name="mp7SC" id="mp7SC" value="MP7SC" title="use SC"  onclick="document.getElementById('setFrame').src='SetAdvancedOptions?feature=MP7SC';  setFeatures();">global color</input></td>
				<td align="left" style="padding-right: 5px;"><input type="checkbox" style="margin-right: 1px;" name="mp7CS" id="mp7CS" value="MP7CS" title="use CS"  onclick="window.frames[0].location.href='SetAdvancedOptions?feature=MP7CS';  setFeatures();">color structure</input></td>
				<td align="left" style="padding-right: 5px;"><input type="checkbox" style="margin-right: 1px;" name="mp7CL" id="mp7CL" value="MP7CL" title="use CL"  onclick="window.frames[0].location.href='SetAdvancedOptions?feature=MP7CL';  setFeatures();">color layout</input></td>
				<td align="left" style="padding-right: 5px;"><input type="checkbox" style="margin-right: 1px;" name="mp7EH" id="mp7EH" value="MP7EH" title="use EH"  onclick="window.frames[0].location.href='SetAdvancedOptions?feature=MP7EH';  setFeatures();">edges</input></td>
				<td align="left"><input type="checkbox" style="margin-right: 1px;" name="mp7HT" id="mp7HT" value="MP7HT" title="use HT"  onclick="window.frames[0].location.href='SetAdvancedOptions?feature=MP7HT';  setFeatures();">texture</input></td>
</tr></table></td>
			</tr>
		</table>
 -->		
		
		<table>
			<tr>
				<td>
				<div style="display: none;" id="advSearch" align="center">
				<img id="queryImage"
					src="queryImg" alt="" height="64" align="middle">
				<!-- 
				<img src="images/close.png"  onclick="changeImage('', '');" border="0" align="top" title="remove the image"> 
				 -->
					
					</div>
					
				</td>
				<td colspan="3">
				<form id="searchbar" method="GET" enctype="" name="form1" action="./Search" onsubmit="setFeatures();">
					<input type="hidden" value="form" name="src">
				
				    <table cellspacing="1" cellpadding="1" border="0">                          
					     <tr>                   
					    <td valign="top">
						<input type="hidden" value="" name="" id="objId">
						<input type="hidden" value="" name="features" id="features">
						<!-- 
						<input type="text" name="value0" id="keywords" value="" size="50">
						<input type="hidden" value="text" name="field0">
						 -->
						<input type="text" name="imgUrl" id="imgUrl" value="" size="50">
						
						</td>                   
					    <td valign="top">
							<input type="submit" value="Search" title="search" name="sumbit">
						</td>
					    <!-- 
					     	<td style="display: none; padding-left: 10px;" id="moreSearchOptionsButton">
					     	<a href="Show more search options" title="Show more search options" onclick="showMoreSearchOptions(); return false;">
					     	<img id="expandSearch" src="images/downarrow.png" border="0"></a>
					     	
					    </td>
					     --> 
					</tr>
					<!-- 
					<tr>
						<td colspan="2">
									<div id="moreSearchOptions" style="">
										 <h5>upload image</h5>
									<input id="imageToUpload" name="fileName" size="38" onclick="changeImage('', '');" type="file">
									</div>
						</td>
					 
					</tr>
					 -->
				   </table>
				   
				</form>
				</td>
			</tr>
		</table>