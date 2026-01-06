$(document).ready(function() {
 
	//syntax highlighter
	hljs.tabReplace = '    ';
	hljs.initHighlightingOnLoad();
 
	//collapsible management
	$('.collapsible').collapsible({
		defaultOpen: 'section1',
		cookieName: 'nav'
	});
	$('.page_collapsible').collapsible({
		defaultOpen: 'body-section1',
		cookieName: 'body'
	});
 
});
 
function display_c(){
	var refresh=1000; // Refresh rate in milli seconds
	mytime=setTimeout('display_ct()',refresh);
}
 
function display_ct() {
	var x = new Date();
	document.getElementById('ct').innerHTML = x;
	tt=display_c();
}
 
function GRCollapsePane()
{
	var isGRListVisible = document.getElementById("GR_List").style.display;
	//alert("GR Collapse"+isGRListVisible);
	if (isGRListVisible == "none")
	{
		document.getElementById("GR_List").style.display = "block";
	}
	else if (isGRListVisible == "block")
	{
		document.getElementById("GR_List").style.display = "none";
	}
	return false;
}
 
function BOECollapsePane()
{
	var isBOEListVisible = document.getElementById("BOE_List").style.display;
	if (isBOEListVisible == "none")
	{
		document.getElementById("BOE_List").style.display = "block";
	}
	else if (isBOEListVisible == "block")
	{
		document.getElementById("BOE_List").style.display = "none";
	}
	return false;
}
 
	