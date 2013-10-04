/*******************************************************************************
 *
 *    Copyright (c) Swadhin Pradhan
 *
 *    History_Logger
 *
 *    AboutActivity
 *    TODO File description or class description.
 *
 *    @author: swadhin
 *    @since:  2012-5-24
 *    @version: 1.0
 *
 ******************************************************************************/
package com.dolphin.browser.extension.history;

import android.content.Intent;

/**
 * AboutActivity of SampleAddon.
 * @author swadhin
 *
 */
public class AboutActivity extends com.dolphin.browser.addons.AboutActivity {

    @Override
    protected void onStartCustomAboutActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

}
