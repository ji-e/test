package com.hns17.ex_dpm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Lockscreen extends Activity {
	private CustomGLSurfaceView mView;
	private ImageView img;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mView = (CustomGLSurfaceView) findViewById(R.id.surface_view);
		img = findViewById(R.id.img);
		img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SmS_Service sms = new SmS_Service();
				sms.removeview();
//				Intent intent = new Intent(getApplicationContext(), SmS_Service.class);
//
//				stopService(intent.setPackage("sms_service"));
			}
		});

		((Button)findViewById(R.id.btn)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("FDF","Fdfd");
				SmS_Service sms = new SmS_Service();
				sms.removeview();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();

//		mView.onResume();
	}

	/**
	 * Also pause our Lesson
	 */
	@Override
	protected void onPause() {
		super.onPause();
//		mView.onPause();
	}
}


