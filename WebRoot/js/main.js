function successTip(title, msg, timeout) {
	$.messager.show({
        title:title,
        msg:msg,
        showType:'fade',
        timeout: timeout,
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