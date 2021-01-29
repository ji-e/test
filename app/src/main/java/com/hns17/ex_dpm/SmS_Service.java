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
	private KeyguardManager km = null; 						//Ű���� �Ŵ��� ����
	private KeyguardManager.KeyguardLock keylock = null;	//Ű�� ����
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
							LAYOUT_FLAG,					//�׻� �� ������ �ְ�. status bar �ؿ� ����. ��ġ �̺�Ʈ ���� �� ����.
							WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,		//�� �Ӽ��� ���ָ� ��ġ & Ű �̺�Ʈ�� �԰� �ȴ�.
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
				String Compare = "1234"; //����� ��ݸ޽���.
				Bundle bundle = intent.getExtras();
	        	
	        	if (bundle != null) {
	                Object[] pdus = (Object[]) bundle.get("pdus");
	                //sms �޽����� �����´�.
	                for(Object pdu : pdus){
	                    SmsMessage smsMessage = 
	                                    SmsMessage.createFromPdu((byte[]) pdu);
	                    message += smsMessage.getMessageBody();
	                }
	            }
	        	//��� �޽����� ���� ��� ȭ���� ��ٴ�
	        	if(message.equals(Compare)){
	        		Toast.makeText(context, "SMSRequest ȣ��", Toast.LENGTH_LONG).show();
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
	                	Toast.makeText(context, "�������� ��������� �ʽ��ϴ�.", Toast.LENGTH_SHORT).show();
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
		Toast.makeText(this, "���񽺰� ���۵Ǿ����ϴ�.", Toast.LENGTH_LONG).show();
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
		Toast.makeText(this, "���� ����", Toast.LENGTH_LONG).show();
	}
	
	public static void removeview(){
		if(mWindowManager!=null){
			if(v!=null)
				mWindowManager.removeView(v);
			mWindowManager = null;
		}
	}

}
