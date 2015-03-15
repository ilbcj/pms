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

$.fn.calendar.defaults.weeks = ['日','一','二','三','四','五','六'];
$.fn.calendar.defaults.months = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一', '十二'];
$.extend($.fn.datetimebox.defaults,{
	currentText:'今天',
	closeText:'关闭',
	okText:'确定'
});

//	$.fn.datebox.defaults.formatter = function(date){
//	var y = date.getFullYear();
//	var m = date.getMonth()+1;
//	var d = date.getDate();
//	alert(y+'/'+m+'/'+d);
//	return y+'/'+m+'/'+d;
//}