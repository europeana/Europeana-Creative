<?php
/*
Plugin Name: Van Go Yourself
Description: Plugin for reenactment of images.
Version: 1.0.0
Author: Bojan Božić and Sergiu Gordea
Author URI: http://www.unet.univie.ac.at/~a0963121
*/
?>
<head>
<title>Van Go Yourself</title>
</head>

<script>
function fileupload(v) {
	var iframe = document.getElementById('target');
	iframe.contentDocument.body.innerHTML = '<img id="preview" src="uploads/' + v + '">';
	$(document).ready(function () {
	    iframe.contentDocument.getElementById('preview').imgAreaSelect({ x1: 120, y1: 90, x2: 280, y2: 210 });
	});
}

function selection() {
	alarm("selection");
}
</script>
			
<h1><?php echo "Van Go Yourself" ?> </h1>
<hr>
<form enctype="multipart/form-data" action="result.php" method="POST">
<table>
<tr><td>Original image:</td><td><input type="file" name="file1" onchange="fileupload(this.value)"></td></tr>
<tr><td>Reenactment:</td><td><input type="file" name="file2"></td></tr>
</table>
<hr>
<!--iframe id="target" frameborder=0 src="preview.html" style="width:100%; height:25%;">
</iframe-->
<table>
<tr>Selection coordinates:</tr>
<tr><td>X:</td><td><input type="text" name="x"></td><td>Width:</td><td><input type="text" name="width"></td></tr>
<tr><td>Y:</td><td><input type="text" name="y"></td><td>Height:</td><td><input type="text" name="height"></td></tr>
<tr><td>Thumbnail width:</td><td><input type="text" name="th_width"></td></tr>
</table>
<hr>
<table>
<tr><td>Name of painting:</td><td><input type="text" name="txt_painting"></td></tr>
<tr><td>Name of artist:</td><td><input type="text" name="txt_artist"></td></tr>
<tr><td>Date of painting:</td><td><input type="text" name="txt_date"></td></tr>
<tr><td>Short URL:</td><td><input type="text" name="txt_shorturl"></td></tr> 
</table>
<hr>
<input type="radio" name="hv" value="Horizontal">Landscape<br>
<input type="radio" name="hv" value="Vertical">Portrait
<hr>
Color code: <input type="text" name="color_code"><br>
Thickness: <input type="text" name="thickness"><br>
Frame Image: <input type="file" name="frame_image" onchange="fileupload(this.value)"><br>
Top right corner: <input type="file" name="top_right" onchange="fileupload(this.value)"><br>
<input type="radio" name="frame" value="Color Frame">Color Frame<br>
<input type="radio" name="frame" value="Standard Frame">Standard Frame<br>
<input type="radio" name="frame" value="Image Frame">Image Frame
<hr>
<input type="submit" value="Load">
</form>
<?php 
/**
 * Creates an input form for image uploads
 */
function image_uploads() {
	echo "image_uploads invocation";
}

add_filter('the_content', 'image_uploads');
?>