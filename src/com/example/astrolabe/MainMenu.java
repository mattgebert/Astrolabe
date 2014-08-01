package com.example.astrolabe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.astrolabe.customviews.AstrolabeView;
import com.example.astrolabe.customviews.AstrolabeView.Orientation;

public class MainMenu extends ActionBarActivity {

	private SharedPreferences prefs;
	private TextView angleText;
	private AstrolabeView astrolabe;
	private Button orientationSwitch;
	private MenuItem menuOrientationSwitch;
	private long angle;
	
	private final static byte MEASURE_ANGLE_REQUEST = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        
        Spinner spinner = (Spinner) findViewById(R.id.stars_spinner);
        angleText = (TextView) findViewById(R.id.angle_display_main);
        astrolabe = (AstrolabeView) findViewById(R.id.astrolabe);
        orientationSwitch = (Button) findViewById(R.id.orientationSwitch);
        astrolabe.rotationGroup = (RadioGroup) findViewById(R.id.rotationBtnGrp);
//        menuOrientationSwitch = (Item) this.getActionBar().(R.id.menuSwitchOrientation); //TODO: Change menu item text.
        
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, 
	    		R.array.stars_array, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menuOrientationSwitch = menu.findItem(R.id.menuSwitchOrientation);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    	switch (item.getItemId()) {
			case(R.id.info):
				info();
				return true;
			case(R.id.menuSwitchOrientation):
				setOrientation();
			case(R.id.action_settings):
				return true;
			default:
				//Do nothing.
    	}
        return super.onOptionsItemSelected(item);
    }
    
    public void measureAngle(View view) {
    	Intent intent = new Intent(this, AngleMeasurement.class);
    	startActivityForResult(intent, MEASURE_ANGLE_REQUEST);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
    		case(MEASURE_ANGLE_REQUEST):
    			if (resultCode == RESULT_OK)
    				angle = data.getExtras().getLong(AngleMeasurement.MEASURED_ANGLE, 99);
    				angleText.setText(""+angle+"°");
    	}
    }
    
    public void onDestroy() {
    	prefs.edit().putBoolean("finished", true).commit();
    	System.out.println("Destroyed");
    	super.onDestroy();
    }
    
    public void onStart() {
    	super.onStart();
    }
    
    public void info() {
    	Intent intent = new Intent(this, Info.class);
    	startActivity(intent);
    }
    
    public void setOrientation(View view) {
    	setOrientation();
    }
    
    public void setOrientation() {
		if (astrolabe.getOrientation() == Orientation.Front) {
			astrolabe.setOrientation(Orientation.Back);
			orientationSwitch.setText(R.string.button_orientation_back);
			menuOrientationSwitch.setTitle(R.string.button_orientation_back);
		} else {
			astrolabe.setOrientation(Orientation.Front);
			orientationSwitch.setText(R.string.button_orientation_front);
			orientationSwitch.setText(R.string.button_orientation_front);
		}	
    }
}