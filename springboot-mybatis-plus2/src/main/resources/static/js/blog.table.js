/**
 * <p> 描述 : bootstrap数据列表公共方法
 * @author : blackcat
 * @date  : 2020/1/28 10:40
*/
(function ($) {
    $.extend({
        tableUtil: {
            _option: {},
            init: function (options) {
                $.tableUtil._option = options;
                $('#tablelist').bootstrapTable({
                    url: options.url,
                    method: 'post',                      //请求方式（*）
                    toolbar: '#toolbar',                //工具按钮用哪个容器
                    striped: true,                      //是否显示行间隔色
                    cache: true,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                    contentType: "application/x-www-form-urlencoded", // 发送到服务器的数据编码类型, application/x-www-form-urlencoded为了实现post方式提交
                    sortable: false,                     //是否启用排序
                    sortOrder: "asc",                   //排序方式
                    sortStable: true,                   // 设置为 true 将获得稳定的排序
                    queryParams: $.tableUtil.queryParams,//传递参数（*）
                    queryParamsType: '',
                    pagination: true,                   //是否显示分页（*）
                    sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
                    pageNumber: 1,                       //初始化加载第一页，默认第一页
                    pageSize: 10,                       //每页的记录行数（*）
                    pageList: [10, 20, 30, 50, 100],        //可供选择的每页的行数（*）
                    //search: true,                       //是否启用搜索框 根据sidePagination选择从前后台搜索
                    //strictSearch: true,                 //设置为 true启用 全匹配搜索，否则为模糊搜索
                    //searchOnEnterKey: true,            // 设置为 true时，按回车触发搜索方法，否则自动触发搜索方法
                    minimumCountColumns: 1,             //最少允许的列数
                    // showColumns: true,                  //是否显示 内容列下拉框
                    showRefresh: true,                  //是否显示刷新按钮
                    // showToggle: true,                   //是否显示详细视图和列表视图的切换按钮
                    // detailView: true,                   //是否显示父子表
                    // showExport: true,                   //是否显示导出
                    // exportDataType: "basic",              //basic', 'all', 'selected'.
                    // clickToSelect: true,                //是否启用点击选中行
                    // singleSelect: true,
                    // height: 505,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
                    onEditableSave: function (field, row, oldValue, $el) {
                        if (options.updateUrl) {
                            $.ajax({
                                type: "post",
                                url: options.updateUrl,
                                data: {strJson: JSON.stringify(row)},
                                success: function (json) {
                                    if (json.status == 200) {
                                        $.tool.alert(json.message);
                                    } else {
                                        $.tool.alertError(json.message);
                                    }
                                },
                                error: function () {
                                    $.tool.alertError("网络超时！");
                                }
                            });
                        } else {
                            $.tool.alertError("无效的请求地址！");
                            return;
                        }
                    },
                    rowStyle: options.rowStyle || function (row, index){return {};},
                    columns: options.columns
                });
            },
            queryParams: function (params) {
                params = $.extend({}, params);
                return params;
            },
            refresh: function () {
                $("#tablelist").bootstrapTable('refresh', {url: $.tableUtil._option.url});
            }
        },
        buttonUtil: {
            init: function (options) {
                /* 添加 */
                $("#btn_add").click(function () {
                    resetForm();
                    $("#addOrUpdateModal").modal('show');
                    $("#addOrUpdateModal").find(".modal-dialog .modal-content .modal-header h4.modal-title").html("添加" + options.modalName);

                    if ($("#password") && $("#password")[0]) {
                        $("#password").attr("required", "required");
                    }
                    if ($("#username") && $("#username")[0]) {
                        $("#username").removeAttr("readonly");
                    }
                    bindSaveInfoEvent(options.createUrl);
                });

                /* 修改 */
                $('#tablelist').on('click', '.btn-update', function () {
                    var $this = $(this);
                    var userId = $this.attr("data-id");
                    $.ajax({
                        type: "post",
                        url: options.getInfoUrl.replace("{id}", userId),
                        success: function (json) {
                            var info = json.data;
                            // console.log(info);
                            resetForm(info);
                            $("#addOrUpdateModal").modal('show');
                            $("#addOrUpdateModal").find(".modal-dialog .modal-content .modal-header h4.modal-title").html("修改" + options.modalName);
                            if ($("#password") && $("#password")[0]) {
                                $("#password").removeAttr("required");
                            }
                            if ($("#username") && $("#username")[0]) {
                                $("#username").attr("readonly", "readonly");
                            }

                            bindSaveInfoEvent(options.updateUrl);

                        },
                        error: $.tool.ajaxError
                    });
                });

                /* 删除 */
                function remove(ids) {
                    $.tool.confirm("确定删除该" + options.modalName + "信息？", function () {
                        $.ajax({
                            type: "post",
                            url: options.removeUrl,
                            traditional: true,
                            data: {'ids': ids},
                            success: function (json) {
                                $.tool.ajaxSuccess(json);
                                $.tableUtil.refresh();
                            },
                            error: $.tool.ajaxError
                        });
                    }, function () {

                    }, 5000);
                }

                /* 批量删除用户 */
                $("#btn_delete_ids").click(function () {
                    var selectedId = getSelectedId();
                    if (!selectedId || selectedId == '[]' || selectedId.length == 0) {
                        $.tool.alertError("请至少选择一条记录");
                        return;
                    }
                    remove(selectedId);
                });

                /* 删除 */
                $('#tablelist').on('click', '.btn-remove', function () {
                    var $this = $(this);
                    var userId = $this.attr("data-id");
                    remove(userId);
                });
            }
        }
    });
})(jQuery);

/**
 * 绑定保存按钮事件
 */
function bindSaveInfoEvent(url) {
    $(".addOrUpdateBtn").unbind('click');
    $(".addOrUpdateBtn").click(function () {
        if (validator.checkAll($("#addOrUpdateForm"))) {
            $.ajax({
                type: "post",
                url: url,
                data: $("#addOrUpdateForm").serialize(),
                success: function (json) {
                    $.tool.ajaxSuccess(json);
                    $("#addOrUpdateModal").modal('hide');
                    $.tableUtil.refresh();
                },
                error: $.tool.ajaxError
            });
        }
    })
}

/**
 * 重置弹窗表单
 */
function resetForm(info) {
    $("#addOrUpdateModal form input,#addOrUpdateModal form select,#addOrUpdateModal form textarea").each(function () {
        var $this = $(this);
        clearText($this, this.type, info);
    });
}

/**
 * 情况表单信息
*/
function clearText($this, type, info){
    var $div = $this.parents(".item");
    if ($div.hasClass("bad")) {
        $div.removeClass("bad");
        $div.find("div.alert").remove();
    }
    if (info) {
        var thisName = $this.attr("name");
        var thisValue = info[thisName];
        if (type == 'radio') {
            $this.iCheck(((thisValue && 1 == $this.val()) || (!thisValue && 0 == $this.val())) ? 'check' : 'uncheck')
        } else if (type == 'checkbox') {
            $this.iCheck((thisValue || thisValue == 1) ? 'check' : 'uncheck');
        } else {
            if (thisValue && thisName != 'password') {
                $this.val(thisValue);
            }
        }
    } else {
        if (type === 'radio' || type === 'checkbox') {
            $this.iCheck('uncheck');
        }else{
            $this.val('');
        }
    }
}

/**
 * 获取选中的记录ID
 */
function getSelectedId() {
    var selectedJson = $("#tablelist").bootstrapTable('getAllSelections');
    var ids = [];
    $.each(selectedJson, function (i) {
        ids.push(selectedJson[i].id);
    });
    return ids;
}

/**
 * 获取选中的记录
 */
/*function getSelectedObj() {
    var selectedJson = $("#tablelist").bootstrapTable('getAllSelections');
    return selectedJson;
}*/
