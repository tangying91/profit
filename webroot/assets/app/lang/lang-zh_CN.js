/**
 * Created by TangYing
 */
var LANG = {
    APP: {
        name: '<font size ="20" color="green">智能股票数据分析后台</font>'
    },

    BTN: {
        login: '登陆',
        logout: '注销',
        add: '新增',
        edit: '编辑',
        save: '保存',
        del: '删除',
        refresh: '刷新',
        search: '搜索',
        cancel: '取消',
        reset: '重置',
        confirm: '确定'
    },

    TITLE: {
        confirm: '确认',
        tip: '提示',
        warn: '警告',
        success: '操作成功',
        failure: '操作失败',
        login: '欢迎登录',
        logout: '退出登录',
        menu: '功能菜单',
        manage: '管理',
        add: '增加',
        edit: '编辑',
        delete: '删除',
        home_page: '主页'
    },

    LABEL: {
        edit: '编辑',
        delete: '删除',
        operate: '操作',
        account: '账号',
        password: '密码',
        name: '姓名',
        desc: '描述',
        type: '类型',
        id: 'ID'
    },

    EMPTY_TIPS: {
        empty_account: '请输入账号',
        empty_password: '请输入密码',
        empty_combo: '请选择'
    },

    MESSAGE: {
        delete: '你确定要删除该条数据吗？',
        failure_connect: '系统连接错误！请刷新页面重新载入，如果还不能解决，请联系技术人员',
        empty_record_chose: '<font color="red">请先选择您要删除的行！<font>',
        failure_load: '<font color="red">数据加载失败！<font>'
    }
}

/**
 * 功能：为全局提供Render的方法。
 *
 * @author TangYing
 */
RenderUtil = function () {

    return {
        /**
         * 功能：render Date 类型的数据。 使用方式：在
         * ColumnModel中需要显示Date数据的地方：renderer:renderDate('Y-m-d').
         *
         * @param format：如"Y-m-d"
         */
        date: function (format) {
            return function (v) {
                var JsonDateValue;
                if (Ext.isEmpty(v))
                    return '';
                else if (Ext.isEmpty(v.time))
                    JsonDateValue = new Date(v);
                else
                    JsonDateValue = new Date(v.time);
                return JsonDateValue.format(format || 'Y-m-d H:i:s');
            };
        },
        btnAnchor: function (value) {
            return "<a href='" + value + "' onclick='return true' target='_blank'><font color='#808080'/>" + value + "</a>";
        },
        btnEdit: function () {
            return "<a href='' onclick='return false'><font color='blue'/>[编辑]</a>";
        },
        btnDel: function () {
            return "<a href='' onclick='return false' ><font color='red'/>[删除]</a>";
        },

        /**
         * Custom function used for column renderer
         * @param {Object} val
         */
        change: function(val) {
            if (val < 0) {
                return '<span style="color:green;">' + val + '</span>';
            } else if (val > 0) {
                return '<span style="color:red;">' + val + '</span>';
            }
            return val;
        },

        /**
         * Custom function used for column renderer
         * @param {Object} val
         */
        percent: function(val) {
            if (val < 0) {
                return '<span style="color:green;">' + Math.round(val * 100) + '%' + '</span>';
            } else if (val > 0) {
                return '<span style="color:red;">' + Math.round(val * 100) + '%' + '</span>';
            }
            return val;
        }
    };
}();

