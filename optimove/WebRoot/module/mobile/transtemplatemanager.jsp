<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>

<html>
  <head>
    <title>管理</title>
   <jsp:include page="/module/common/comm.jsp"></jsp:include>
   <script type="text/javascript"  charset="GBK">
   var searchUrl = "<%=request.getContextPath()%>/transtemplatemanagers/searchTranstemplatemanager.do";
   var updateUrl = "<%=request.getContextPath()%>/transtemplatemanagers/update.do";
   var insertUrl = "<%=request.getContextPath()%>/transtemplatemanagers/insert.do";
   var deleteUrl = "<%=request.getContextPath()%>/transtemplatemanagers/delete.do";
   ajaxConstants("tblLookupValues|LookupCode|Meaning|LookupType='useChannel';orderBy=LookupCode");
	
	$(function() {
	    $('#dataList').datagrid({  
	        title:'列表',  
	        iconCls:'icon-edit',//图标  
	        //width: 700,  
	        height: 'auto',  
	        nowrap: false,  
	        striped: true,  
	        border: true,  
	        collapsible:false,//是否可折叠的  
	        fit: true,//自动大小  
	        url:'#',  
	        remoteSort:false,   
	        singleSelect:false,//是否单选  
	        pagination:true,//分页控件  
	        rownumbers:true,//行号  
	        frozenColumns:[[  
	            {field:'ck',checkbox:true}  
	        ]],  
	        url:searchUrl, 
	        toolbar:'#tb',
	        columns:[[   
                   		{field:'templatecode',title:'模板代码',width:100,align:'center'},
                   		{field:'templatename',title:'模板名称',width:100,align:'center'},
                   		{field:'templateobject',title:'模板对象',width:100,align:'center'},
                   		{field:'linkurl',title:'引用地址',width:100,align:'center'},
                   		{field:'usechannel',title:'使用渠道',width:100,align:'center',formatter: function(value,row,index){ 
           		    		return getConstantDisplay('tblLookupValues',value); 
           				}},
                   		{field:'operator',title:'操作人员',width:100,align:'center'},
                   		{field:'operatororg',title:'操作机构',width:100,align:'center'},
                   		{field:'updatedate',title:'操作日期',width:100,align:'center'},
                   		{field:'updatetime',title:'操作时间',width:100,align:'center'}
	        ]],
	        onLoadSuccess:function(data){
	    		data = convertJson(data);
	        	if(data.result!='ok'){
	        		showBox("提示信息",data.errorMsg,'warning');
	        	}
	        }
	        
	    });  
	
	    //设置分页控件  
	    var p = $('#dataList').datagrid('getPager');  
	    $(p).pagination({  
	        pageSize: 10,//每页显示的记录条数，默认为10  
	        pageList: [10,20,30],//可以设置每页记录条数的列表  
	        beforePageText: '第',//页数文本框前显示的汉字  
	        afterPageText: '页    共 {pages} 页',  
	        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	    })
	});
	</script>
  </head>
  
  <body class="easyui-layout" >
	<div  region="center" >
		<div id='dataList'>
			<div id="tb" style="padding:5px;height:auto">
		<div style="margin-bottom:5px">
			<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showAddwindow({title:'新增'})">新增</a>|
			<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="showUpdate({title:'修改',readonlyFields:['templatecode']});">修改</a>|
			<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delRowData({id:'templatecode'});">删除</a>
		</div>
		<div>
			<form  id='searchForm' action="" method="post">
				模板代码:
				<input type="text" id="templatecode" name="templatecode"/>
				模板名称:
				<input type="text" id="templatename" name="templatename"/>
				使用渠道:
				<select id="usechannel" name="usechannel" constantId="tblLookupValues" style="width:120px"></select>
				
				<input type="button" onclick="loadList(1);" value="查询"/>
			</form>
		</div>
	</div>
		</div>
	</div>
	
	<div style="visibility:hidden" >
		<div id="addwindow"  title="添加" style="width:600px;height:350px;padding:10px">
			<form id='addForm' action="" method="post">
				<table>
						<tr>
							<td>模板代码:</td>
							<td><input type="text" id="templatecode" name="templatecode" style="width:120px"/></td>
						</tr>
						<tr>
							<td>模板名称:</td>
							<td><input type="text" id="templatename" name="templatename" style="width:120px"/></td>
						</tr>
						<tr>
							<td>模板对象:</td>
							<td><input type="text" id="templateobject" name="templateobject" style="width:120px"/></td>
						</tr>
						<tr>
							<td>引用地址:</td>
							<td><input type="text" id="linkurl" name="linkurl" style="width:120px"/></td>
						</tr>
						<tr>
							<td>使用渠道:</td>
							<td><select id="usechannel" name="usechannel" constantId="tblLookupValues" style="width:120px"></select></td>
					
						</tr>
						
				</table>
			</form>
		</div>
	</div>
	
  </body>
</html>
