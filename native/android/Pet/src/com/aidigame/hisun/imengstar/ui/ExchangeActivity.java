package com.aidigame.hisun.imengstar.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.adapter.GalleryAdapter;
import com.aidigame.hisun.imengstar.adapter.MarketRealGridViewAdapter;
import com.aidigame.hisun.imengstar.adapter.OnlyShowIconAdapter;
import com.aidigame.hisun.imengstar.adapter.GalleryAdapter.ClickViewListener;
import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.Gift;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UiUtil;
import com.aidigame.hisun.imengstar.view.HorizontalListView2;
import com.aidigame.hisun.imengstar.view.HorizontialListView;
import com.aidigame.hisun.imengstar.view.RoundImageView;
import com.aidigame.hisun.imengstar.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
/**
 * 兑换界面
 * @author admin
 *
 */
public class ExchangeActivity extends BaseActivity implements OnClickListener{
	ImageView backIv,foodIv,showIconsIv;
	LinearLayout typeLayout;
	TextView typeTv,foodTv;
	GridView gridView;
	RoundImageView petIcon;
	MarketRealGridViewAdapter marketRealGridViewAdapter;
	ArrayList<Gift> giftList;
	ArrayList<Gift> giftTotalList;
	public static ExchangeActivity exchangeActivity;
	PopupWindow popupWindow;
	Animal animal;
	ArrayList<Animal> animals;
	Handler handler;
	GalleryAdapter galleryAdapter;
	int currentPosition;
	
//	Gallery gallery;
	HorizontalListView2  linearLayoutForListView;
	LinearLayout iconsLayout;
	ImageView leftIv,rightIv;
	RelativeLayout bottomLayout,rooLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exchange);
		
		rooLayout=(RelativeLayout)findViewById(R.id.root_layout);
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=4;
		rooLayout.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.blur, options)));
		
		exchangeActivity=this;
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		margin=-getResources().getDimensionPixelSize(R.dimen.one_dip)*46;
		loadData();
		initView();
	}
	
	private void loadData() {
		// TODO Auto-generated method stub
		animals=new ArrayList<Animal>();
		if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null){
			for(int i=0;i<PetApplication.myUser.aniList.size();i++){
				if(PetApplication.myUser.userId==PetApplication.myUser.aniList.get(i).master_id){
					animals.add(PetApplication.myUser.aniList.get(i));
				}
			}
		}
		if(animals.size()>0){
			animal=animals.get(0);
			currentPosition=0;
		}else{
			animal=new Animal();
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ArrayList<Gift> gifts=HttpUtil.exchangeFoodList(handler, null, ExchangeActivity.this);
				final ArrayList<Gift> temp=new ArrayList<Gift>();
				if(gifts!=null){
					for(int i=0;i<gifts.size();i++){
						boolean flag=HttpUtil.giftInfo(handler, gifts.get(i), ExchangeActivity.this);
						if(flag){
							temp.add(gifts.get(i));
						}
					}
					
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							giftList=temp;
							giftTotalList=new ArrayList<Gift>();
							for(int i=0;i<giftList.size();i++){
								giftTotalList.add(giftList.get(i));
							}
							
							marketRealGridViewAdapter.updateList(temp);
							marketRealGridViewAdapter.notifyDataSetChanged();
						}
					});
					
					
				}
			}
		}).start();
	}

	private void initView() {
		// TODO Auto-generated method stub
		backIv=(ImageView)findViewById(R.id.back);
		typeLayout=(LinearLayout)findViewById(R.id.type_layout);
		typeTv=(TextView)findViewById(R.id.type_tv);
		gridView=(GridView)findViewById(R.id.grid_view);
		foodTv=(TextView)findViewById(R.id.food_tv);
		foodIv=(ImageView)findViewById(R.id.food_iv);
		petIcon=(RoundImageView)findViewById(R.id.pet_icon);
		showIconsIv=(ImageView)findViewById(R.id.show_icons_iv);
		bottomLayout=(RelativeLayout)findViewById(R.id.bottom_layout);
		
		
//		gallery=(Gallery)findViewById(R.id.galleryview);
		linearLayoutForListView=(HorizontalListView2 )findViewById(R.id.galleryview);
		iconsLayout=(LinearLayout)findViewById(R.id.icons_layout);
		leftIv=(ImageView)findViewById(R.id.left_iv);
		rightIv=(ImageView)findViewById(R.id.right_iv);
		
		galleryAdapter=new GalleryAdapter(this, animals,0);
		galleryAdapter.setClickViewListener(new ClickViewListener() {
			
			@Override
			public void onClick(int position) {
				// TODO Auto-generated method stub
				loadIcon(animals.get(position));
				currentPosition=position;
			}
		});
		linearLayoutForListView.setAdapter(galleryAdapter);
//		gallery.setAdapter(galleryAdapter);
		if(animals.size()>0){
//			gallery.scrollTo(gallery.getMeasuredWidth(), 0);
		}
		
		
		
		
		giftList=new ArrayList<Gift>();
		marketRealGridViewAdapter=new MarketRealGridViewAdapter(this, giftList);
		gridView.setAdapter(marketRealGridViewAdapter);
		loadIcon(animal);
		changeFoodNum(animal.foodNum);
		showIconsIv.setOnClickListener(this);
		backIv.setOnClickListener(this);
		typeLayout.setOnClickListener(this);
		leftIv.setOnClickListener(this);
		rightIv.setOnClickListener(this);
		initListener();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<Animal> anim=HttpUtil.usersKingdom(ExchangeActivity.this,PetApplication.myUser, 1, handler);
				if(anim!=null&&anim.size()>0){
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							PetApplication.myUser.aniList=anim;
							animals=new ArrayList<Animal>();
							if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null){
								for(int i=0;i<PetApplication.myUser.aniList.size();i++){
									if(PetApplication.myUser.userId==PetApplication.myUser.aniList.get(i).master_id){
										animals.add(PetApplication.myUser.aniList.get(i));
									}
								}
							}
							galleryAdapter.update(animals);
							if(animals.size()>0)
							loadIcon(animals.get(0));
							galleryAdapter.notifyDataSetChanged();
						}
					});
				}
			}
		}).start();
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Gift gift=giftList.get(position);
				/*if(animal.foodNum<gift.price){
					Intent intent=new Intent(ExchangeActivity.this,Dialog3Activity.class);
					intent.putExtra("mode", 2);
					ExchangeActivity.this.startActivity(intent);
				}else{*/
					Intent intent=new Intent(ExchangeActivity.this,GiftInfoActivity.class);
					
					gift.animal=animal;
					intent.putExtra("gift", gift);
					ExchangeActivity.this.startActivity(intent);
//				}
				
			}
		});
	}
	private void initListener() {
		// TODO Auto-generated method stub
		petIcon.setOnClickListener(this);
		linearLayoutForListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				loadIcon(animals.get(position));
				hideIcons(true);
			}
		});
	}

	public void changeFoodNum(long num){
		if(num<=0){
			foodTv.setText("0");
			foodIv.setImageResource(R.drawable.exchange_food_red);
		}else{
			foodTv.setText(""+num);
			foodIv.setImageResource(R.drawable.exchange_food_red);
		}
		
	}
	/**
	 * 用户，宠物头像下载
	 */
	public  void loadIcon(Animal animal) {
		// TODO Auto-generated method stub
		this.animal=animal;
		BitmapFactory.Options options=new BitmapFactory.Options();
		
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		DisplayImageOptions displayImageOptions1=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.pet_icon)
	            .showImageOnFail(R.drawable.pet_icon)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
		ImageLoader imageLoader1=ImageLoader.getInstance();
		imageLoader1.displayImage(Constants.ANIMAL_DOWNLOAD_TX+animal.pet_iconUrl, petIcon, displayImageOptions1);
		changeFoodNum(animal.foodNum);
		
		
	}
	public void hideIcons(final boolean isHide){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean flag=true;
				while(flag){
					RelativeLayout.LayoutParams param=(RelativeLayout.LayoutParams)bottomLayout.getLayoutParams();
					if(param==null){
						param=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
					}
					if(isHide){
						animHandler.sendEmptyMessage(1);
						if(param.bottomMargin==margin){
							flag=false;
							showList=false;
						}
					}else{
						animHandler.sendEmptyMessage(2);
						if(param.bottomMargin==0){
							flag=false;
							showList=true;
						}
					}
					try {
						Thread.sleep(100/30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}).start();
	}
	int margin;
	Handler animHandler=new Handler(){
		int speed;
		
		public void handleMessage(android.os.Message msg) {
			speed=(getResources().getDimensionPixelSize(R.dimen.one_dip))*46/40;
			RelativeLayout.LayoutParams param=(RelativeLayout.LayoutParams)bottomLayout.getLayoutParams();
			if(param==null){
				param=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			}
			if(msg.what==1){
				param.bottomMargin-=speed;
				if(param.bottomMargin<=margin){
					param.bottomMargin=margin;
				}
				bottomLayout.setLayoutParams(param);
			}else if(msg.what==2){
				param.bottomMargin+=speed;
				if(param.bottomMargin>=0){
					param.bottomMargin=0;
				}
				bottomLayout.setLayoutParams(param);
			}
			
		};
	};
    boolean showList=false;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.show_icons_iv:
			
			
			break;
		case R.id.back:
			if(isTaskRoot()){
				if(HomeActivity.homeActivity!=null){
					ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
					am.moveTaskToFront(HomeActivity.homeActivity.getTaskId(), 0);
				}else{
					Intent intent=new Intent(this,HomeActivity.class);
					this.startActivity(intent);
				}
			}
			exchangeActivity=null;
			
			
			finish();
			System.gc();
			break;
		case R.id.type_layout:
			showType();
			break;
		case R.id.pet_icon:
			if(animals.size()<=0)return;
			if(!showList){
				hideIcons(false);
			}else{
				
				hideIcons(true);
			}
			
			break;

		default:
			break;
		}
	}
	public void showType(){
		View view=LayoutInflater.from(this).inflate(R.layout.popup_exchange_1, null);
		TextView tv1=(TextView)view.findViewById(R.id.textView1);
	    TextView tv2=(TextView)view.findViewById(R.id.textView2);
	    TextView tv3=(TextView)view.findViewById(R.id.textView3);
	    TextView tv4=(TextView)view.findViewById(R.id.textView4);
		popupWindow=new PopupWindow(view,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.showAsDropDown(typeLayout, 0, getResources().getDimensionPixelSize(R.dimen.one_dip)*10);
        tv1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
				typeTv.setText("全部");
				giftList.removeAll(giftList);
				Gift gift=null;
				for(int i=0;i<giftTotalList.size();i++){
					gift=giftTotalList.get(i);
						giftList.add(gift);
					
				}
				marketRealGridViewAdapter.updateList(giftList);
				marketRealGridViewAdapter.notifyDataSetChanged();
			}
		});
        tv2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
				typeTv.setText("猫粮");
				giftList.removeAll(giftList);
				Gift gift=null;
				for(int i=0;i<giftTotalList.size();i++){
					gift=giftTotalList.get(i);
					if(gift.type==1){
						giftList.add(gift);
					}
					
				}
				marketRealGridViewAdapter.updateList(giftList);
				marketRealGridViewAdapter.notifyDataSetChanged();
			}
		});
        tv3.setOnClickListener(new OnClickListener() {
	
	        @Override
	        public void onClick(View v) {
		    // TODO Auto-generated method stub
	        	popupWindow.dismiss();
				typeTv.setText("狗粮");
				giftList.removeAll(giftList);
				Gift gift=null;
				for(int i=0;i<giftTotalList.size();i++){
					gift=giftTotalList.get(i);
					if(gift.type==2){
						giftList.add(gift);
					}
					
				}
				marketRealGridViewAdapter.updateList(giftList);
				marketRealGridViewAdapter.notifyDataSetChanged();
	        }
        });
        tv4.setOnClickListener(new OnClickListener() {
        	
	        @Override
	        public void onClick(View v) {
		    // TODO Auto-generated method stub
	        	popupWindow.dismiss();
				typeTv.setText("其他");
				giftList.removeAll(giftList);
				Gift gift=null;
				for(int i=0;i<giftTotalList.size();i++){
					gift=giftTotalList.get(i);
					if(gift.type==3){
						giftList.add(gift);
					}
					
				}
				marketRealGridViewAdapter.updateList(giftList);
				marketRealGridViewAdapter.notifyDataSetChanged();
	        }
        });
	}
	   
	      @Override
	   protected void onResume() {
	   	// TODO Auto-generated method stub
	   	super.onResume();
	   	if(animal!=null){
	   		foodTv.setText(""+animal.foodNum);
	   	}
	   }

}
