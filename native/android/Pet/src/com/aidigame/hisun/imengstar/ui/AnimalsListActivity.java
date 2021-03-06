package com.aidigame.hisun.imengstar.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.adapter.AnimalsListAdapter;
import com.aidigame.hisun.imengstar.adapter.UsersListAdapter;
import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.MyUser;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.http.json.UserJson;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UiUtil;
import com.aidigame.hisun.imengstar.R;
/**
 * 宠物列表界面
 * @author admin
 *
 */
public class AnimalsListActivity extends BaseActivity {
	private View viewTopWhite;
	
	
	private AnimalsListAdapter adapter;
	private ArrayList<Animal> list;
	private ListView  listView;
	private String aids;
	private ImageView back;
	private TextView title;
	private HandleHttpConnectionException handleHttpConnectionException;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users_list);
		listView=(ListView)findViewById(R.id.users_list_listview);
		back=(ImageView)findViewById(R.id.imageView1);
		title=(TextView)findViewById(R.id.textView1);
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		setBlurImageBackground();
		
		title.setText("参与宠物");
		aids=getIntent().getStringExtra("aids");
		
		list=new ArrayList<Animal>();
		adapter=new AnimalsListAdapter(this, list,null);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				final Animal user=list.get(position);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Animal u=HttpUtil.animalInfo(AnimalsListActivity.this,user, handleHttpConnectionException.getHandler(AnimalsListActivity.this));
						if(u!=null){
							if(NewPetKingdomActivity.petKingdomActivity!=null){
								if(NewPetKingdomActivity.petKingdomActivity.loadedImage1!=null&&!NewPetKingdomActivity.petKingdomActivity.loadedImage1.isRecycled()){
									NewPetKingdomActivity.petKingdomActivity.loadedImage1.recycle();
									NewPetKingdomActivity.petKingdomActivity.loadedImage1=null;
								}
								NewPetKingdomActivity.petKingdomActivity.finish();
								NewPetKingdomActivity.petKingdomActivity=null;
							}
							Intent intent=new Intent(AnimalsListActivity.this,NewPetKingdomActivity.class);
							intent.putExtra("animal", u);
							AnimalsListActivity.this.startActivity(intent);
							AnimalsListActivity.this.finish();
						}
					}
				}).start();
			}
		});
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				AnimalsListActivity.this.finish();
				System.gc();
			}
		});
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<Animal> temp=HttpUtil.otherAnimals(handleHttpConnectionException.getHandler(AnimalsListActivity.this), aids, 1, AnimalsListActivity.this);
				if(temp!=null){
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							adapter.updateList(temp);
							list=temp;
							adapter.notifyDataSetChanged();
						}
					});
				}
			}
		}).start();
	}
	/**
	 * 设置毛玻璃背景，列表滑动时顶部变透明并显示列表
	 */

	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		viewTopWhite=(View)findViewById(R.id.top_white_view);
       
		 listView.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					if(listView.getFirstVisiblePosition()==0&&listView.getChildAt(0).getTop()==0){
						viewTopWhite.setVisibility(View.VISIBLE);
					}else{
						if(viewTopWhite.getVisibility()!=View.GONE){
							viewTopWhite.setVisibility(View.GONE);
						}
					}
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub
					
				}
			});
	}


	

}
