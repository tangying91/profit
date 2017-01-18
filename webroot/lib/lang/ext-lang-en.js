Ext.onReady(function() {
    var cm = Ext.ClassManager,
        exists = Ext.Function.bind(cm.get, cm),
        parseCodes;

    if (Ext.Updater) {
        Ext.Updater.defaults.indicatorText = '<div class="loading-indicator">Loading...</div>';
    }

    Ext.define("Ext.locale.zh_CN.view.View", {
        override: "Ext.view.View",
        emptyText: ""
    });

    Ext.define("Ext.locale.zh_CN.grid.plugin.DragDrop", {
        override: "Ext.grid.plugin.DragDrop",
        dragText: "choose {0} row"
    });

    Ext.define("Ext.locale.zh_CN.TabPanelItem", {
        override: "Ext.TabPanelItem",
        closeText: "close the label"
    });

    Ext.define("Ext.locale.zh_CN.form.field.Base", {
        override: "Ext.form.field.Base",
        invalidText: "illegal input value"
    });

    // changing the msg text below will affect the LoadMask
    Ext.define("Ext.locale.zh_CN.view.AbstractView", {
        override: "Ext.view.AbstractView",
        msg: "reading..."
    });

    if (Ext.Date) {
        Ext.Date.monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

        Ext.Date.dayNames = ["Sun", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat"];

        Ext.Date.formatCodes.a = "(this.getHours() < 12 ? 'AM' : 'PM')";
        Ext.Date.formatCodes.A = "(this.getHours() < 12 ? 'AM' : 'PM')";

        parseCodes = {
            g: 1,
            c: "if (/(AM)/i.test(results[{0}])) {\n"
                + "if (!h || h == 12) { h = 0; }\n"
                + "} else { if (!h || h < 12) { h = (h || 0) + 12; }}",
            s: "(AM|PM)",
            calcAtEnd: true
        };

        Ext.Date.parseCodes.a = Ext.Date.parseCodes.A = parseCodes;
    }

    if (Ext.MessageBox) {
        Ext.MessageBox.buttonText = {
            ok: "confirm",
            cancel: "cancel",
            yes: "yes",
            no: "no"
        };
    }

    if (exists('Ext.util.Format')) {
        Ext.apply(Ext.util.Format, {
            thousandSeparator: ',',
            decimalSeparator: '.',
            currencySign: '\u00a5',
            // Chinese Yuan
            dateFormat: 'yYmMdD'
        });
    }

    Ext.define("Ext.locale.zh_CN.picker.Date", {
        override: "Ext.picker.Date",
        todayText: "Today",
        minText: "Date less than min",
        //update
        maxText: "Date greater than max",
        //update
        disabledDaysText: "",
        disabledDatesText: "",
        monthNames: Ext.Date.monthNames,
        dayNames: Ext.Date.dayNames,
        nextText: 'Next Month (Ctrl+Right)',
        prevText: 'Prev Month (Ctrl+Left)',
        monthYearText: 'Choose month (Control+Up/Down change year)',
        //update
        todayTip: "{0} (space choose)",
        format: "yYmMdD",
        ariaTitle: '{0}',
        ariaTitleDateFormat: 'Y\u5e74m\u6708d\u65e5',
        longDayFormat: 'Y\u5e74m\u6708d\u65e5',
        monthYearFormat: 'Y\u5e74m\u6708',
        getDayInitial: function (value) {
            // Grab the last character
            return value.substr(value.length - 1);
        }
    });

    Ext.define("Ext.locale.zh_CN.picker.Month", {
        override: "Ext.picker.Month",
        okText: "confirm",
        cancelText: "cancel"
    });

    Ext.define("Ext.locale.zh_CN.toolbar.Paging", {
        override: "Ext.PagingToolbar",
        beforePageText: "The",
        //update
        afterPageText: "Page,Total {0} Page",
        //update
        firstText: "First",
        prevText: "Prev",
        //update
        nextText: "Next",
        lastText: "End",
        refreshText: "refresh",
        displayMsg: "Show {0} - {1} line, Total {2} line",
        //update
        emptyMsg: 'Empty data'
    });

    Ext.define("Ext.locale.zh_CN.form.field.Text", {
        override: "Ext.form.field.Text",
        minLengthText: "Min input length {0}",
        maxLengthText: "Max input length {0}",
        blankText: "Must input value",
        regexText: "",
        emptyText: null
    });

    Ext.define("Ext.locale.zh_CN.form.field.Number", {
        override: "Ext.form.field.Number",
        minText: "Min input value {0}",
        maxText: "Max input value {0}",
        nanText: "{0} invalid value"
    });

    Ext.define("Ext.locale.zh_CN.form.field.Date", {
        override: "Ext.form.field.Date",
        disabledDaysText: "forbidden",
        disabledDatesText: "forbidden",
        minText: "min value",
        maxText: "max value",
        invalidText: "{0} invalid format： {1}",
        format: "y-m-d"
    });

    Ext.define("Ext.locale.zh_CN.form.field.ComboBox", {
        override: "Ext.form.field.ComboBox",
        valueNotFoundText: undefined
    }, function() {
        Ext.apply(Ext.form.field.ComboBox.prototype.defaultListConfig, {
            loadingText: "Loading..."
        });
    });

    if (exists('Ext.form.field.VTypes')) {
        Ext.apply(Ext.form.field.VTypes, {
            emailText: '该输入项必须是电子邮件地址，格式如： "user@example.com"',
            urlText: '该输入项必须是URL地址，格式如： "http:/' + '/www.example.com"',
            alphaText: '该输入项只能包含半角字母和_',
            //update
            alphanumText: '该输入项只能包含半角字母,数字和_' //update
        });
    }
    //add HTMLEditor's tips by andy_ghg
    Ext.define("Ext.locale.zh_CN.form.field.HtmlEditor", {
        override: "Ext.form.field.HtmlEditor",
        createLinkText: '添加超级链接:'
    }, function() {
        Ext.apply(Ext.form.field.HtmlEditor.prototype, {
            buttonTips: {
                bold: {
                    title: '粗体 (Ctrl+B)',
                    text: '将选中的文字设置为粗体',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                italic: {
                    title: '斜体 (Ctrl+I)',
                    text: '将选中的文字设置为斜体',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                underline: {
                    title: '下划线 (Ctrl+U)',
                    text: '给所选文字加下划线',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                increasefontsize: {
                    title: '增大字体',
                    text: '增大字号',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                decreasefontsize: {
                    title: '缩小字体',
                    text: '减小字号',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                backcolor: {
                    title: '以不同颜色突出显示文本',
                    text: '使文字看上去像是用荧光笔做了标记一样',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                forecolor: {
                    title: '字体颜色',
                    text: '更改字体颜色',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                justifyleft: {
                    title: '左对齐',
                    text: '将文字左对齐',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                justifycenter: {
                    title: '居中',
                    text: '将文字居中对齐',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                justifyright: {
                    title: '右对齐',
                    text: '将文字右对齐',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                insertunorderedlist: {
                    title: '项目符号',
                    text: '开始创建项目符号列表',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                insertorderedlist: {
                    title: '编号',
                    text: '开始创建编号列表',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                createlink: {
                    title: '转成超级链接',
                    text: '将所选文本转换成超级链接',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                sourceedit: {
                    title: '代码视图',
                    text: '以代码的形式展现文本',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                }
            }
        });
    });

    Ext.define("Ext.locale.zh_CN.grid.header.Container", {
        override: "Ext.grid.header.Container",
        sortAscText: "ASC",
        //update
        sortDescText: "DESC",
        //update
        lockText: "lock",
        //update
        unlockText: "Unlock",
        //update
        columnsText: "row"
    });

    Ext.define("Ext.locale.zh_CN.grid.PropertyColumnModel", {
        override: "Ext.grid.PropertyColumnModel",
        nameText: "name",
        valueText: "value",
        dateFormat: "y-m-d"
    });
});
