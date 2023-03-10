<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>

<html>
  <head>
    <title>管理</title>
   <jsp:include page="/module/common/comm.jsp"></jsp:include>
   <script type="text/javascript"  charset="GBK">
   var searchUrl = "<%=request.getContextPath()%>/systemmanagers/searchSystemmanager.do";
   var updateUrl = "<%=request.getContextPath()%>/systemmanagers/update.do";
   var insertUrl = "<%=request.getContextPath()%>/systemmanagers/insert.do";
   var deleteUrl = "<%=request.getContextPath()%>/systemmanagers/delete.do";
   ajaxConstants("tblLookupValues|LookupCode|Meaning|LookupType='accessType';orderBy=LookupCode");
	
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
                   		{field:'syscode',title:'系统代码',width:100,align:'center'},
                   		{field:'sysname',title:'系统名称',width:100,align:'center'},
                   		{field:'sysdescription',title:'系统说明',width:100,align:'center'},
                   		{field:'accesstype',title:'接入方式',width:100,align:'center',formatter: function(value,row,index){ 
           		    		return getConstantDisplay('tblLookupValues',value); 
           				}},
                   		{field:'sysip',title:'IP地址',width:100,align:'center'},
                   		{field:'sysport',title:'端口',width:100,align:'center'},
                   		{field:'sysuser',title:'使用用户',width:100,align:'center'},
                   		{field:'syspwd',title:'使用密码',width:100,align:'center'},
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
			<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="showUpdate({title:'修改',readonlyFields:['syscode']});">修改</a>|
			<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delRowData({id:'syscode'});">删除</a>
		</div>
		<div>
			<form  id='searchForm' action="" method="post">
				系统代码:
				<input type="text" id="syscode" name="syscode"/>
				系统名称:
				<input type="text" id="sysname" name="sysname"/>
				
				接入方式:
				<select id="accesstype" name="accesstype" constantId="tblLookupValues" style="width:120px"></select>
				
				
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
							<td>系统代码:</td>
							<td><input type="text" id="syscode" name="syscode" style="width:120px"/></td>
						</tr>
						<tr>
							<td>系统名称:</td>
							<td><input type="text" id="sysname" name="sysname" style="width:120px"/></td>
						</tr>
						<tr>
							<td>系统说明:</td>
							<td><input type="text" id="sysdescription" name="sysdescription" style="width:120px"/></td>
						</tr>
						<tr>
							<td>接入方式:</td>
							<td><select id="accesstype" name="accesstype" constantId="tblLookupValues" style="width:120px"></select></td>
					
						</tr>
						<tr>
							<td>IP地址:</td>
							<td><input type="text" id="sysip" name="sysip" style="width:120px"/></td>
						</tr>
						<tr>
							<td>端口:</td>
							<td><input type="text" id="sysport" name="sysport" style="width:120px"/></td>
						</tr>
						<tr>
							<td>使用用户:</td>
							<td><input type="text" id="sysuser" name="sysuser" style="width:120px"/></td>
						</tr>
						<tr>
							<td>使用密码:</td>
							<td><input type="text" id="syspwd" name="syspwd" style="width:120px"/></td>
						</tr>
						
				</table>
			</form>
		</div>
	</div>
	
  </body>
</html>
