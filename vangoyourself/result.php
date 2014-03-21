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
<!--[if lt IE 9]>
<link rel="stylesheet" id="twentyfourteen-ie-css"  href="http://localhost/wordpress/wordpress/trunk/wp-content/themes/twentyfourteen/css/ie.css?ver=20131205" type="text/css" media="all" />
<![endif]-->
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

<?php
$base_path = "uploads/";
$target_path1 = $base_path . basename($_FILES['file1']['name']);
$target_path2 = $base_path . basename($_FILES['file2']['name']);
$x = $_POST['x'];
$y = $_POST['y'];
$width = $_POST['width'];
$height = $_POST['height'];
$th_width = $_POST['th_width'];

if(move_uploaded_file($_FILES['file1']['tmp_name'], $target_path1) && move_uploaded_file($_FILES['file2']['tmp_name'], $target_path2)) {

$image1 = new Imagick($target_path1);
$image2 = new Imagick($target_path2);

$httppath = 'http://' . $_SERVER['SERVER_NAME'] . dirname($_SERVER['REQUEST_URI']);

$geo1 = $image1->getimagegeometry();
$image2->cropimage($width, $height, $x, $y);
$geo2 = $image2->getimagegeometry();

session_start();
$datedir = date("d-m-y");
$sessioniddir = session_id();
if(!is_dir(getcwd() . "/" . $base_path . $datedir)) {
	mkdir(getcwd() . "/" . $base_path . $datedir);
}
if(!is_dir(getcwd() . "/" . $base_path . $datedir . "/" . $sessioniddir)) {
	mkdir(getcwd() . "/" . $base_path . $datedir . "/" . $sessioniddir);
}
$target_path3 = $base_path . $datedir . "/" . $sessioniddir . "/" . time() . "-large.jpg";
$resulturi = $httppath . $datedir . "/" . $sessioniddir . "/" . time() . "-large.jpg";

$icol = new Imagick();
if($_POST['hv'] == 'Vertical') {
	if($geo1['width'] > $geo2['width']) {
		$image1->resizeimage($geo2['width'], 0, Imagick::FILTER_POINT, 1);
	}
	else {
		$image2->resizeimage($geo1['width'], 0, Imagick::FILTER_POINT, 1);
	}
	$icol->addimage($image1);
	$icol->addimage($image2);
	$icol->resetiterator();
	$result = $icol->appendimages(true);
} else if($_POST['hv'] == 'Horizontal') {
	if($geo1['height'] > $geo2['height']) {
		$image1->resizeimage(0, $geo2['height'], Imagick::FILTER_POINT, 1);
	}
	else {
		$image2->resizeimage(0, $geo1['height'], Imagick::FILTER_POINT, 1);
	}
	$icol->addimage($image1);
	$icol->addimage($image2);
	$icol->resetiterator();
	$result =$icol->appendimages(false);
}

$result->writeimage($target_path3);
$result->resizeimage($th_width, 0, Imagick::FILTER_POINT, 1);
$thumbpath = $base_path . $datedir . "/" . $sessioniddir . "/" . time() . "-small.jpg";
$resultthumburi = $httppath . $datedir . "/" . $sessioniddir . "/" . time() . "-small.jpg";
$result->writeimage($thumbpath);

	echo "<img src=$target_path3><img src=$thumbpath><br><br>";
	echo "Image URI: " . $resulturi . "<br>";
	echo "Thumbnail URI: " . $resultthumburi;
} 
else {
	echo 'There was an error uploading the file, please try again!';
}

?>