function successTip(title, msg, timeout) {
	var tt = 1000;
	if(timeout != undefined) {
		tt = timeout;
	}
	$.messager.show({
        title:title,
        msg:msg,
        showType:'fade',
        timeout: tt,
        style:{
            right:'',
            bottom:''
        }
    });
}

function errorTip(msg) {
	$("#message").show();
	$("#message").addClass("alert alert-danger");
	$("#message").html(msg);
}

function warningTip(title, msg) {
	$.messager.alert(title, msg,'warning');
}