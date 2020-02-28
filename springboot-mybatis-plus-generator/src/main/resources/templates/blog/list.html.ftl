

<div class="clearfix"></div>
<div class="row">
    <div class="col-md-12 col-sm-12 col-xs-12">
        <ol class="breadcrumb">
            <li><a href="/admin">首页</a></li>
            <li class="active">${table.comment!}管理</li>
        </ol>
        <div class="x_panel">
            <div class="x_content">
                <div class="<#--table-responsive-->">
                    <div class="btn-group hidden-xs" id="toolbar">
                            <button id="btn_add" type="button" class="btn btn-default" title="新增用户">
                                <i class="fa fa-plus"></i> 新增${table.comment!}
                            </button>
                            <button id="btn_delete_ids" type="button" class="btn btn-default" title="删除选中">
                                <i class="fa fa-trash-o"></i> 批量删除
                            </button>
                    </div>
                    <table id="dataTable">
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>


<!--添加${table.comment!}-->
<div class="modal fade" id="addOrUpdateModal" tabindex="-1" role="dialog" aria-labelledby="addroleLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="addroleLabel"></h4>
            </div>
            <div class="modal-body">
                <form id="addOrUpdateForm" class="form-horizontal form-label-left" novalidate>
                    <input type="hidden" name="id">
                    <#list table.fields as field>
                        <div class="item form-group">
                            <label class="control-label col-md-3 col-sm-3 col-xs-12" for="${field.propertyName}">${field.comment}: <span class="required">*</span></label>
                            <div class="col-md-6 col-sm-6 col-xs-12">
                                <input type="text" class="form-control col-md-7 col-xs-12" name="${field.propertyName}" id="${field.propertyName}" required="required" placeholder="请输入${field.comment}"/>
                            </div>
                        </div>
                    </#list>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary addOrUpdateBtn">保存</button>
            </div>
        </div>
    </div>
</div>
<!--/添加用户弹框-->
<script>
    $(function () {
        // 列表属性
        var options = {
            url: "/${table.name}/list",
            getInfoUrl: "/${table.name}/get/{id}",
            updateUrl: "/${table.name}/edit",
            removeUrl: "/${table.name}/remove",
            createUrl: "/${table.name}/add",
            columns: [
                {
                    checkbox: true
        <#list table.fields as field>
                }, {
                    field: '${field.propertyName}',
                    title: '${field.comment}'
        </#list>
                }, {
                    field: 'operate',
                    title: '操作',
                    formatter: operateFormatter //自定义方法，添加操作按钮
                }],
            modalName: "${table.comment!}"
        };
        //1.初始化Table
        $.tableUtil.init(options);
        //2.初始化Button的点击事件
        $.buttonUtil.init(options);

    });



    /**
     * 操作按钮
     */
    function operateFormatter(code, row, index) {
        var trUserId = row.id;
        var operateBtn = [
            '<a class="btn btn-xs btn-primary btn-update" data-id="' + trUserId + '"><i class="fa fa-edit"></i>编辑</a>',
            '<a class="btn btn-xs btn-danger btn-remove" data-id="' + trUserId + '"><i class="fa fa-trash-o"></i>删除</a>'
        ];

        return operateBtn.join('');
    }

</script>


