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

import android.view.View;

/*
 * Author: Bruno Hautzenberger
 * This is the abstract eventlistener for the TwoWaysSlideButton.
 * Implement this to react on TwoWaysSlideButtonEvents
 */
public abstract class OnTwoWaysSlideButtonEventListener {
	
	/*
	 * Called when the button was dragged to right or left.
	 */
	public abstract void onTwoWaysSlide(View v, OnTwoWaysSlideButtonEventListener.SlideDirection d);

	/*
	 * Called when the button was clicked.
	 */
	public abstract void onClick(View v);
	
	/*
	 * Slide Directions
	 */
	public enum SlideDirection{
		LEFT,RGHT
	}
}
