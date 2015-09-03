package com.agup.gps.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

public class ElapsedTimer {

	private Handler _handler;
	
	private long _startTime = 0;
	private long _millis;
	
	private long _delayMillis;
	
	private TextView _textView;
	
	private String _format = String.format("%%0%dd", 2); 

	public ElapsedTimer(){
		_handler = new Handler();
	}
	
	public void startTimer(long delayMillis,TextView id){
		_textView = (TextView) id;
		_delayMillis = delayMillis;
		_startTime = SystemClock.elapsedRealtime();
		_handler.removeCallbacks(updateTimerTask);

		updateTimerTask.run();
		//Thread taskThread = new Thread(updateTimerTask);
		//taskThread.start();
	}
	
	public void stopTimer(){
		_handler.removeCallbacks(updateTimerTask);
	}
	
	public void pauseTimer(){
		_handler.removeCallbacks(updateTimerTask);
	}
	
	public void unpauseTimer(){
		if(_startTime != 0){
			updateTimerTask.run();			
		}
	}
	
	private Runnable updateTimerTask = new Runnable() {
		
		@Override
		public void run() {
			final long start = _startTime;
			_millis = SystemClock.elapsedRealtime() - start;
			_textView.setText(getMinutes() + ":" + getSeconds() + ":" + getMillis());
			_handler.postDelayed(updateTimerTask, _delayMillis);
		}
	};
		
	public long getElapsedtime(){
		return _millis;
	}
	
	public String getMillis(){
		return String.format(_format, _millis % 1000);
	}
	
	public String getSeconds(){  
		return String.format(_format, _millis/1000 % 60);
	}
	
	public String getMinutes(){	
		return String.format(_format, (_millis/1000 % 3600) / 60);  
	}
	
	public String getHours(){
		return String.format(_format, TimeUnit.MILLISECONDS.toHours(_millis));
	}
	
	public String calculateTimeDifference(long begin, long end){
		long time = end - begin;
		String millis = String.format(_format, time % 1000);
		String secs = String.format(_format, time/1000 % 60);
		String mins = String.format(_format, (time/1000 % 3600) / 60);
		return mins + ":" + secs + ":" + millis;
	}
	
	public String convertMillisToHMSS(long millis){
		String seconds = String.format(_format, _millis/1000 % 60);
		String minutes = String.format(_format, (_millis/1000 % 3600) / 60);
		String hours = String.format(_format, TimeUnit.MILLISECONDS.toHours(millis));
		String ms = String.format(_format,  millis % 1000);
		return hours + ":" + minutes + ":" + seconds + ":" + ms;
	}
	
	/**
	 * Returns formatted String that includes time zone offset.
	 * @param millis
	 * @return
	 */
	public String convertMillisToMDYHMSS(long millis){
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSSZ",Locale.getDefault());
		formatter.setTimeZone(TimeZone.getDefault());
		Calendar calendar = Calendar.getInstance();		
	    calendar.setTimeInMillis(millis);
	    return formatter.format(calendar.getTime());
	}
	
}
