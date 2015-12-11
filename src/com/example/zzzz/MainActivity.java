package com.example.zzzz;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
TextView text;
private ImageView mImageView;
private Button btnStart;
private Button btnStop;
private TextView textView;
private Location mLocation;
private LocationManager mLocationManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		Drawable dra=getResources().getDrawable(R.drawable.ic_launcher);
//		text=(TextView)findViewById(R.id.aaa);
//		text.setBackground(dra);
//		text=(TextView)findViewById(R.id.fff);
//		text.setBackground(dra);
		 btnStart = (Button)findViewById(R.id.btnStart);
		 btnStop = (Button)findViewById(R.id.btnStop);
		 textView = (TextView)findViewById(R.id.text);
		 btnStart.setOnClickListener(btnClickListener); //��ʼ��λ
		 btnStop.setOnClickListener(btnClickListener); //������λ��ť
		mImageView=(ImageView)findViewById(R.id.imageview);
		mImageView.setImageResource(ss("bb"));
		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... arg0) {
				try {
					jiexi();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}
			
			protected void onPostExecute(Boolean result) {
				if(result==true){
					
					Log.e("", "onPostExecute"+result);
				}
			};
		}.execute();
	}
	public OnClickListener btnClickListener = new OnClickListener()
	{
	 public void onClick(View v)
	 {
	  Button btn = (Button)v;
	  if(btn.getId() == R.id.btnStart)
	  {
	   if(!gpsIsOpen())
	    return;
	    
	  mLocation = getLocation();
	    
	   if(mLocation != null)
	    textView.setText("ά��:" + mLocation.getLatitude() + "\n����:" + mLocation.getLongitude());
	   else
	    textView.setText("��ȡ��������");
	  }
	  else if(btn.getId() == R.id.btnStop)
	  {
	   mLocationManager.removeUpdates(locationListener);
	  }
	 
	 }
	};


//	�жϵ�ǰ�Ƿ���GPS
	private boolean gpsIsOpen()
	{
	 boolean bRet = true;
	  
	 LocationManager alm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
	 if(!alm.isProviderEnabled(LocationManager.GPS_PROVIDER))
	 {
	  Toast.makeText(this, "δ����GPS", Toast.LENGTH_SHORT).show();
	  bRet = false;
	 }
	 else
	 {
	  Toast.makeText(this, "GPS�ѿ���", Toast.LENGTH_SHORT).show();
	 }
	 
	 return bRet;
	}
//	�÷�����ȡ��ǰ�ľ�γ�ȣ� ��һ�λ�ȡ����null
//	�����LocationListener��ȡ�Ѹı��λ��
//	mLocationManager.requestLocationUpdates()�ǿ���һ��LocationListener�ȴ�λ�ñ仯
	private Location getLocation()
	{
	 //��ȡλ�ù������
	 mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
	 
	 //���ҷ�����Ϣ
	 Criteria criteria = new Criteria();
	 criteria.setAccuracy(Criteria.ACCURACY_FINE); //��λ����: ���
	 criteria.setAltitudeRequired(false); //������Ϣ������Ҫ
	 criteria.setBearingRequired(false); //��λ��Ϣ: ����Ҫ
	 criteria.setCostAllowed(true);  //�Ƿ�������
	 criteria.setPowerRequirement(Criteria.POWER_LOW); //�ĵ���: �͹���
	  
	 String provider = mLocationManager.getBestProvider(criteria, true); //��ȡGPS��Ϣ
	 
	 Location location = mLocationManager.getLastKnownLocation(provider);
	  
	 mLocationManager.requestLocationUpdates(provider, 2000, 5, locationListener);
	  
	 return location;
	}
//	�ķ����ǵȴ�GPSλ�øı��õ��µľ�γ��
	private final LocationListener locationListener = new LocationListener()
	{
	 public void onLocationChanged(Location location)
	 {
	  // TODO Auto-generated method stub
	  if(location != null)
	   textView.setText("ά��:" + location.getLatitude() + "\n����:"
	      + location.getLongitude());
	  else
	   textView.setText("��ȡ��������" );
	 }
	 
	 public void onProviderDisabled(String provider)
	 {
	  // TODO Auto-generated method stub
	 }
	 
	 public void onProviderEnabled(String provider)
	 {
	  // TODO Auto-generated method stub
	 }
	 
	 public void onStatusChanged(String provider, int status, Bundle extras)
	 {
	  // TODO Auto-generated method stub
	   
	 }
	};
//	private void ss(){
//		String str="http://apis.juhe.cn/plan/city?dtype=XML&key=bbf477f7f05e0bfe88d0860d7ce0a67d";
//		try {
//			URL url=new URL(str);
//			HttpURLConnection http=(HttpURLConnection) url.openConnection();
//			http.setConnectTimeout(5000);
//			String path=getCacheDir().getAbsolutePath()+File.separator+"airport.xml";
//			File f=new File(path);
//			FileOutputStream fos=new FileOutputStream(f);
//			BufferedOutputStream bos=new BufferedOutputStream(fos);
//			String airstr;
//			BufferedInputStream bis=null;
//			if(http.getResponseCode()==HttpStatus.SC_OK){
//				InputStream is=http.getInputStream();
//				bis=new BufferedInputStream(is);
//				StringBuffer buffer=new StringBuffer();
//				byte[] data=new byte[1024];
//				int len=0;
//				while ((len=bis.read(data))!=-1) {
//					bos.write(data, 0, len);
//					bos.flush();
//			}	
//			}
//			bis.close();
//			bos.close();
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	public List<AirportNumber> jiexi() throws IOException, JSONException{
		String txt=null;
		List<AirportNumber> mAirportNumbers=null;
		AssetManager manager=getResources().getAssets();
		InputStream is=manager.open("airport.xml");
		BufferedInputStream bis=new BufferedInputStream(is);
		StringBuffer buffer=new StringBuffer();
		int len=0;
		byte[] data=new byte[1024];
		while((len=bis.read(data))!=-1){
			String str=new String(data, 0, len);
			buffer.append(str);
			
		}
		txt=buffer.toString();
		JSONObject json=new JSONObject(txt);
		JSONArray array=json.getJSONArray("result");
		
		for (int i = 0; i < array.length(); i++) {
			JSONObject element=array.getJSONObject(i);
			String city=element.getString("city");
			String spell=element.getString("spell");
			Log.e("", "@@@@@@2"+city+spell);
			AirportNumber manum=new AirportNumber();
			manum.setCity(city);
			manum.setCitynumber(spell);
			mAirportNumbers.add(manum);
		}
		return mAirportNumbers;
	}
	private int ss(String b){
		int id=0;
		if(b.equals("bb")){
			id=R.drawable.alipay_icon;
		}
		return id;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
