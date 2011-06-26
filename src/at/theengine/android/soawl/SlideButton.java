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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ImageView;

/*
 * Author: Bruno Hautzenberger
 * This class implements a horizontal slide button control with
 * two events (onSlide and onClick)
 */
public class SlideButton extends LinearLayout {
	
	//the image that represents the slider
	private ImageView slider;
	
	//the application context
	private Context ctx;
	
	//the eventlistener which is called for all events
	private OnSlideButtonEventListener listener; 
	
	//slider position and size
    private int offset_x = 0;
    private int leftEnd = 0;
    private int rightEnd = 0;
    private int sliderWidth = 0;
    
    //state indicators
    private boolean initialized = false;
    private boolean isLeft = false;
    
    
	/* constructor
	 * initializes the button 
     */
	public SlideButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(HORIZONTAL);
		this.ctx = context;
		
		initButton();
	}
    
	/* 
	 * called from layout when this view should assign a size
	 * and a position to all of its children.
	 */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
    	//get bounds
    	this.leftEnd = l; 
    	this.rightEnd = r;
    	
    	if(!isLeft){ //button to the left only once
    		setButtonLeft();
    		sliderWidth = slider.getMeasuredWidth(); //get width of slider
    	}
    	
    	super.onLayout(changed, l, t, r, b); //call super
    }
    
    /*
     * initializes the button and add touch events to the slider
     */
    private void initButton()
    {
    	if(!initialized){ //initialize just once
    		
    		//initialize the slider image view and add it to the parent view
	    	this.slider = new ImageView(ctx); 
	    	
	    	Drawable d = ctx.getResources().getDrawable(R.drawable.slider);
	    	slider.setBackgroundDrawable(d);
	    	slider.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    	
	    	this.addView(slider);
	    	
	    	//add an onTouchListener to the slider
			this.slider.setOnTouchListener(new View.OnTouchListener() {
	            
				//ontouch get the xoffset of the event
	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
	                    switch(event.getActionMasked())
	                    {
	                            case MotionEvent.ACTION_DOWN:
	                                    offset_x = (int)event.getX();
	                                    break;
	                            default:
	                                    break;
	                    }
	                    
	                    return false;
	            }
			});
	    	
			//add an ontouchlistiner to the whole control
	    	this.setOnTouchListener(new View.OnTouchListener() {
	              
	    		//drag the control
		        @Override
		        public boolean onTouch(View v, MotionEvent event) {
		        	int x = 0;
		        	LinearLayout.LayoutParams lp;
		        	
		        	switch(event.getActionMasked())
		            {
		            	case MotionEvent.ACTION_MOVE:
		            		//drag the control
		            		x = (int)event.getX() - offset_x;
		            		
		            		if(x > rightEnd - sliderWidth)
		                    	x = rightEnd - sliderWidth;
		            		
		            		if(x < leftEnd)
		                    	x = leftEnd;
		            		
		                    lp = new LinearLayout.LayoutParams(
		                    		new ViewGroup.MarginLayoutParams(
		                    				LinearLayout.LayoutParams.WRAP_CONTENT,
		                                    LinearLayout.LayoutParams.WRAP_CONTENT));
	
		                    lp.setMargins(x, 0, 0, 0);
		                    slider.setLayoutParams(lp);
		                    break;
		            	
		            	case MotionEvent.ACTION_UP:
		            		//check if the button was dragged to left, right or if it just was clicked
		            		//and raise events.
		            		x = (int)event.getX() - offset_x;
		            		boolean handled = false;
		            		
		            		if((int)event.getX() > ((rightEnd - leftEnd) / 2)){
		            			listener.onSlide(v);
		            			handled = true;
		            		}
		            		
		            		if(!handled){
		            			listener.onClick(v);
		            		}
		            		
		            		setButtonLeft();
		                
		            	default:
		            		break;
		            }
		            return true;
		        }
		   });
    	}
    }

    /*
     * finalize inflating a view from XML.
     */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	/*
	 * Puts the button to the left
	 */
    private void setButtonLeft(){
		int x = 0;
		
		LayoutParams lp = new LinearLayout.LayoutParams(
        		new ViewGroup.MarginLayoutParams(
        				LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

		lp.setMargins(x, 0, 0, 0);
        slider.setLayoutParams(lp);
        isLeft = true;
    }
    
	/*
	 * sets a new onTouchListener
	 */
    public void setOnSlideButtonEventListener(OnSlideButtonEventListener listener){
    	this.listener = listener;
    }
}
