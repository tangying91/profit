/**
 * Created by TangYing.</br>
 * All variable
 */
// 申明一些全局变量，带默认值
var GLOBAL = {
    // 综
    DEFAULT_TYPE: 10,
    DEFAULT_RISE_DAY: 5,
    DEFAULT_VOLUME_RATE: 1.5,
    DEFAULT_GAINS_RANGE: '0,30',
    DEFAULT_FUND_RANGE: '-100,-100,-100,-100,-200',

    // 积
    RISE_TYPE: 10,
    RISE_RISE_DAY: 8,
    RISE_VOLUME_RATE: 0,
    RISE_GAINS_RANGE: '-3,5',
    RISE_FUND_RANGE: '-1000,-1000,-1000,-1000,-200',

    // 突
    BREAK_TYPE: 5,
    BREAK_RISE_DAY: 1,
    BREAK_VOLUME_RATE: 3,
    BREAK_GAINS_RANGE: '-3,10',
    BREAK_FUND_RANGE: '-10000,-10000,-10000,-10000,-2000',

    // 金
    FUND_TYPE: 5,
    FUND_RISE_DAY: 2,
    FUND_VOLUME_RATE: 0,
    FUND_GAINS_RANGE: '-5,15',
    FUND_FUND_RANGE: '0,0,0,0,0',

    // 底
    BOTTOM_TYPE: 30,
    BOTTOM_RISE_DAY: 10,
    BOTTOM_VOLUME_RATE: 0,
    BOTTOM_GAINS_RANGE: '-15,15',
    BOTTOM_FUND_RANGE: '-10000,-10000,-10000,-10000,-2000'
};

var defaultDate = new Date().setDate(new Date().getDate() - 1);

