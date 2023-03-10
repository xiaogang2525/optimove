if ($.fn.pagination){
	$.fn.pagination.defaults.beforePageText = 'P??gina';
	$.fn.pagination.defaults.afterPageText = 'de {pages}';
	$.fn.pagination.defaults.displayMsg = "Veient {from} a {to} de {total} d'articles";
}
if ($.fn.datagrid){
	$.fn.datagrid.defaults.loadMsg = 'Elaboraci??, si us plau esperi ...';
}
if ($.fn.treegrid && $.fn.datagrid){
	$.fn.treegrid.defaults.loadMsg = $.fn.datagrid.defaults.loadMsg;
}
if ($.messager){
	$.messager.defaults.ok = 'Ok';
	$.messager.defaults.cancel = 'Cancel';
}
if ($.fn.validatebox){
	$.fn.validatebox.defaults.missingMessage = 'Aquest camp ??s obligatori.';
	$.fn.validatebox.defaults.rules.email.message = 'Introdu?u una adre?a de correu electr??nic v??lida.';
	$.fn.validatebox.defaults.rules.url.message = 'Si us plau, introdu?u un URL v??lida.';
	$.fn.validatebox.defaults.rules.length.message = 'Si us plau, introdu?u un valor entre {0} i {1}.';
}
if ($.fn.numberbox){
	$.fn.numberbox.defaults.missingMessage = 'Aquest camp ??s obligatori.';
}
if ($.fn.combobox){
	$.fn.combobox.defaults.missingMessage = 'Aquest camp ??s obligatori.';
}
if ($.fn.combotree){
	$.fn.combotree.defaults.missingMessage = 'Aquest camp ??s obligatori.';
}
if ($.fn.combogrid){
	$.fn.combogrid.defaults.missingMessage = 'Aquest camp ??s obligatori.';
}
if ($.fn.calendar){
	$.fn.calendar.defaults.weeks = ['S','M','T','W','T','F','S'];
	$.fn.calendar.defaults.months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
}
if ($.fn.datebox){
	$.fn.datebox.defaults.currentText = 'Avui';
	$.fn.datebox.defaults.closeText = 'Tancar';
	$.fn.datebox.defaults.okText = 'Ok';
	$.fn.datebox.defaults.missingMessage = 'Aquest camp ??s obligatori.';
}
if ($.fn.datetimebox && $.fn.datebox){
	$.extend($.fn.datetimebox.defaults,{
		currentText: $.fn.datebox.defaults.currentText,
		closeText: $.fn.datebox.defaults.closeText,
		okText: $.fn.datebox.defaults.okText,
		missingMessage: $.fn.datebox.defaults.missingMessage
	});
}
