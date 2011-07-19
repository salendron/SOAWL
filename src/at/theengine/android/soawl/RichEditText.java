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

import java.util.Iterator;
import java.util.LinkedList;

import android.content.Context;
import android.graphics.*;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.widget.EditText;

/*
 * Author: Bruno Hautzenberger
 * This class implements a EditText View which can mix 
 * text styles and can display lines for every
 * line of text. (kind of basic rich text)
 */
public class RichEditText extends EditText {
	
	//Rect and Paint Object for drawing lines
    private Rect rect;
    private Paint paint;
    
    //text style flags
    private boolean bold;
    private boolean italic;
    
    //flag to control if lines a drawn or not
    private boolean lines;
    
    private CharSequence lastKnownText = null;
    
    private RichTextSpanCollection<StyleSpan> styleSpans;

    /* constructor
	 * initializes the RichEditText View
     */
    public RichEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        rect = new Rect();
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        this.setTextColor(Color.BLACK);
    }

    /*
     * Draws the lines
     */
    @Override
    protected void onDraw(Canvas canvas) {
    	if(lines){
	        int count = getLineCount();
	        Rect r = rect;
	        Paint p = paint;
	        
	        for (int i = 0; i < count; i++) {
	            int baseline = getLineBounds(i, r);
	            canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, p); //draw the lines
	        }
    	}
    	
        super.onDraw(canvas);
    }
    
    /*
     * Here we apply the different text styles and colors.
     */
    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
    	super.onTextChanged(text, start, before, after);
    	
    	if(lastKnownText == null || !text.toString().equals(lastKnownText.toString()) ){
    	initSpans();
    	
    	//Spannable str = this.getText();
    	Spannable str = new SpannableString(this.getText().toString());     
    	
    	if(before > after){
    		this.styleSpans.removeSpans(start + after,start + before);
    	}
    	
    	if(after >= before){
    		CharSequence newText = str.subSequence(start + before, start + after);
    		
    		styleSpans.moveSpansBackward(start + after, after - before);
    		
    		if(italic && newText.length() > 0){
    			styleSpans.addSpan(new StyleSpan(android.graphics.Typeface.ITALIC), start + before, start + before);
    		}
    		
    		if(bold && newText.length() > 0){
    			styleSpans.addSpan(new StyleSpan(android.graphics.Typeface.BOLD), start + before, start + before);
    		}
    	}
    	
        Iterator<RichTextSpan<StyleSpan>> styleIt = this.styleSpans.getSpans(this.getText().length()).iterator();
    	while(styleIt.hasNext()){
    		RichTextSpan<StyleSpan> span = styleIt.next();
    		str.setSpan(span.getSpan(), span.getPos(), span.getEnd() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);	
    	}
    	
    	lastKnownText = text;
    	
    	int sel = this.getSelectionStart();
    	this.setText(str);
    	this.setSelection(sel);
    	}
    }

    private void initSpans(){
    	if(this.styleSpans == null){
    		this.styleSpans = new RichTextSpanCollection<StyleSpan>();
    	}
    }
    
    
    @Override
    public Editable getText() {
    	return super.getText();
    }
    
    public void setStyles(RichTextSpanCollection<StyleSpan> styles, CharSequence text){
    	this.styleSpans = styles;
    	this.lastKnownText = null;
    	this.setText(text);
    }
    
    public RichTextSpanCollection<StyleSpan> getStyles(){
    	return this.styleSpans;
    }
    
    public void setBold(boolean bold){
    	this.bold = bold;
    }
    
    public boolean getBold(){
    	return this.bold;
    }
    
    public void setItalic(boolean italic){
    	this.italic = italic;
    }
    
    public boolean getItalic(){
    	return this.italic;
    }
    
    public void setDrawLines(boolean drawLines){
    	this.lines = drawLines;
    }
    
    private static class RichTextSpan<T> {
    	
    	private T span;
    	private int pos;
    	private int end;
    	
		public RichTextSpan(T span, int pos, int end) {
			this.span = span;
			this.pos = pos;
			this.end = end;
		}

		public int getPos() {
			return pos;
		}
		
		public int getEnd() {
			return end;
		}

		public T getSpan() {
			return span;
		}
    	
    	
    }

    private static class RichTextSpanCollection<T> {
    	
    	private LinkedList<RichTextSpan<T>> spans;

		public RichTextSpanCollection() {
			spans = new LinkedList<RichTextSpan<T>>();
		}
    	
    	public void addSpan(T span, int pos, int end){
    		spans.add(new RichTextSpan<T>(span, pos, end));
    	}
    	
    	public void removeSpans(int begin, int end){
    		Iterator<RichTextSpan<T>> it = spans.iterator();
    		while(it.hasNext()){
    			RichTextSpan<T> span = it.next();
    			if(span.getPos() >= begin && span.getPos() < end){
    				spans.remove(span);
    				it = spans.iterator();
    			}
    		}
    		
    		//move all other spans forward
    		int length = end - begin;
    		it = spans.iterator();
     		while(it.hasNext()){
     			RichTextSpan<T> span = it.next();
     			if(span.pos > begin){
     				span.pos -= length;
     				span.end -= length;
     			}
     		}
    	}
    	
    	public void moveSpansBackward(int begin, int length){
    		Iterator<RichTextSpan<T>> it = spans.iterator();
     		while(it.hasNext()){
     			RichTextSpan<T> span = it.next();
     			if(span.pos >= begin){
     				span.pos += length;
     				span.end += length;
     			}
     		}
    	}
    	
    	public LinkedList<RichTextSpan<T>> getSpans(int textlength){
    		return spans;
    	}
		
    }
}
