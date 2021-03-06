package com.aidigame.hisun.imengstar.ui;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.widget.Toast;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UiUtil;
import com.aidigame.hisun.imengstar.R;

public class AlbumPictureBackground extends Activity {
	private  static final int MODE_REGISTER=30;//注册
	private  static final int MODE_CHANGE_ICON=31;//更改头像
	private  static final int MODE_TOPIC=32;//发表图片
	private int mode=-1;
	private String filename;
	private String path;
	private Animal animal;
	private int isBeg;
	private long star_id;
	private int topic_id=-1;
	private String topic_name;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.obj!=null){
				Uri uri=(Uri)msg.obj;
				Intent intent3=new Intent(AlbumPictureBackground.this,SubmitPictureActivity.class);
				intent3.setData(uri);
				intent3.putExtra("mode", 0);
				intent3.putExtra("animal", animal);
				intent3.putExtra("isBeg", isBeg);
				intent3.putExtra("star_id", getIntent().getLongExtra("star_id", -1));
				uri.getPath();
				LogUtil.i("me", "图库返回的uri="+uri.toString());
				LogUtil.i("me", "图库返回的uri.getPath()="+uri.getPath());
				intent3.putExtra("path", path);
				AlbumPictureBackground.this.startActivity(intent3);
				
				finish();
				System.gc();
			}
				
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		LogUtil.i("me", "创建TakePictureBackground");
		setContentView(R.layout.activity_take_picture_background);
		mode=getIntent().getIntExtra("mode", -1);
		isBeg=getIntent().getIntExtra("isBeg", 0);
		switch (mode) {
		case -1:
			
			finish();
			System.gc();
			break;
		}
		animal=(Animal)getIntent().getSerializableExtra("animal");
		star_id=getIntent().getLongExtra("star_id", -1);
		topic_id=getIntent().getIntExtra("topic_id", -1);
		topic_name=getIntent().getStringExtra("topic_name");
		Intent intent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		this.startActivityForResult(intent, 22);
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			
			if(requestCode==11){
				if(data.getBooleanExtra("cancel", false)){
					Intent intent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					this.startActivityForResult(intent, 22);
					return;
				}else{
	                	LogUtil.i("me", "msg.what================");
						Message msg=handler.obtainMessage();
						msg.obj=data.getData();
						handler.sendMessageAtTime(msg, 50);
						return;
					
				}
				
			}else if(requestCode==22){
				loadBitmap(data.getData());
				BitmapFactory.Options options=new BitmapFactory.Options();
				options.inJustDecodeBounds=true;
				BitmapFactory.decodeFile(path, options);
				int width=options.outWidth;
				int height=options.outHeight;
				float standScaleW_H=Constants.screen_width*1f/Constants.screen_height;
				float standScaleH_W=Constants.screen_height*1f/Constants.screen_width;
				float max=(standScaleH_W>standScaleW_H?standScaleH_W:standScaleW_H)*2;
				float min=(standScaleH_W>standScaleW_H?standScaleW_H:standScaleH_W)/2;
                float bmpScale=width*1f/height;
                if(bmpScale>=min&&bmpScale<=max){
                	/*if(width<100&&height<100){
                    	Toast.makeText(this, "您选择的图片太小。", 2000).show();
                    	Intent intent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    					this.startActivityForResult(intent, 22);
    					return;
                    }*/
                }else{
                	/*Toast.makeText(this, "您选择的图片宽高比例不符合规范。", 2000).show();
                	Intent intent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					this.startActivityForResult(intent, 22);
					return;*/
                }
			}
			loadBitmap(data.getData());
			
			switch (mode) {
			case MODE_REGISTER:
				LogUtil.i("me", "msg.what================");
				Intent intent1=getIntent();
				intent1.putExtra("path", path);
				setResult(RESULT_OK,intent1);
				this.finish();
				break;
			case MODE_CHANGE_ICON:
				LogUtil.i("me", "msg.what================");
				Intent intent2=getIntent();
				intent2.putExtra("path",path);
				setResult(RESULT_OK,intent2);
				this.finish();
				break;
			case MODE_TOPIC:
				LogUtil.i("me",""+Constants.Picture_Camera+File.separator+filename);
				/*Intent intent=new Intent(this,com.aviary.android.feather.FeatherActivity.class);
				intent.putExtra("mode", 1);
				intent.setData(data.getData());
				intent.putExtra(com.aviary.android.feather.library.Constants.EXTRA_IN_API_KEY_SECRET, Constants.EXTRA_IN_API_KEY_SECRET);
				startActivityForResult(intent, 11); */
				
				
				
				Intent intent3=new Intent(AlbumPictureBackground.this,SubmitPictureActivity.class);
				intent3.setData(data.getData());
				intent3.putExtra("mode", 0);
				intent3.putExtra("animal", animal);
				intent3.putExtra("isBeg", isBeg);
				intent3.putExtra("path", path);
				intent3.putExtra("star_id",star_id );
				intent3.putExtra("topic_id", getIntent().getIntExtra("topic_id", -1));
				intent3.putExtra("topic_name", getIntent().getStringExtra("topic_name"));
				AlbumPictureBackground.this.startActivity(intent3);
				AlbumPictureBackground.this.finish();
				
				
				
				break;
			}
			
		}else{
			this.finish();
		}

		

	}
	private  void loadBitmap(Uri uri){
		Cursor cursor=getContentResolver().query(uri, null, null, null, null);
		if(cursor!=null){
			cursor.moveToFirst();
			path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
			cursor.close();
		}
		
		
	}
	   @Override
	   protected void onPause() {
	   	// TODO Auto-generated method stub
	   	super.onPause();
	   	StringUtil.umengOnPause(this);
	   }
	      @Override
	   protected void onResume() {
	   	// TODO Auto-generated method stub
	   	super.onResume();
	   	StringUtil.umengOnResume(this);
	   }

}
