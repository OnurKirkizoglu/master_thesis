/*
*
* Created by Michael Alexander Tröls, 01.06.2016
*
*/

var jfxTest = function(id, version){
	
	var depth = 3;
	var maxArt = 7;
	var xmlhttp = new XMLHttpRequest();
	var url = "http://localhost:8080/designspace/viewer/artifact/id="+id+"&v="+version+"&l="+depth+"&a="+maxArt;
	//document.getElementById("TestOutput").innerHTML = url;
	
	xmlhttp.open("GET", url, true);
	xmlhttp.send();

	xmlhttp.onreadystatechange = function() {
	    if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
	        var jsonObject= JSON.parse(xmlhttp.responseText);
	        drawArtifacts(jsonObject);
	    }
	};
}