<?php
/*
 Plugin Name: Van Go Yourself
Plugin URI: http://localhost/wordpress/vangoyourself
Description: Plugin for reenactment of images.
Version: 1.0.0
Author: Bojan Božić
Author URI: http://www.unet.univie.ac.at/~a0963121
*/
?>
<head>
<meta charset="UTF-8">
<meta content="width=device-width" name="viewport">
<title>Test | Van Go Yourself</title>
<link href="http://gmpg.org/xfn/11" rel="profile">
<link href="http://localhost/wordpress/wordpress/trunk/xmlrpc.php" rel="pingback">
<!--[if lt IE 9]>
<script src="http://localhost/wordpress/wordpress/trunk/wp-content/themes/twentyfourteen/js/html5.js"></script>
<![endif]-->
<meta content="noindex,follow" name="robots">
<link href="http://localhost/wordpress/wordpress/trunk/?feed=rss2" title="Van Go Yourself » Feed" type="application/rss+xml" rel="alternate">
<link href="http://localhost/wordpress/wordpress/trunk/?feed=comments-rss2" title="Van Go Yourself » Comments Feed" type="application/rss+xml" rel="alternate">
<link href="http://localhost/wordpress/wordpress/trunk/?feed=rss2&amp;p=9" title="Van Go Yourself » Test Comments Feed" type="application/rss+xml" rel="alternate">
<link media="all" type="text/css" href="//fonts.googleapis.com/css?family=Open+Sans%3A300italic%2C400italic%2C600italic%2C300%2C400%2C600&amp;subset=latin%2Clatin-ext&amp;ver=3.9-alpha-27445" id="open-sans-css" rel="stylesheet">
<link media="all" type="text/css" href="http://localhost/wordpress/wordpress/trunk/wp-includes/css/dashicons.min.css?ver=3.9-alpha-27445" id="dashicons-css" rel="stylesheet">
<link media="all" type="text/css" href="http://localhost/wordpress/wordpress/trunk/wp-includes/css/admin-bar.min.css?ver=3.9-alpha-27445" id="admin-bar-css" rel="stylesheet">
<link media="all" type="text/css" href="//fonts.googleapis.com/css?family=Lato%3A300%2C400%2C700%2C900%2C300italic%2C400italic%2C700italic" id="twentyfourteen-lato-css" rel="stylesheet">
<link media="all" type="text/css" href="http://localhost/wordpress/wordpress/trunk/wp-content/themes/twentyfourteen/genericons/genericons.css?ver=3.0.2" id="genericons-css" rel="stylesheet">
<link media="all" type="text/css" href="http://localhost/wordpress/wordpress/trunk/wp-content/themes/twentyfourteen/style.css?ver=3.9-alpha-27445" id="twentyfourteen-style-css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="jquery.imgareaselect-0.9.10/css/imgareaselect-default.css" />
<script type="text/javascript" src="jquery.imgareaselect-0.9.10/scripts/jquery.min.js"></script>
<script type="text/javascript" src="jquery.imgareaselect-0.9.10/scripts/jquery.imgareaselect.pack.js"></script>
<script type="text/javascript" src="jquery.imgareaselect-0.9.10/scripts/jquery.imgareaselect.js"></script>
<script type="text/javascript" src="jquery.imgareaselect-0.9.10/scripts/jquery.imgareaselect.min.js"></script>
<script src="http://localhost/wordpress/wordpress/trunk/wp-includes/js/jquery/jquery.js?ver=1.11.0" type="text/javascript"></script>
<script src="http://localhost/wordpress/wordpress/trunk/wp-includes/js/jquery/jquery-migrate.min.js?ver=1.2.1" type="text/javascript"></script>
<link href="http://localhost/wordpress/wordpress/trunk/xmlrpc.php?rsd" title="RSD" type="application/rsd+xml" rel="EditURI">
	<link href="http://localhost/wordpress/wordpress/trunk/wp-includes/wlwmanifest.xml" type="application/wlwmanifest+xml" rel="wlwmanifest">
	<meta content="WordPress 3.9-alpha-27445" name="generator">
	<link href="http://localhost/wordpress/wordpress/trunk/?p=9" rel="canonical">
	<link href="http://localhost/wordpress/wordpress/trunk/?p=9" rel="shortlink">
	<style type="text/css">.recentcomments a{display:inline !important;padding:0 !important;margin:0 !important;}</style>
	<style media="print" type="text/css">#wpadminbar { display:none; }</style>
	<style media="screen" type="text/css">
			html { margin-top: 32px !important; }
			* html body { margin-top: 32px !important; }
			@media screen and ( max-width: 782px ) {
				html { margin-top: 46px !important; }
				* html body { margin-top: 46px !important; }
			}
			</style>
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
<input type="submit" value="Load">
</form>
<?php 
/**
 * Creates an input form for image uploads
 */
function image_uploads() {
	//echo "image_uploads invokation";
}

// add_filter('the_content', 'image_uploads');
?>