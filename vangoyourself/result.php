<head>
<title>Test | Van Go Yourself</title>
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
if(!($width == "" && $height == "" && $x == "" && $y == ""))
{
	if($width == "")
	{$width = $geo1['width'];}
	if($height == "")
	{$height = $geo1['height'];}
	if($x == "")
	{$x = 0;}
	if($y == "")
	{$y = 0;}
	$image2->cropimage($width, $height, $x, $y);
}
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

if($_POST['frame'] == 'Color Frame') {
	$result->frameimage($_POST['color_code'], $_POST['thickness'], $_POST['thickness'], 5, 5);
} else if($_POST['frame'] == 'Standard Frame') {
	$result->borderimage('#000000', 2, 2); // Black
	$result->borderimage('#A0522D', 3, 3); // Siena
	$result->borderimage('#000000', 1, 1); // Black
	$result->borderimage('#000000', 2, 2); // Black
	$result->borderimage('#000000', 2, 2); // Black
	$result->borderimage('#CD853F', 3, 3); // Peru
	$result->borderimage('#000000', 1, 1); // Black
	$frame = new Imagick("uploads/fancy_add.gif");
	$result->compositeimage($frame, Imagick::COMPOSITE_OVER, 0, 0);
	$frame->flipImage();
	$result->compositeimage($frame, Imagick::COMPOSITE_OVER, 0, $result->getimagegeometry()['height']-48);
	$frame->flopimage();
	$result->compositeimage($frame, Imagick::COMPOSITE_OVER, $result->getimagegeometry()['width']-48, $result->getimagegeometry()['height']-48);
	$frame->flipimage();
	$result->compositeimage($frame, Imagick::COMPOSITE_OVER, $result->getimagegeometry()['width']-48, 0);
} else if($_POST['frame'] == 'Image Frame') {
	$frame_image = new Imagick($base_path . basename($_FILES['frame_image']['name']));
	$geo_frame = $frame_image->getimagegeometry();
	$geo_result = $result->getimagegeometry();
	$framed_result = new Imagick();
	for($i = 0; $i < $geo_result['width']; $i += $geo_frame['width']) {
		$result->compositeimage($frame_image, imagick::COMPOSITE_DEFAULT, $i, 0);
		$frame_image->flipimage();
		$result->compositeimage($frame_image, imagick::COMPOSITE_DEFAULT, $i, $geo_result['height']-$geo_frame['height']);
		$frame_image->flipimage();
	}
	for($i = 0; $i < $geo_result['height']; $i += $geo_frame['width']) {
		$frame_image->rotateimage(new ImagickPixel('none'), 270);
		$result->compositeimage($frame_image, imagick::COMPOSITE_DEFAULT, 0, $i);
		$frame_image->rotateimage(new ImagickPixel('none'), -180);
		$result->compositeimage($frame_image, imagick::COMPOSITE_DEFAULT, $geo_result['width']-$geo_frame['height'], $i);
		$frame_image->rotateimage(new ImagickPixel('none'), -90);
	}
	$top_right = new Imagick($base_path . basename($_FILES['top_right']['name']));
	$geo_topright = $top_right->getimagegeometry();
	$result->compositeimage($top_right, imagick::COMPOSITE_DEFAULT, $geo_result['width']-$geo_topright['width'], 0);
	$top_right->flipimage();
	$result->compositeimage($top_right, imagick::COMPOSITE_DEFAULT, $geo_result['width']-$geo_topright['width'], $geo_result['height']-$geo_topright['height']);
	$top_right->flopimage();
	$result->compositeimage($top_right, imagick::COMPOSITE_DEFAULT, 0, $geo_result['height']-$geo_topright['height']);
	$top_right->flipimage();
	$result->compositeimage($top_right, imagick::COMPOSITE_DEFAULT, 0, 0);
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