package com.hns17.ex_dpm;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Toast;

import com.hns17.ex_dpm.Ex_DPM.DpmClass;

public class SmS_Service extends Service {
	private String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private KeyguardManager km = null; 						//키가드 매니저 생성
	private KeyguardManager.KeyguardLock keylock = null;	//키락 생성
	boolean test = false;
	private static View v;
	private static WindowManager mWindowManager;
		
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action.equals("android.intent.action.SCREEN_OFF")){
				if(!test){
					if(mWindowManager!=null){
						if(v!=null){
							mWindowManager.removeView(v);
							mWindowManager=null;
						}
					}
					LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = (View) inflater.inflate(R.layout.activity_main, null);
					

					int LAYOUT_FLAG;


					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

						LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

					}else{

						LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;

					}
					WindowManager.LayoutParams params = new WindowManager.LayoutParams(
							LAYOUT_FLAG,					//항상 최 상위에 있게. status bar 밑에 있음. 터치 이벤트 받을 수 있음.
							WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,		//이 속성을 안주면 터치 & 키 이벤트도 먹게 된다.
							PixelFormat.TRANSLUCENT
					);
					mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

					mWindowManager.addView(v, params);
				}
			}
			else if(action.equals(Intent.ACTION_USER_PRESENT)) {  
				//Toast.makeText(context, "Release", Toast.LENGTH_LONG).show();
				keylock.disableKeyguard();
				test = false;
			}
			else{
				String message = "";			
				String Compare = "1234"; //사용할 잠금메시지.
				Bundle bundle = intent.getExtras();
	        	
	        	if (bundle != null) {
	                Object[] pdus = (Object[]) bundle.get("pdus");
	                //sms 메시지를 가져온다.
	                for(Object pdu : pdus){
	                    SmsMessage smsMessage = 
	                                    SmsMessage.createFromPdu((byte[]) pdu);
	                    message += smsMessage.getMessageBody();
	                }
	            }
	        	//잠금 메시지를 받은 경우 화면을 잠근다
	        	if(message.equals(Compare)){
	        		Toast.makeText(context, "SMSRequest 호출", Toast.LENGTH_LONG).show();
	        		DevicePolicyManager devicePolicyManager;
	        		ComponentName adminComponent;
	        		adminComponent = new ComponentName(context, DpmClass.class);
	                devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
	                if (devicePolicyManager.isAdminActive(adminComponent))
	                {
	               		keylock.reenableKeyguard();	  
	               		test = true;
	                	devicePolicyManager.lockNow();
	                }
	                else
	                	Toast.makeText(context, "기기권한이 사용중이지 않습니다.", Toast.LENGTH_SHORT).show();
	        	}
				
			}	
		}
		
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
		super.onCreate();
		
		km=(KeyguardManager)getSystemService(Activity.KEYGUARD_SERVICE);
		if(km!=null){
    		keylock = km.newKeyguardLock("test");
    		keylock.disableKeyguard();
    		test = false;
    	}
		
		
	
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Toast.makeText(this, "서비스가 시작되었습니다.", Toast.LENGTH_LONG).show();
		IntentFilter filter = new IntentFilter(ACTION_SMS_RECEIVED);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_USER_PRESENT);
		registerReceiver(mReceiver, filter);
		return SmS_Service.START_NOT_STICKY;
	}
	
	@Override
	public void onDestroy(){
		if(keylock!=null){
			keylock.reenableKeyguard();
			test = true;
    	}
		if(mReceiver != null)
			unregisterReceiver(mReceiver);
		if(mWindowManager!=null)
			mWindowManager.removeView(v);
		Toast.makeText(this, "서비스 종료", Toast.LENGTH_LONG).show();
	}
	
	public static void removeview(){
		if(mWindowManager!=null){
			if(v!=null)
				mWindowManager.removeView(v);
			mWindowManager = null;
		}
	}

}
