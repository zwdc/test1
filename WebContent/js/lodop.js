
var LODOP;
var tableString;

//列表打印预览方法
function print_view(test_datagrid){
	if(!CheckIsInstall()){
		return;
	}
	tableString="<table>";
	LODOP=getLodop();
	LODOP.SET_PRINT_MODE("FULL_WIDTH_FOR_OVERFLOW",true);
	LODOP.SET_PRINT_MODE("FULL_HEIGHT_FOR_OVERFLOW",true);
	createPrintPage(test_datagrid);
	LODOP.PREVIEW();
}

//将datagrid数据放到打印预览里
function createPrintPage(test_datagrid){
	var frozenColumns = test_datagrid.datagrid("options").frozenColumns;  // 得到frozenColumns对象

    var columns = test_datagrid.datagrid("options").columns;    // 得到columns对象

 
    
    // 载入title

    tableString = tableString + "\n<tr>";

    if(frozenColumns != undefined && frozenColumns != '') {
       for(var i = 0;i<frozenColumns[0].length; i++) {

           if(frozenColumns[0][i].hidden != true) {

              tableString = tableString + "\n<th width= auto>" + frozenColumns[0][i].title + "</th>";

           }

       }

    }

    if(columns != undefined && columns != '') {
       for(var i = 0;i<columns[0].length; i++) {

           if(columns[0][i].hidden != true) {

              tableString = tableString + "\n<th width= auto>" + columns[0][i].title + "</th>";

           }

       }

    }

    tableString = tableString + "\n</tr>";

 

    // 载入内容

    var rows = test_datagrid.datagrid("getRows"); // 这段代码是获取当前页的所有行。

    for(var j = 0; j <rows.length;j++) {

       tableString = tableString + "\n<tr>";

       if(frozenColumns != undefined && frozenColumns != '') {

           for(var i = 0;i<frozenColumns[0].length; i++) {

              if(frozenColumns[0][i].hidden != true) {
            	  if(Object.prototype.toString.call(rows[j][frozenColumns[0][i].field]) === '[object Date]'){
            		  tableString = tableString + "\n<td >" + new Date().Format("yyyy-MM-dd HH:mm:ss") + "</td>";
            	  }else{
            		  tableString = tableString + "\n<td >" + rows[j][frozenColumns[0][i].field] + "</td>";
            	  }
              }

           }

       }

       if(columns != undefined && columns != '') {

           for(var i = 0;i<columns[0].length; i++) {

              if(columns[0][i].hidden != true) {
                  tableString = tableString + "\n<td >" + rows[j][columns[0][i].field] + "</td>";
              }

           }

       }

       tableString = tableString + "\n</tr>";

    }

    tableString = tableString + "\n</table>";
    LODOP=getLodop();  
	LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_表单二");
	var strBodyStyle="<style>table,td { border: 1 solid #000000;border-collapse:collapse };table,th { border: 1 solid #000000;border-collapse:collapse }</style>";
	var strFormHtml=strBodyStyle+"<body>"+tableString+"</body>";
	LODOP.ADD_PRINT_TABLE(100,100,1200,1000,strFormHtml);
}

//将详细页面数据
function createPrintPagetwo(id){
	refreshData();
    LODOP=getLodop();  
	LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_表单二");
	LODOP.SET_PRINT_MODE("FULL_WIDTH_FOR_OVERFLOW",true);
	LODOP.SET_PRINT_MODE("FULL_HEIGHT_FOR_OVERFLOW",true);
	var css="<style>table,td { border: 1 solid #000000;border-collapse:collapse };table,th { border: 1 solid #000000;border-collapse:collapse }</style>";
	LODOP.ADD_PRINT_HTM(88,40,1000,500,css+document.getElementById(id).innerHTML);
}
//用于将获取详情页面的单选按钮值
function refreshData(){ //让innerHTML获取的内容包含input和select(option)的最新值
	var allInputObject=document.body.getElementsByTagName("input");
	for (i = 0; i < allInputObject.length; i++) {
		if(allInputObject[i].type=="radio")  {
	        	if (allInputObject[i].checked ) 
            	   	allInputObject[i].setAttribute("checked","checked"); 
            	   	else
			        allInputObject[i].removeAttribute("checked");
    	}else allInputObject[i].setAttribute("value",allInputObject[i].value);
	};
	for (i = 0; i < document.getElementsByTagName("select").length; i++) {
	    var sl=document.getElementsByTagName("select")[i];
	    for (j = 0; j < sl.options.length; j++) {
		    if (sl.options[j].selected) 
	                 	sl.options[j].setAttribute("selected","selected");
			else sl.options[j]=new Option(sl.options[j].text,sl.options[j].value);
	    };
	};
	for (i = 0; i < document.getElementsByTagName("textarea").length; i++) {
	    var sl=document.getElementsByTagName("textarea")[i];
	    if(sl.value!=''&&sl.value!=null&&sl.value!=undefined){
	    	if(document.getElementById("textarea")){
	    		document.getElementById("textarea").innerHTML='备注：'+sl.value;
	    	}
	    }
	};
};   

//判断本机是否装打印插件
function CheckIsInstall() {	 
	try{
	 var LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM')); 
		if ((LODOP!=null)&&(typeof(LODOP.VERSION)!="undefined")){
			return true;
		}else{
			alert("本机未安装!是否下载安装？");
			window.location.href=ctx+"/lodop/install_lodop32.exe";
			return false;
		}
	}catch(err){
		return false;
	}
		
}