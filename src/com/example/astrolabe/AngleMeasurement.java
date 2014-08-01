package com.example.astrolabe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class AngleMeasurement extends Activity implements SensorEventListener {
	
	/** Identifier for returning intent with result of angle data.*/
	public static final String MEASURED_ANGLE = "MEASURED_ANGLE";
	
	SensorManager mSensorManager;
	Sensor rotVec;
	long angle;
	TextView angleDisplay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_angle_measurement);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        rotVec = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        angleDisplay = (TextView) findViewById(R.id.angle_display_measure);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.angle_measurement, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		angle = Math.round(2*180/Math.PI*Math.asin((Math.sqrt(Math.pow(event.values[0],2)+Math.pow(event.values[1],2)))));
		angleDisplay.setText(""+angle+"°");
	}
	
	public void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, rotVec, Sensor.TYPE_ROTATION_VECTOR);
	}
	
	public void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}
	
	public void onBackPressed() {
		returnAngle();
	}
	
	public void touchEvent(View view) {
		returnAngle();
	}
	
	public void returnAngle() {
		Intent ret = new Intent();
		ret.putExtra(MEASURED_ANGLE, angle);
		setResult(RESULT_OK, ret); //Sets result from activity to return to MainMenu.
		finish(); //Return to main menu.
	}
	
}
