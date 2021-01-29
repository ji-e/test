package com.hns17.ex_dpm;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.provider.Settings.SettingNotFoundException;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.hns17.ex_dpm.Ex_DPM.DpmClass;

public class MainActivity extends PreferenceActivity implements OnPreferenceClickListener  {
	public DevicePolicyManager devicePolicyManager;
	public ComponentName adminComponent;
    
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   
        addPreferencesFromResource(R.xml.pref);
        Preference Reen_Service = (Preference)findPreference("Reen_service");
        Preference Disable_Service = (Preference)findPreference("Disable_service");
        Preference Reen_DPM = (Preference)findPreference("Reen_device_policy");						
        Preference Disable_DPM = (Preference)findPreference("Disable_device_policy");						
        Preference Re_LockNow = (Preference)findPreference("Request_LockNow");						//���ȭ�� ī�װ�
        Preference Re_PassScreen = (Preference)findPreference("Request_PasswordScreen");			//������� ī�װ�
        Preference isPass = (Preference)findPreference("Request_isPass");					//������� ī�װ�
        
        Reen_Service.setOnPreferenceClickListener(this);
        Disable_Service.setOnPreferenceClickListener(this);
        Reen_DPM.setOnPreferenceClickListener(this);
        Disable_DPM.setOnPreferenceClickListener(this);
        Re_LockNow.setOnPreferenceClickListener(this);
        Re_PassScreen.setOnPreferenceClickListener(this);
        isPass.setOnPreferenceClickListener(this);
          
    }

	public boolean onPreferenceClick(Preference preference) {
        adminComponent = new ComponentName(this, DpmClass.class);
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
       
        boolean is_pass=false;
        		
		if(preference.getKey().equals("Reen_device_policy"))
        {
			if (!devicePolicyManager.isAdminActive(adminComponent)){        
	        	Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			   	intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
			   	intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
			   			"Additional text explaining why this needs to be added.");
			   	startActivityForResult(intent, 1);
	        }
			else
				Toast.makeText(this, "�̹� �������� ��� �� �Դϴ�.", Toast.LENGTH_SHORT).show();
        }
		else if(preference.getKey().equals("Disable_device_policy"))
        {
			if (devicePolicyManager.isAdminActive(adminComponent)){
				Toast.makeText(this, "�������� �����Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
				devicePolicyManager.removeActiveAdmin(adminComponent);
			}
			else
				Toast.makeText(this, "�̹� �������� �����Ǿ� �ֽ��ϴ�.", Toast.LENGTH_SHORT).show();
        }
		else if(preference.getKey().equals("Reen_service"))
        {
        	Intent intent = new Intent(getApplicationContext(), SmS_Service.class);

			startService(intent.setPackage("sms_service"));
        }
		else if(preference.getKey().equals("Disable_service"))
        {
			Intent intent = new Intent(getApplicationContext(), SmS_Service.class);

			stopService(intent.setPackage("sms_service"));
//			stopService(new Intent("sms_service"));
        }
		else if(preference.getKey().equals("Request_LockNow"))
        {
			if (devicePolicyManager.isAdminActive(adminComponent))
				devicePolicyManager.lockNow();
			else
				Toast.makeText(this, "�������� �����ϼž� �մϴ�.", Toast.LENGTH_SHORT).show();
        }
		else if(preference.getKey().equals("Request_PasswordScreen"))
        {
			Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
			startActivity(intent);
        }
		else if(preference.getKey().equals("Request_isPass"))
        {
			String PASSWORD_TYPE_KEY = "lockscreen.password_type";
			try {
				final boolean isPattern = 1 ==
						android.provider.Settings.System.getLong(getContentResolver(),
								android.provider.Settings.System.LOCK_PATTERN_ENABLED);
				
				long mode = android.provider.Settings.Secure.getLong(getContentResolver(),
		    			PASSWORD_TYPE_KEY);
		    	final boolean isPassword =
		    			DevicePolicyManager.PASSWORD_QUALITY_NUMERIC == mode
		    			|| DevicePolicyManager.PASSWORD_QUALITY_ALPHABETIC == mode
		    			|| DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC == mode;
		    	is_pass = isPassword || isPattern;
		    	
			} catch (SettingNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(is_pass)
				Toast.makeText(this, "��й�ȣ ���", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(this, "��й�ȣ �̻��", Toast.LENGTH_SHORT).show();			
        }
		
		// TODO Auto-generated method stub
		return false;
	}
    
}
