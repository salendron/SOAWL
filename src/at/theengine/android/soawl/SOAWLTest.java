//SOAWL (Salendron's open Android widget library) is a collection
//of android widgets and views that aren't available in the 
//standard Android framework developed by Bruno Hautzenberger.
//
//This file is part of SOAWL.
//
//SOAWL is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//SOAWL is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with SOAWL.  If not, see <http://www.gnu.org/licenses/>.

package at.theengine.android.soawl;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/*
 * Author: Bruno Hautzenberger
 * This class implements tests all controls contained in this library
 */
public class SOAWLTest extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //initialize TwoWaysSlideButton
        initTwoWaysSlideButton();
    }
    
    /*
     * Initializes the TwoWaysSlideButton
     */
    private void initTwoWaysSlideButton(){
        TwoWaysSlideButton sbTest = (TwoWaysSlideButton) findViewById(R.id.twsbTest);
        
        //set a new setOnTwoWaysSlideButtonEventListener
        sbTest.setOnTwoWaysSlideButtonEventListener(new OnTwoWaysSlideButtonEventListener(){
        	
        	@Override
        	public void onTwoWaysSlide(View v, OnTwoWaysSlideButtonEventListener.SlideDirection d){
        		if(d == SlideDirection.LEFT){
        			((TextView) findViewById(R.id.tvTwsbTest)).setText(R.string.left);
        		}
        		
        		if(d == SlideDirection.RGHT){
        			((TextView) findViewById(R.id.tvTwsbTest)).setText(R.string.right);
        		}
        	}
        	
        	@Override
        	public void onClick(View v){
        		((TextView) findViewById(R.id.tvTwsbTest)).setText(R.string.clicked);
        	}
        });
    }
}