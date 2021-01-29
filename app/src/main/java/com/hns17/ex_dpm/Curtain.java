package com.hns17.ex_dpm;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Curtain{
	private Aether aethers;					//Hold all our star instances in this array
		
	private float zoom = -15.0f;			//Distance Away From Stars
	private int[] textures = new int[5];
	
	public Curtain() {
				
		//Initiate the stars array
		aethers = new Aether();
		//Initiate our stars with random colors and increasing distance	
	}
	
	public void draw(GL10 gl) {
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
			//Recover the current star into an object
			Aether aether = aethers;
			
			gl.glLoadIdentity();							//Reset The Current Modelview Matrix

			gl.glTranslatef(0.0f, 0.0f, 0.0f); 				//Zoom Into The Screen (Using The Value In 'zoom')
			
			aether.draw(gl);
		
	}
		
	
	public void loadGLTexture(GL10 gl, Context context) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.knightrun);
		gl.glGenTextures(1, textures, 0);
		
		//Create Linear Filtered Texture and bind it to texture
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		
		//Clean up
		bitmap.recycle();
		
	}
}
