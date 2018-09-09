
var prefix = "/common/service"
$(function() {
	load();
});
function selectLoad() {
	var html = "";
	$.ajax({
		url : '/common/service/type',
		success : function(data) {
			//加载数据
			for (var i = 0; i < data.length; i++) {
				html += '<option value="' + data[i].type + '">' + data[i].description + '</option>'
			}
			$(".chosen-select").append(html);
			$(".chosen-select").chosen({
				maxHeight : 200
			});
			//点击事件
			$('.chosen-select').on('change', function(e, params) {
				console.log(params.selected);
				var opt = {
					query : {
						type : params.selected,
					}
				}
				$('#exampleTable').bootstrapTable('refresh', opt);
			});
		}
	});
}
function load() {
	$('#exampleTable')
		.bootstrapTable(
			{
				method : 'get', // 服务器数据的请求方式 get or post
				url : prefix + "/list", // 服务器数据的加载地址
				//	showRefresh : true,
				//	showToggle : true,
				//	showColumns : true,
				iconSize : 'outline',
				toolbar : '#exampleToolbar',
				striped : true, // 设置为true会有隔行变色效果
				dataType : "json", // 服务器返回的数据类型
				pagination : true, // 设置为true会在底部显示分页条
				// queryParamsType : "limit",
				// //设置为limit则会发送符合RESTFull格式的参数
				singleSelect : false, // 设置为true将禁止多选
				// contentType : "application/x-www-form-urlencoded",
				// //发送到服务器的数据编码类型
				pageSize : 10, // 如果设置了分页，每页数据条数
				pageNumber : 1, // 如果设置了分布，首页页码
				//search : true, // 是否显示搜索框
				showColumns : false, // 是否显示内容下拉框（选择显示的列）
				sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
                leftFixedColumns: false,
                leftFixedNumber: 3,
                rightFixedColumns: true,
                rightFixedNumber: 1,
				queryParams : function(params) {
					return {
						//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
						limit : params.limit,
						offset : params.offset,
                        serviceId:$('#serviceId').val()
					};
				},
				// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
				// queryParamsType = 'limit' ,返回参数必须包含
				// limit, offset, search, sort, order 否则, 需要包含:
				// pageSize, pageNumber, searchText, sortName,
				// sortOrder.
				// 返回false将会终止请求
				columns : [
					{
						checkbox : true
					},
					{
						field : 'id',
						title : '编号',
                        titleTooltip:'编号'
					},
					{
						field : 'serviceId',
						title : 'serviceId',
                        class:'minWidth100',
                        titleTooltip:'serviceId'
					},
					{
						field : 'domain',
						title : 'domain',
                        class:'minWidth100',
                        titleTooltip:'domain'
					},
					{
						field : 'serviceModel',
						title : '服务类型',
                        class:'minWidth100',
                        titleTooltip:'服务类型'
					},
					{
                        field : 'serviceDesc',
                        title : '描述',
                        class:'minWidth150',
                        titleTooltip:'描述'
                    },
                    {
                        field : 'actionType',
                        title : '权限码',
                        class:'minWidth100',
                        titleTooltip:'权限码'
                    },
                    {
                        field : 'permission',
                        title : '是否登录',
                        class:'minWidth100',
                        titleTooltip:'是否登录'
                    },
                    {
                        field : 'serviceLog',
                        title : '是否记录日志',
                        class:'minWidth100',
                        titleTooltip:'是否记录日志'
                    },
                    {
                        field : 'status',
                        title : '发布状态',
                        align : 'center',
                        formatter : function(value, row, index) {
                            if (value == '0') {
                                return '<span class="label label-danger">未发布</span>';
                            } else if (value == '1') {
                                return '<span class="label label-primary">已发布</span>';
                            }
                        },
                        class:'minWidth100',
                        titleTooltip:'发布状态'
                    },
                    {
                        field : 'path',
                        title : 'HTTP PATH',
                        class:'minWidth150',
                        titleTooltip:'请求PATH'
                    },
                    {
                        field : 'param',
                        title : 'HTTP 参数校验',
                        class:'minWidth100',
                        titleTooltip:'HTTP参数校验'
                    },
                    {
                        field : 'mode',
                        title : 'HTTP 请求方式',
                        class:'minWidth100',
                        titleTooltip:'HTTP请求方式'
                    },
                    {
                        field : 'argument',
                        title : 'RPC 参数类型',
                        class:'minWidth100',
                        titleTooltip:'调用目标服务的参数类型'
                    },
                    {
                        field : 'interfaze',
                        title : 'RPC 接口类名',
                        class:'minWidth100',
                        titleTooltip:'目标地址的接口类名称'
                    },
                    {
                        field : 'method',
                        title : 'RPC 接口方法名',
                        class:'minWidth100',
                        titleTooltip:'目标服务的接口方法名'
                    },
                    {
                        field : 'replenishRate',
                        title : '限流速率(s)',
                        class:'minWidth100',
                        titleTooltip:'限流:令牌桶填充平均速率，单位:秒'
                    },
                    {
                        field : 'burstCapacity',
                        title : '限流上限',
                        class:'minWidth100',
                        titleTooltip:'限流:令牌桶上限'
                    },
                    {
                        field : 'circuitBreakerEnabled',
                        title : '熔断开关',
                        class:'minWidth100',
                        titleTooltip:'熔断开关'
                    },
                    {
                        field : 'metricsRollingStatisticalWindowInMilliseconds',
                        title : '熔断时间窗',
                        class:'minWidth100',
                        titleTooltip:'熔断:统计滚动的时间窗口'
                    },
                    {
                        field : 'circuitBreakerSleepWindowInMilliseconds',
                        title : '熔断半开状态',
                        class:'minWidth100',
                        titleTooltip:'熔断时间窗口，当熔断器打开5s后进入半开状态，允许部分流量进来重试'
                    },
                    {
                        field : 'circuitBreakerRequestVolumeThreshold',
                        title : '熔断阀值',
                        class:'minWidth100',
                        titleTooltip:'熔断器在整个统计时间内是否开启的阀值'
                    },
                    {
                        field : 'circuitBreakerErrorThresholdPercentage',
                        title : '熔断错误率',
                        class:'minWidth100',
                        titleTooltip:'熔断打开最大错误率'
                    },
                    {
                        field : 'executionIsolationSemaphoreMaxConcurrentRequests',
                        title : '最大并发数',
                        class:'minWidth100',
                        titleTooltip:'最大并发数'
                    },
                    {
                        field : 'fallbackIsolationSemaphoreMaxConcurrentRequests',
                        title : '失败回滚',
                        class:'minWidth100',
                        titleTooltip:'失败回滚调用最大并发数'
                    },
                    {
                        field : 'createBy',
                        title : '创建人',
                        class:'minWidth100',
                    },
                    {
                        field : 'updateBy',
                        title : '修改人',
                        class:'minWidth100',
                    },
					{
						field : 'createTime',
						title : '创建时间',
                        class:'minWidth150',
					},
					{
						field : 'updateTime',
						title : '修改时间',
                        class:'minWidth150',
					},
					{
						title : '操作',
						field : 'id',
                        class:'minWidth200',
                        width : '200px',
						align : 'center',
						formatter : function(value, row, index) {
							var e = '<a class="btn btn-primary btn-sm ' + s_edit_h + '" href="#" mce_href="#" title="编辑" onclick="edit(\''
								+ row.id
								+ '\')"><i class="fa fa-edit"></i></a> ';
							var d = '<a class="btn btn-warning btn-sm ' + s_remove_h + '" href="#" title="删除"  mce_href="#" onclick="remove(\''
								+ row.id
								+ '\')"><i class="fa fa-remove"></i></a> ';
							var f = '<a class="btn btn-success btn-sm ' + s_add_h + '" href="#" title="增加"  mce_href="#" onclick="add()"><i class="fa fa-plus"></i></a> ';
                            var v = '<a class="btn btn-info btn-sm ' + s_add_h + '" href="#" title="查询"  mce_href="#" onclick="find(\''
                               + row.serviceId
                               + '\')"><i class="fa fa-search"></i></a> ';
							return e + d + f + v;
						}
					} ]
			});
}
function reLoad() {
    $('#exampleTable').bootstrapTable('refresh');
}
function add() {
	layer.open({
		type : 2,
		title : '增加',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/add' // iframe的url
	});
}
function edit(id) {
	layer.open({
		type : 2,
		title : '编辑',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/edit/' + id // iframe的url
	});
}


function find(serviceId) {
    layer.open({
        type: 2,
        title: '查看',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        content: prefix + '/find/' + serviceId // iframe的url
    });
}


function remove(id) {
	layer.confirm('确定要删除选中的记录？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix + "/remove",
			type : "post",
			data : {
				'id' : id
			},
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
					reLoad();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	})
}

function addD(type,description) {
	layer.open({
		type : 2,
		title : '增加',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/add/'+type+'/'+description // iframe的url
	});
}
function batchRemove() {
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length == 0) {
		layer.msg("请选择要删除的数据");
		return;
	}
	layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		var ids = new Array();
		// 遍历所有选择的行数据，取每条数据对应的ID
		$.each(rows, function(i, row) {
			ids[i] = row['id'];
		});
		$.ajax({
			type : 'POST',
			data : {
				"ids" : ids
			},
			url : prefix + '/batchRemove',
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
					reLoad();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	}, function() {});
}

function publish() {
    var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
    if (rows.length == 0) {
        layer.msg("请选择要发布的数据");
        return;
    }
    layer.confirm("确认要发布选中的'" + rows.length + "'条数据吗?", {
        btn : [ '确定', '取消' ]
        // 按钮
    }, function() {
        var ids = new Array();
        // 遍历所有选择的行数据，取每条数据对应的ID
        $.each(rows, function(i, row) {
            ids[i] = row['id'];
        });
        $.ajax({
            type : 'POST',
            data : {
                "ids" : ids
            },
            url : prefix + '/publish',
            success : function(r) {
                if (r.code == 0) {
                    layer.msg(r.msg);
                    reLoad();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    }, function() {});
}