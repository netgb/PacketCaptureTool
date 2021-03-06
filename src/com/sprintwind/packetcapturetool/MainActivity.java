package com.sprintwind.packetcapturetool;

import com.baidu.appx.BDBannerAd;
import com.baidu.appx.BDBannerAd.BannerAdListener;
import com.baidu.mobstat.StatService;
import com.sprintwind.packetcapturetool.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;  
import android.widget.RelativeLayout;
import android.widget.Toast;



@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class MainActivity extends Activity {
	
	private Fragment[] fragments;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	
	private RadioGroup rdgrpBottomMenu;
	
	private RelativeLayout appxBannerContainer;
	private static BDBannerAd bannerAdView;
	
	private static final String TAG = "sprintwind";
	
	private long exitTime = 0;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        fragments = new Fragment[3];  
        fragmentManager = getFragmentManager();  
        fragments[0] = fragmentManager.findFragmentById(R.id.frgmntCapture);  
        fragments[1] = fragmentManager.findFragmentById(R.id.frgmntAnalyze);
        fragments[2] = fragmentManager.findFragmentById(R.id.frgmntMore);

        fragmentTransaction = fragmentManager.beginTransaction()  
                .hide(fragments[0]).hide(fragments[1]).hide(fragments[2]);  
        fragmentTransaction.show(fragments[0]).commit();  
        setFragmentIndicator(); 
        
        bannerAdView = new BDBannerAd(this, "CgK84vIbCSHjBP79f62GqzFFRWveWCIW",
				"d7u2VhB9RAKKB3j4k0R4PcMn");

		bannerAdView.setAdSize(BDBannerAd.SIZE_320X50);
		
		bannerAdView.setAdListener(new BannerAdListener() {

			@Override
			public void onAdvertisementDataDidLoadFailure() {
				Log.e(TAG, "load failure");
			}

			@Override
			public void onAdvertisementDataDidLoadSuccess() {
				Log.e(TAG, "load success");
			}

			@Override
			public void onAdvertisementViewDidClick() {
				Log.e(TAG, "on click");
			}

			@Override
			public void onAdvertisementViewDidShow() {
				Log.e(TAG, "on show");
			}

			@Override
			public void onAdvertisementViewWillStartNewIntent() {
				Log.e(TAG, "leave app");
			}
		});

		// ŽŽœš¹ãžæÈÝÆ÷
		appxBannerContainer = (RelativeLayout) findViewById(R.id.appx_banner_container);

		// ÏÔÊŸ¹ãžæÊÓÍŒ
		appxBannerContainer.addView(bannerAdView);
    }
    
    private void setFragmentIndicator() {  
    	  
        rdgrpBottomMenu = (RadioGroup) findViewById(R.id.rdgrpBottomMenu); 
  
        rdgrpBottomMenu.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
  
            @Override  
            public void onCheckedChanged(RadioGroup group, int checkedId) {  
                fragmentTransaction = fragmentManager.beginTransaction()  
                        .hide(fragments[0]).hide(fragments[1]).hide(fragments[2]);  
                switch (checkedId) {  
                case R.id.rdbttnCapture:  
                    fragmentTransaction.show(fragments[0]).commit();  
                    break;  
  
                case R.id.rdbttnAnalyze:  
                    fragmentTransaction.show(fragments[1]).commit();  
                    break; 
                    
                case R.id.rdbttnMore:
                	fragmentTransaction.show(fragments[2]).commit(); 
                	break;
                default:  
                    break;  
                }  
            }  
        });
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_share) {
        	Intent intent=new Intent(Intent.ACTION_SEND);   
            intent.setType("text/*");   
            intent.putExtra(Intent.EXTRA_SUBJECT, "分享");   
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_string));    
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
            startActivity(Intent.createChooser(intent, "分享"+getTitle()));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /* 监听返回键按下事件 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
            if((System.currentTimeMillis()-exitTime) > 2000){  
                Toast.makeText(getApplicationContext(), getString(R.string.press_to_exit), Toast.LENGTH_SHORT).show();                                
                exitTime = System.currentTimeMillis();   
            } else {
            	/* 取消注册网络变化通知 */
            	//unregisterReceiver(broadcastReceiver);
                finish();
                System.exit(0);
            }
            return true;   
        }
        return super.onKeyDown(keyCode, event);
    }
    
    public void onResume() {
		super.onResume();
		System.out.println("FragmentDemoActivity-->onResume");
		/**
		 * 页面起始（注意： 每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 如果该FragmentActivity包含了几个全页面的fragment，那么可以在fragment里面加入就可以了，这里可以不加入。如果不加入将不会记录该Activity页面。
		 */
		StatService.onResume(this);
	}

	public void onPause() {
		super.onPause();
		System.out.println("FragmentDemoActivity-->onPause");
		/**
		 * 页面结束（注意： 每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 如果该FragmentActivity包含了几个全页面的fragment，那么可以在fragment里面加入就可以了，这里可以不加入。如果不加入将不会记录该Activity页面。
		 */
		StatService.onPause(this);
	}

    
}
