package com.aidigame.hisun.imengstar.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.MyUser;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.http.json.UserImagesJson;
import com.aidigame.hisun.imengstar.http.json.UserJson;
import com.aidigame.hisun.imengstar.http.json.UserImagesJson.Data;
import com.aidigame.hisun.imengstar.ui.UserCardActivity;
import com.aidigame.hisun.imengstar.ui.UsersListActivity;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UserStatusUtil;
import com.aidigame.hisun.imengstar.view.RoundImageView;
import com.aidigame.hisun.imengstar.R;
import com.easemob.chat.EMContactManager;
import com.easemob.exceptions.EaseMobException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 类似  围观群众 界面  用户信息列表
 * @author admin
 *
 */
public class SimpleUsersListAdapter extends BaseAdapter {
	DisplayImageOptions displayImageOptions;//显示图片的格式
    ImageLoader imageLoader;
	Activity context;
	ArrayList<MyUser> list;
	Handler handler;
	HandleHttpConnectionException handleHttpConnectionException;
	public SimpleUsersListAdapter(Activity context,ArrayList<MyUser> list,Handler handler){
		this.context=context;
		this.list=list;
		this.handler=handler;
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.user_icon)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
	}
	public void updateList(
			ArrayList<MyUser> temp) {
		// TODO Auto-generated method stub
		this.list=temp;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=null;
		if(convertView==null){
			holder=new Holder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_simple_users_listview, null);
			holder.icon=(RoundImageView)convertView.findViewById(R.id.icon_circleView);
			holder.name=(TextView)convertView.findViewById(R.id.textView2);
			holder.sendEmail=(TextView)convertView.findViewById(R.id.textView1);
//			holder.layout=(LinearLayout)convertView.findViewById(R.id.linearlayout);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		LogUtil.i("exception", "position========"+position);
//		if(position<list.size()){
		final MyUser data=list.get(position);
		
//		holder.icon.setImageBitmap();
		final RoundImageView view=holder.icon;
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*if(context instanceof UsersListActivity){
					UsersListActivity u=(UsersListActivity)context;
					if(!UserStatusUtil.isLoginSuccess(u,u.popup_parent,u.black_layout))return;
				}*/
				if(UserCardActivity.userCardActivity!=null){
					
					UserCardActivity.userCardActivity.finish();
					UserCardActivity.userCardActivity=null;
					System.gc();
				}
				Intent intent=new Intent(context,UserCardActivity.class);
				intent.putExtra("user", list.get(position));
				context.startActivity(intent);
			}
		});
			loadIcon(holder.icon, data);
		holder.name.setText(""+data.u_nick);
		/*holder.layout.setClickable(true);
		holder.layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,UserDossierActivity.class);
				UserImagesJson.Data d=new UserImagesJson.Data();
				d.usr_id=data.user.userId;
				d.user=data.user;
				d.isFriend=data.isFriend;
				intent.putExtra("data", d);
				context.startActivity(intent);
			}
		});*/
		holder.sendEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					EMContactManager.getInstance().deleteUserFromBlackList(""+data.userId);
					list.remove(data);
					notifyDataSetChanged();
//					Toast.makeText(context, "取消拉黑成功", Toast.LENGTH_LONG).show();
				} catch (EaseMobException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(context, "取消拉黑失败", Toast.LENGTH_LONG).show();
				}
				/*new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						final boolean flag=HttpUtil.unBlockOther(handler, data.userId, context);
						context.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(flag){
									list.remove(data);
									notifyDataSetChanged();
									Toast.makeText(context, "取消拉黑成功", Toast.LENGTH_LONG).show();
								}else{
									Toast.makeText(context, "取消拉黑失败", Toast.LENGTH_LONG).show();
								}
								
							}
						});
					}
				}).start();*/
			}
		});
		
		
//		}
		return convertView;
	}
	public void loadIcon(RoundImageView icon,final MyUser data){
		
		imageLoader=ImageLoader.getInstance();
		imageLoader.displayImage(Constants.USER_DOWNLOAD_TX+data.u_iconUrl, icon, displayImageOptions);
	}
	class Holder{
		RoundImageView icon;
		TextView name;
		TextView sendEmail;
//		LinearLayout layout;
	}


}
