function changeCheckBoxValue(checkBoxId){
	let checkBox = document.getElementById(checkBoxId);
	if (checkBox.value == "true") 
		checkBox.value = "false";
	else
		checkBox.value = "true";
}

function dismissSuccessAlert(){
	document.getElementById("success-alert").remove();
}