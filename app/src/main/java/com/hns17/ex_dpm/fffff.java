package com.hns17.ex_dpm;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class fffff extends androidx.appcompat.widget.AppCompatImageView {
    private Curtain curtain;					//Our Stars class, managing all stars
    private float START_X, START_Y;
    private Context context;

//    public CustomGLSurfaceView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
//        //Set this as Renderer
//        this.setRenderer(this);
//        //Request focus
//        this.requestFocus();
//        this.setFocusableInTouchMode(true);
//
//        //
//        this.context = context;
//    }

    public fffff(@NonNull Context context) {
        super(context);
        init(context);
    }

    public fffff(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public fffff(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
//        this.setRenderer(this);
//        //Request focus
        this.requestFocus();
        this.setFocusableInTouchMode(true);
        this.context = context;
    }

//    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//        //Settings
//        gl.glEnable(GL10.GL_TEXTURE_2D);					//Enable Texture Mapping
//        gl.glShadeModel(GL10.GL_SMOOTH); 					//Enable Smooth Shading
//        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 			//Black Background
//        gl.glClearDepthf(1.0f); 							//Depth Buffer Setup
//
//        //Really Nice Perspective Calculations
//        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
//
//        gl.glEnable(GL10.GL_BLEND);							//Enable blending
//        gl.glDisable(GL10.GL_DEPTH_TEST);					//Disable depth test
//        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);		//Set The Blending Function For Translucency
//
//        int height=0, width = 0;
//        GLSurfaceView.getDefaultSize(height, getHeight());
//        GLSurfaceView.getDefaultSize(width, getWidth());
//
//        curtain = new Curtain();
//        curtain.loadGLTexture(gl, this.context);
//    }
//
//    public void onDrawFrame(GL10 gl) {
//        gl.glClear(GL10.GL_COLOR_BUFFER_BIT| GL10.GL_DEPTH_BUFFER_BIT);
//        curtain.draw(gl);
//    }
//
//    public void onSurfaceChanged(GL10 gl, int width, int height) {
//        if(height == 0) { 						//Prevent A Divide By Zero By
//            height = 1; 						//Making Height Equal One
//        }
//
//        gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
//        gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
//        gl.glLoadIdentity(); 					//Reset The Projection Matrix
//
//
//        //GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
//
//        gl.glOrthof(0, 768, 1024, 0, 1, -1);
//        gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
//        gl.glLoadIdentity(); 					//Reset The Modelview Matrix
//    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        SmS_Service sms = new SmS_Service();

        DisplayMetrics dis = new DisplayMetrics();
        Display display = ((WindowManager)context.getSystemService(context.WINDOW_SERVICE)).getDefaultDisplay();
        display.getMetrics(dis);
        int screenWidth = dis.widthPixels/2;
        int screenHeight = dis.heightPixels/3;

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            Toast.makeText(context, "touch", Toast.LENGTH_SHORT).show();
            START_X = event.getRawX();					//터치 시작 점
            START_Y = event.getRawY();					//터치 시작 점
        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE) {
            int x = (int)(event.getRawX() - START_X);	//이동한 거리
            int y = (int)(event.getRawY() - START_Y);	//이동한 거리

            if(x > screenWidth || x < -screenWidth || y > screenHeight || y < -screenHeight){
                sms.removeview();
            }
        }
        return true;
    }
}
