/*******************************************************************************
 *
 *    Copyright (c) swadhin pradhan
 *
 *    History_Logger
 *
 *    MyAddonService
 *    TODO File description or class description.
 *
 *    @author: swadhin
 *    @since:  2013-3-13
 *    @version: 1.0
 *
 ******************************************************************************/
package com.dolphin.browser.extension.history;

import com.dolphin.browser.addons.AddonService;
//import com.dolphin.browser.addons.AlertDialogBuilder;
import com.dolphin.browser.addons.Browser;
import com.dolphin.browser.addons.HistoryInfo;
import com.dolphin.browser.addons.IContentObserver;
//import com.dolphin.browser.addons.ITab;
//import com.dolphin.browser.addons.IWebView;
import com.dolphin.browser.addons.OnClickListener;
//import android.content.SharedPreferences;
//import com.dolphin.browser.addons.TitleBarAction;
import android.graphics.BitmapFactory;
import android.os.Environment;
//import android.os.Message;
import android.os.RemoteException;
//import android.preference.PreferenceManager;
//import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * MyAddonService of Historyworld.
 * @author 
 *
 */
public class MyAddonService extends AddonService {

	public static final String LOG_TAG = "SensoSaur_Debug_Info ";
	private File fileDir = null;
	private PrintWriter  lmFout = null;
	
    @Override
    protected void onBrowserConnected(Browser browser) {
        try {
            //Show add-on bar action
            browser.addonBarAction.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
            browser.addonBarAction.setTitle("World_of_Web_History");
            browser.addonBarAction.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(Browser browser) {
                    try {
                        //String url = getCurrentUrl(browser);
                        fileDir = fileLocation();
                        
                        if( null != fileDir)
						{
							boolean result = fileDir.mkdir();
							Log.d(LOG_TAG,"mkdir result = " + result);
							if( false == result )
							{
								result = fileDir.mkdirs();
							}
							Log.d(LOG_TAG,"mkdir result = " + result);
							if (!fileDir.getParentFile().exists() && !fileDir.getParentFile().mkdirs()){
								  Log.d(LOG_TAG,"Unable to create " + fileDir.getParentFile());
								}
							//Toast.makeText(getApplicationContext(), "Logging started @ "+fileLocation(), Toast.LENGTH_LONG).show();
						
						}
                        
                        File lmFile = new File(fileDir,"history_mobile_web.txt");
				    	//if file does not exist, then create it			
						if(!lmFile.exists()){
							try {
									lmFile.createNewFile();
								} catch (IOException e) {
								   // TODO Auto-generated catch block
									e.printStackTrace();
								}
						}
						
						try {
								lmFout = new PrintWriter(new FileWriter(lmFile));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
						}
						
						if ( null != lmFout )
				    	{
							lmFout.println("URL URL_ID Page_Title Last_Visit_Time No_of_Visits");
							 List<HistoryInfo> historyInfos = browser.history.searchHistories(null,null); // get all history data
						        if (historyInfos != null && historyInfos.size() > 0) {
						        	for (int i=0; i< historyInfos.size(); i++){
						        		lmFout.println(historyInfos.get(i).url + " " + historyInfos.get(i).id + " " + historyInfos.get(i).title + " " + historyInfos.get(i).date + " " + historyInfos.get(i).visits);
						        	}
						        }
							
				    	}
						
                        /*if (TextUtils.isEmpty(url)) {
                            showDialog(browser, "Url of current page is empty");
                        } else {
                            int visits = getUrlVisits(browser, url);
                            showDialog(browser, "You have visited current page " + visits + " times.");
                        }*/
                        
						if ( null != lmFout )
	            		{
	            			lmFout.close();
	            		}
						
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            browser.addonBarAction.show();

            //Listen title bar url change event
            //browser.titleBarAction.addListener(mTitleBarActionListener);

            //listen history change event
            browser.history.registerHistoryContentObserver(mHistoryContentObserver);

            //updateBadgeNumber(browser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private String getCurrentUrl(Browser browser) throws RemoteException {
        //Get the url of current web page
        ITab tab = browser.tabs.getCurrent();
        if (null == tab) {
            return null;
        }
        IWebView webView = tab.getWebView();
        if (null == webView) {
            return null;
        }
        return webView.getUrl();
    }

   private int getUrlVisits(Browser browser, String url) throws RemoteException {
        //Get visits of url from history
        List<HistoryInfo> historyInfos = browser.history.searchHistories(HistoryInfo.KEY_URL + "=?", new String[]{url});
        if (historyInfos != null && historyInfos.size() > 0) {
            return historyInfos.get(0).visits;
        }
        return 0;
    }*/


    private IContentObserver mHistoryContentObserver = new IContentObserver.Stub() {

        @Override
        public void onChange() throws RemoteException {
            
        	Toast.makeText(MyAddonService.this, "History changed", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onBrowserDisconnected(Browser browser) {
    }

    public File fileLocation() {
		
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		} else {
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		Log.d(LOG_TAG,Environment.getExternalStorageState()+Boolean.toString(mExternalStorageAvailable) + Boolean.toString(mExternalStorageWriteable));
		if (mExternalStorageAvailable && mExternalStorageWriteable) {
			
			return new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Dolphin_History/");
			
		}
		else {
			//No external Storage;
			return null;
		}
	}

}
