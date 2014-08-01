package com.example.astrolabe.customviews;

import com.example.astrolabe.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioGroup;

public class AstrolabeView extends View {
	
	int viewW, viewH;
	int plateW, plateH;
	int reteW, reteH;
	int ruleW, ruleH;
	int alidadeW, alidadeH;
	
	double reteAngle, ruleAngle, alidadeAngle; // Degree of rotation of rete, rule and alidade.
	double newPhi; //Degree to rotate.
	double oldTouchAngle; //Angle of last touch event.
	Bitmap plate, rete, backPlate, rule, alidade;
	public RadioGroup rotationGroup;
	float centreX;
	float centreY;
	Orientation ori;
	
	public enum Orientation {
		Back,
		Front
	}

	public AstrolabeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		reteAngle = 0;
		alidadeAngle = 0;
		ruleAngle = 0;
		oldTouchAngle=0;
		
		plate = BitmapFactory.decodeResource(getResources(),
				R.drawable.astrolabe_45n_pg4);
		rete = BitmapFactory.decodeResource(getResources(),
				R.drawable.astrolabe_45n_pg5);
		backPlate = BitmapFactory.decodeResource(getResources(),
				R.drawable.astrolabe_45n_pg3);
		rule = BitmapFactory.decodeResource(getResources(), R.drawable.rule);
		alidade = BitmapFactory.decodeResource(getResources(), R.drawable.alidade);
		
		plateW = plate.getWidth(); plateH = plate.getHeight();
		reteW = rete.getWidth(); reteH = rete.getHeight();
		ruleW = rule.getWidth(); ruleH = rule.getHeight();
		alidadeW = alidade.getWidth(); alidadeH = alidade.getHeight();
		
		//Natural Center with img size 968 is 482.
		centreX = plateW*482/968;
		centreY = plateH*482/968;
		ori = Orientation.Front;
		
	}
	
	@SuppressLint("DrawAllocation") @Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		viewW = MeasureSpec.getSize(widthMeasureSpec);
		viewH = MeasureSpec.getSize(heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		super.onTouchEvent(e);
		//TODO: Perform Click?
		float touchX = e.getX();
		float touchY = e.getY();
		double newTouchAngle = Math.atan2((touchX-centreX),(touchY-centreY)); //Gives ArcTan of X/Y rather than Y/X, because measuring coordinates from top.
		if (e.getAction()==MotionEvent.ACTION_DOWN) {
			oldTouchAngle = newTouchAngle; //Assimilates new touch event.
		}
		newPhi = newTouchAngle - oldTouchAngle;
		oldTouchAngle = newTouchAngle;
		invalidate(); //Forces onDraw Event.
		return true;
	}
	
	public boolean performClick() {
		super.performClick();
		return false;
	}
	
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		updateCanvas(canvas);
	}

	private void updateCanvas(Canvas canvas) {
		// Rotates and updates canvas element.
		if (ori == Orientation.Front) {
			canvas.drawBitmap(plate, 0, 0, null);
			canvas.save();
			if (rotationGroup.getCheckedRadioButtonId() == R.id.rete_button) {
				canvas.rotate((float)(-ruleAngle * 180 / Math.PI), plateW/2, plateH/2);
				canvas.drawBitmap(rule, (plateW-ruleW)/2, (plateH-ruleH)/2, null);
				canvas.restore();
				canvas.save();
				
				reteAngle += newPhi;
				canvas.rotate((float)(-reteAngle * 180 / Math.PI), plateW/2, plateH/2);
				canvas.drawBitmap(rete, (plateW-reteW)/2, (plateH-reteH)/2, null);
				canvas.restore();
				newPhi = 0; //Turn off rotation until new angle found.
			} else {
				canvas.rotate((float)(-reteAngle * 180 / Math.PI), plateW/2, plateH/2);
				canvas.drawBitmap(rete, (plateW-reteW)/2, (plateH-reteH)/2, null);
				canvas.restore();
				canvas.save();
				
				ruleAngle += newPhi;
				canvas.rotate((float)(-ruleAngle * 180 / Math.PI), plateW/2, plateH/2);
				canvas.drawBitmap(rule, (plateW-ruleW)/2, (plateH-ruleH)/2, null);
				canvas.restore();
				newPhi = 0; //Turn off rotation until new angle found.
			}
		} else {
			canvas.drawBitmap(backPlate, 0, 0, null);
			canvas.save();
			alidadeAngle += newPhi;
			canvas.rotate((float)(-alidadeAngle * 180 / Math.PI), plateW/2, plateH/2);
			canvas.drawBitmap(alidade, (plateW-alidadeW)/2, (plateH-alidadeH)/2, null);
			canvas.restore();
			newPhi = 0; //Turn off rotation until new angle found.
			
		}
	}
	
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	public void setOrientation(Orientation newOri) {
		ori = newOri;
		invalidate();
	}
	
	public Orientation getOrientation() {
		if (ori == Orientation.Back)
			return Orientation.Back;
		else //Orientation ifS front.
			return Orientation.Front;
	}
	
	
	
}
