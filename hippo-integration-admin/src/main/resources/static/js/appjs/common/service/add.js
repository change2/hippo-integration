$().ready(function() {
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		save();
	}
});
function save() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/common/service/save",
		data : $('#signupForm').serialize(), // 你的formid
		async : false,
		error : function(request) {
			parent.layer.alert("网络超时");
		},
		success : function(data) {
			if (data.code == 0) {
				parent.layer.msg("操作成功");
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name);
				parent.layer.close(index);

			} else {
				parent.layer.alert(data.msg)
			}

		}
	});

}
function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
            serviceId : {
				required : true,
                minlength : 8,
                maxlength : 8,
                remote : {
                    url : "/common/service/exit", // 后台处理程序
                    type : "post", // 数据发送方式
                    dataType : "json", // 接受数据格式
                    data : { // 要传递的数据
                        serviceId : function() {
                            return $("#serviceId").val();
                        }
                    }
                }
			}
		},
		messages : {
            serviceId : {
				required : icon + "请输入SERVICE ID",
                minlength : icon + "SERVICE ID 必须为8位字符",
                minlength : icon + "SERVICE ID 必须为8位字符",
                remote : icon + "SERVICE ID 已经存在"
			}
		}
	})
}