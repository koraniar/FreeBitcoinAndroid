package com.koraniar.freebitcoin;

import com.koraniar.freebitcoin.Enums.RequestType;

/**
 * Created by Esteban on 6/15/2017.
 */

public final class JavaScript {

    //Global
    public static final String GlobalTestJSInterface = "javascript:(function(){android.showToast('hello moto');})()";
    public static final String GlobalTestLogin = "javascript:(function(){android.getPageInfo(" + RequestType.LoginTest + ",$('#homepage_login_button').length);})()";
    public static final String GoToHome = "javascript:(function(){$('.free_play_link').click();})()";
    public static final String GoToMultiply = "javascript:(function(){$('.double_your_btc_link').click();})()";
    public static final String GoToLottery = "javascript:(function(){$('.lottery_link').click();})()";
    public static final String GoToRefer = "javascript:(function(){$('.refer_link').click();})()";
    public static final String GoToProfile = "javascript:(function(){$('.edit_link').click();})()";
    public static final String GolobalHdeMenu = "javascript:(function(){$('.edit_link').click();})()";

    //Free BTC
    public static final String FreeTestSound = "javascript:(function(){document.getElementById('test_sound').click();})()";
    public static final String FreeGetCountDown = "javascript:(function(){" +
                "android.getPageInfo(" + RequestType.GetCountDown + ",$('#time_remaining').find('.countdown_section').find('.countdown_amount').first().text());" +
            "})()";
    public static final String FreeAddListenerToRollButton = "javascript:(function(){$('#free_play_form_button').click(function() {" +
                "android.getPageInfo(" + RequestType.RollButtonPressed + ",'OK')" +
            "});})()";
}
