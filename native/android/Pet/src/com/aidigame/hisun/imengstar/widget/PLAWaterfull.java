package com.aidigame.hisun.imengstar.widget;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.maxwin.view.XListView;
import me.maxwin.view.XListViewHeader;
import me.maxwin.view.XListView.IXListViewListener;
import me.maxwin.view.XListView.ScrollowTopListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Message;
import android.os.AsyncTask.Status;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView.BufferType;

import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.Banner;
import com.aidigame.hisun.imengstar.bean.PetPicture;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.http.json.UserImagesJson;
import com.aidigame.hisun.imengstar.huanxin.NewSmileUtils;
import com.aidigame.hisun.imengstar.ui.ActivityWebActivity;
import com.aidigame.hisun.imengstar.ui.HomeActivity;
import com.aidigame.hisun.imengstar.ui.NewShowTopicActivity;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.view.MyViewPager;
import com.aidigame.hisun.imengstar.view.StaticViewPager;
import com.aidigame.hisun.imengstar.view.MyViewPager.OnSingleTouchListener;
import com.aidigame.hisun.imengstar.widget.fragment.DiscoveryFragment;
import com.aidigame.hisun.imengstar.widget.fragment.HomeSeePictureFragment;
import com.aidigame.hisun.imengstar.R;
import com.dodola.model.DuitangInfo;
import com.dodowaterfall.Helper;
import com.dodowaterfall.widget.ScaleImageView;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.example.android.bitmapfun.util.ImageWorker.LoadCompleteListener;
import com.huewu.pla.lib.internal.PLA_AbsListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class PLAWaterfull implements IXListViewListener{
	
	private  BitmapFactory.Options options;
	private  Activity activity;
	private  LinearLayout parent;
	private  View view;
	private  int mode;
	private  Handler handler;

//	private  MyViewPager viewPager;
	private  ArrayList<Banner> banners;
//	private  RelativeLayout bannersLayout;
	private  ArrayList<ImageView> imageViews;
	private  PagerAdapter pagerAdapter;
	private  LinearLayout rootLayout;
	private  ArrayList<ImageView> points;
	HomeSeePictureFragment homeSeePictureFragment;
	public PLAWaterfull(Activity activity,LinearLayout parent,int mode,HomeSeePictureFragment homeSeePictureFragment){
		this.activity=activity;
		this.parent=parent;
		this.mode=mode;
		handler=HandleHttpConnectionException.getInstance().getHandler(activity);
		this.homeSeePictureFragment=homeSeePictureFragment;
		 padding=activity.getResources().getDimensionPixelOffset(R.dimen.one_dip)*32;
		inite();
		
		
		
	}
	private ImageFetcher mImageFetcher;
    private XListView mAdapterView = null;
    private StaggeredAdapter mAdapter = null;
    private int currentPage = 0;
    private   ContentTask task ;
    private   int currentPosition=0;
    int padding;
    private class ContentTask extends AsyncTask<String, Integer, ArrayList<DuitangInfo>> {

        private Context mContext;
        private int mType = 1;
        private int last_id=-1;
        public ContentTask(Context context, int type,int last_id) {
            super();
            mContext = context;
            mType = type;
            this.last_id=last_id;
           
            
        }

        @Override
        protected ArrayList<DuitangInfo> doInBackground(String... params) {
            try {
            	
            	  UserImagesJson json=null;
          		if(mode==4||mode==5){
          		}else{
          			json=HttpUtil.downloadPetkingdomImages(handler, last_id,mode,activity,-1);
          		}
          		
          if(json!=null&&json.petPictures!=null){
       	   PetPicture pp=null;
       	   DuitangInfo di=null;
       	  final  ArrayList<DuitangInfo> list=new ArrayList<DuitangInfo>();
       	  int count=0;
       	   for(int i=0;i<json.petPictures.size();i++){
       		   if(count>=15)break;
       		   pp=json.petPictures.get(i);
       		   di=new DuitangInfo();
       		   di.img_id=pp.img_id;
       		   di.isrc=pp.url;
       		 di.setMsg(pp.cmt);
       		   if(!list.contains(di)){
       			list.add(di);
       			count++;
       		   }
       		   
       	   }
       	   DiscoveryFragment.isRefresh=false;
       	return list;
          }	
            	return null;
//                return parseNewsJSON(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
            	  isrefreshing=false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<DuitangInfo> result) {
            if (mType == 1) {

                mAdapter.addItemTop(result);
                mAdapter.notifyDataSetChanged();
                mAdapterView.stopRefresh();
            } else if (mType == 2) {
            	 mAdapterView.stopLoadMore();
                mAdapter.addItemLast(result);
                mAdapter.notifyDataSetChanged();
            }
            mAdapterView.getMeasuredHeight();
            
            LogUtil.i("mi", "mAdapterView.getMeasuredHeight()"+mAdapterView.getMaxScrollAmount());

        }

        @Override
        protected void onPreExecute() {
        }

        public ArrayList<DuitangInfo> parseNewsJSON(String url) throws IOException {
        	ArrayList<DuitangInfo> duitangs = new ArrayList<DuitangInfo>();
            String json = "";
            if (Helper.checkConnection(mContext)) {
                try {
                    json = Helper.getStringFromUrl(url);

                } catch (IOException e) {
                    Log.e("IOException is : ", e.toString());
                    e.printStackTrace();
                    return duitangs;
                }
            }
            Log.d("MainActiivty", "json:" + json);

            try {
                if (null != json) {
                    JSONObject newsObject = new JSONObject(json);
                    JSONObject jsonObject = newsObject.getJSONObject("data");
                    JSONArray blogsJson = jsonObject.getJSONArray("blogs");

                    for (int i = 0; i < blogsJson.length(); i++) {
                        JSONObject newsInfoLeftObject = blogsJson.getJSONObject(i);
                        DuitangInfo newsInfo1 = new DuitangInfo();
                        newsInfo1.setAlbid(newsInfoLeftObject.isNull("albid") ? "" : newsInfoLeftObject.getString("albid"));
                        newsInfo1.setIsrc(newsInfoLeftObject.isNull("isrc") ? "" : newsInfoLeftObject.getString("isrc"));
                        newsInfo1.setMsg(newsInfoLeftObject.isNull("msg") ? "" : newsInfoLeftObject.getString("msg"));
                        newsInfo1.setHeight(newsInfoLeftObject.getInt("iht"));
                        duitangs.add(newsInfo1);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return duitangs;
        }
    }

    /**
     * 添加内容
     * 
     * @param pageindex
     * @param type
     *            1为下拉刷新 2为加载更多
     */
    private void AddItemToContainer(int pageindex, int type,int last_id) {
        if (task.getStatus() != Status.RUNNING) {
            String url = "http://www.duitang.com/album/1733789/masn/p/" + pageindex + "/24/";
            Log.d("MainActivity", "current url:" + url);
            ContentTask task = new ContentTask(activity, type,last_id);
//            task.execute(url);
            task.execute();
        }
    }

    public class StaggeredAdapter extends BaseAdapter {
        private Context mContext;
        public ArrayList<DuitangInfo> mInfos;
        private XListView mListView;
        public int count=0;
        public StaggeredAdapter(Context context, XListView xListView) {
            mContext = context;
            mInfos = new ArrayList<DuitangInfo>();
            mListView = xListView;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            
            ViewHolder holder;
           final DuitangInfo duitangInfo = mInfos.get(position);
              count++;
              LogUtil.i("me", "++++++++++++++++count="+count);
            if (convertView == null) {
                LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
                convertView = layoutInflator.inflate(R.layout.infos_list, null);
                
                holder = new ViewHolder();
                holder.layout=(LinearLayout)convertView.findViewById(R.id.news_list);
                holder.imageView = (/*Scale*/ImageView) convertView.findViewById(R.id.news_pic);
                holder.contentView = (TextView) convertView.findViewById(R.id.news_title);
                holder.conLinearLayout=(LinearLayout)convertView.findViewById(R.id.container_layout);
                convertView.setTag(holder);
            }
            
            holder = (ViewHolder) convertView.getTag();
           /* if(position==0||position==1){
    			LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)holder.layout.getLayoutParams();
    			
    			if(param!=null){
    				param.topMargin=padding;
    				holder.layout.setLayoutParams(param);
    				
    			}
    		}else{
               LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)holder.layout.getLayoutParams();
    			
    			if(param!=null){
    				param.topMargin=0;
    				holder.layout.setLayoutParams(param);
    			}
    		}*/
            
           /* if(position==0||position==1){
    	    	LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)holder.layout.getLayoutParams();
    	    	if(param==null){
    	    		param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    	    	}
    	    	param.topMargin=activity.getResources().getDimensionPixelSize(R.dimen.dip_38);
    	    	holder.layout.setLayoutParams(param);
    	    }else{
    	    	LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)holder.layout.getLayoutParams();
    	    	if(param==null){
    	    		param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    	    	}
    	    	param.topMargin=0;
    	    	holder.layout.setLayoutParams(param);
    	    }*/
            Spannable span = NewSmileUtils.getSmiledText(activity, duitangInfo.getMsg());
 			// 设置内容
            holder.contentView.setText(span, BufferType.SPANNABLE);
            holder.imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					/*if(ShowTopicActivity.showTopicActivity!=null){
						if(ShowTopicActivity.showTopicActivity.getBitmap()!=null){
							if(!ShowTopicActivity.showTopicActivity.getBitmap().isRecycled())
								ShowTopicActivity.showTopicActivity.getBitmap().recycle();
						}
						ShowTopicActivity.showTopicActivity.finish();
					}
					Intent intent=new Intent(activity,ShowTopicActivity.class);*/
					if(NewShowTopicActivity.newShowTopicActivity!=null){
						NewShowTopicActivity.newShowTopicActivity.recyle();
					}
					HomeActivity.showTopic=true;
					Intent intent=new Intent(activity,NewShowTopicActivity.class);
				
					PetPicture pp=new PetPicture();
					pp.img_id=(int)duitangInfo.img_id;
					pp.animal=new Animal();
					pp.animal.a_id=duitangInfo.a_id;
					pp.url=duitangInfo.isrc;
					pp.petPicture_path=duitangInfo.path;
//					pp.petPicture_path=mInfos.get(position).path;
					ArrayList<PetPicture> pictures=new ArrayList<PetPicture>();
					PetPicture p=null;
					for(int i=0;i<mInfos.size();i++){
						p=new PetPicture();
						p.img_id=(int)mInfos.get(i).img_id;
						p.animal=new Animal();
						p.animal.a_id=mInfos.get(i).a_id;
						pictures.add(p);
					}
					intent.putExtra("PetPicture",pp);
//					intent.putExtra("mode", flowTag.mode);
					activity.startActivity(intent);
				}
			});
          
            mImageFetcher.setWidth(Constants.screen_width/2);
            //
            mImageFetcher.setLoadCompleteListener(new LoadCompleteListener() {
            	DuitangInfo  d=duitangInfo;
				@Override
				public void onComplete(Bitmap bitmap) {
					// TODO Auto-generated method stub
					count++;
				    bitmap=null;
				    if(mode==2){
				    		if(count>4){
				    			PLAWaterfull.this.parent.setVisibility(View.VISIBLE);
				    			
				    		}
				    }
				    
				}
				@Override
				public void getPath(String path) {
					// TODO Auto-generated method stub
					File f=new File(path);
					for(int i=0;i<mInfos.size();i++){
						if(f.getName().contains(mInfos.get(i).isrc)){
							mInfos.get(i).path=f.getName();
						}
					}
					
				}
				
				
			});
          
            
            
            
         
            options.inSampleSize=StringUtil.getScaleByDPI(activity,duitangInfo.getIsrc());;
            mImageFetcher.setImageCache(new ImageCache(activity, new ImageCacheParams(duitangInfo.getIsrc()+"@"+Constants.screen_width/2+"w_0l.jpg")));
          /*  if(duitangInfo.getIsrc().contains("@")){
            	int a=duitangInfo.getIsrc().indexOf("@");
            	int b=duitangInfo.getIsrc().lastIndexOf("@");
            	int lenth=Integer.parseInt(duitangInfo.getIsrc().substring(a+1, b));
            	if(lenth>1024*100){
            		options.inSampleSize=4;
            	}else{
            		options.inSampleSize=StringUtil.getScaleByDPI(activity);;
            	}
            }else{
        		options.inSampleSize=StringUtil.getScaleByDPI(activity);;
        	}*/
            LogUtil.i("run", "options.inSampleSize"+options.inSampleSize+"====="+duitangInfo.getIsrc()+"@"+Constants.screen_width/2+"w_0l.jpg");
//            mImageFetcher.loadImage(/*Constants.UPLOAD_IMAGE_RETURN_URL+*/duitangInfo.getIsrc(), holder.imageView,options);
          mImageFetcher.IP=mImageFetcher.UPLOAD_THUMBMAIL_IMAGE;
            mImageFetcher.loadImage(/*Constants.UPLOAD_IMAGE_RETURN_URL+*/duitangInfo.getIsrc()+"@"+Constants.screen_width/2+"w_0l.jpg", holder.imageView,null);
            return convertView;
        }

        class ViewHolder {
            /*Scale*/ImageView imageView;
            TextView contentView;
            TextView timeView;
            LinearLayout layout,conLinearLayout;
        }

        @Override
        public int getCount() {
            return mInfos.size();
        }

        @Override
        public Object getItem(int arg0) {
            return mInfos.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        public void addItemLast(ArrayList<DuitangInfo> datas) {
        	if(datas==null)return;
        	if(mInfos!=null){
        		
        	}else{
        		mInfos=new ArrayList<DuitangInfo>();
        	}
        	 for (DuitangInfo info : datas) {
              	if(!mInfos.contains(info)){
              		mInfos.add(info);
              	}
              }
        }

        public void addItemTop(ArrayList<DuitangInfo> datas) {
        	if(datas==null)return;
        	mInfos=new ArrayList<DuitangInfo>();
            for (DuitangInfo info : datas) {
            	if(!mInfos.contains(info)){
            		mInfos.add(info);
            	}
            }
        }
    }
    public static boolean scrollShowTab=true;
    protected void inite() {
    	options=new BitmapFactory.Options();
		options.inSampleSize=StringUtil.getScaleByDPI(activity);;
		LogUtil.i("me", "图片像素压缩比例="+StringUtil.getScaleByDPI(activity));
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
	
    	
    	
    	
        view=LayoutInflater.from(activity).inflate(R.layout.act_pull_to_refresh_sample,null);
//        viewPager=(MyViewPager)view.findViewById(R.id.viewpager);
//        bannersLayout=(RelativeLayout)view.findViewById(R.id.banner_layout);
        rootLayout=(LinearLayout)view.findViewById(R.id.parent_layout);
        
        banners=new ArrayList<Banner>();
        imageViews=new ArrayList<ImageView>();
        pagerAdapter=new PagerAdapter() {
			
        	@Override
        	public int getCount() {
        		// TODO Auto-generated method stub
        		if(banners.size()==0)return 0;
        		return 1999999999;
        	}

        	@Override
        	public boolean isViewFromObject(View arg0, Object arg1) {
        		// TODO Auto-generated method stub
        		return arg0==arg1;
        	}
        	@Override
        	public void destroyItem(ViewGroup container, int position, Object object) {
        		// TODO Auto-generated method stub
        		if(imageViews.size()>0){
        			container.removeView(imageViews.get(position%imageViews.size()));
        		}
        		
        	}
        	@Override
        	public int getItemPosition(Object object) {
        		// TODO Auto-generated method stub
        		return super.getItemPosition(object);
//        		return POSITION_NONE;
        	}
        	@Override
        	public CharSequence getPageTitle(int position) {
        		// TODO Auto-generated method stub
        		return super.getPageTitle(position);
        	}
        	@Override
        	public Object instantiateItem(ViewGroup container,final int position) {
        		// TODO Auto-generated method stubI
        		
        		/*ImageView iv=null;
        		if(imageViews.size()==0){
        			iv=(ImageView)LayoutInflater.from(activity).inflate(R.layout.item_banner_iv, null);
        			imageViews.add(0, iv);
        		}else{
        			iv=imageViews.get(position%imageViews.size());
        			if(iv==null){
        				iv=(ImageView)LayoutInflater.from(activity).inflate(R.layout.item_banner_iv, null);
        			}
        			imageViews.add(position%imageViews.size(), iv);
        		}*/
        		LogUtil.i("mi", "root_parent_height="+rootLayout.getHeight()+",root_parent_width="+rootLayout.getWidth());
        		ImageView iv=(ImageView)LayoutInflater.from(activity).inflate(R.layout.item_banner_iv, null);
        		iv.setImageResource(R.drawable.empty_photo);
        		ImageFetcher imageFetcher=new ImageFetcher(activity, Constants.screen_width);
        		imageFetcher.itemUrl="banner/";
        		BitmapFactory.Options options=new BitmapFactory.Options();
        		options.inSampleSize=1;
        		
        		imageFetcher.setImageCache(new ImageCache(activity, new ImageCacheParams(banners.get(position%banners.size()).img_url)));
        		
        		if(position==0){
        			imageFetcher.setLoadCompleteListener(new LoadCompleteListener() {
						
						@Override
						public void onComplete(Bitmap bitmap) {
							// TODO Auto-generated method stub
							RelativeLayout.LayoutParams param=(RelativeLayout.LayoutParams) homeSeePictureFragment.viewPager.getLayoutParams();
						        if(param==null){
						        	param=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
						        }
						        int w=rootLayout.getWidth();
						        int h=(int)(w*1f/bitmap.getWidth()*1f*bitmap.getHeight());
						        param.width=w;
						        param.height=h;
						        homeSeePictureFragment.viewPager.setLayoutParams(param);
						        LogUtil.i("mi", "positon=0,下载玩图片root_parent_height="+rootLayout.getHeight()+",root_parent_width="+rootLayout.getWidth());
						}
						
						@Override
						public void getPath(String path) {
							// TODO Auto-generated method stub
							
						}
					});
        		}
        		imageFetcher.loadImage(banners.get(position%banners.size()).img_url, iv, options);
        		if(imageViews.size()==0){
        			imageViews.add(iv);
        		}else{
        			imageViews.add(position%imageViews.size(),iv);
        		}
        		iv.setTag(banners.get(position%banners.size()));
        		iv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(activity,ActivityWebActivity.class);
						intent.putExtra("banner", banners.get(position%banners.size()));
						activity.startActivity(intent);
					}
				});
        		container.addView(iv);
        		return iv;
        	}
		};
		homeSeePictureFragment.viewPager.setAdapter(pagerAdapter);
		homeSeePictureFragment.viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg) {
				// TODO Auto-generated method stub
				currentPosition=arg;
				int arg0=arg%points.size();
				for(int i=0;i<points.size();i++){
					if(i==arg0){
//						points.get(i).setVisibility(View.VISIBLE);
						points.get(i).setImageResource(R.drawable.point_red);
					}else{
						points.get(i).setImageResource(R.drawable.point_gray);
						if(i<=arg0){
//							points.get(i).setVisibility(View.VISIBLE);
						}else{
//							points.get(i).setVisibility(View.GONE);
						}
					}
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		homeSeePictureFragment.viewPager.setOnSingleTouchListener(new OnSingleTouchListener() {
			
			@Override
			public void onSingleTouch(int position) {
				// TODO Auto-generated method stub
				Banner banner=banners.get(position%banners.size());
				Intent intent=new Intent(activity,ActivityWebActivity.class);
				intent.putExtra("banner", banner);
				activity.startActivity(intent);
			}
		});
        

        mAdapterView = (XListView) view.findViewById(R.id.list);
        LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)mAdapterView.getLayoutParams();
        if(param==null){
        	param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        }
        mAdapterView.setLayoutParams(param);
        mAdapterView.setPullLoadEnable(true);
        
        mAdapterView.setXListViewListener(this);
        mAdapter = new StaggeredAdapter(activity, mAdapterView);
        mImageFetcher = new ImageFetcher(activity, 240);
        mImageFetcher.setLoadingImage(R.drawable.white_box);
        parent.removeAllViews();
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
//        parent.addView(view);
        mAdapterView.setAdapter(mAdapter);
       
        
mAdapterView.setScrollowTopListenenr(new ScrollowTopListener() {
			Thread thread=null;
			@Override
			public void changeTopTab(float firstChildTop) {
				// TODO Auto-generated method stub
					
				
				HomeActivity.topAlpha=HomeActivity.topAlpha+firstChildTop*0.002f;
				if(HomeActivity.topAlpha>=1.0f){
					HomeActivity.topAlpha=1.0f;
				}else if(HomeActivity.topAlpha<=0){
					HomeActivity.topAlpha=0.0f;
				}
				LogUtil.i("mi", "topTabAlpha====="+HomeActivity.topAlpha);
					setAlpha(HomeActivity.topAlpha);
				}	
			@Override
			public void onScrollStop(final int status) {
				// TODO Auto-generated method stub
				if(status==PLA_AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
					scrollShowTab=true;
					if(thread!=null){
						try {
							thread.interrupt();
							thread=null;
						} catch (Exception e2) {
							// TODO: handle exception
						}
						
					}
					thread=new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								Thread.sleep(1500);
								if(scrollShowTab){
									handler.post(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											if(scrollShowTab){
												HomeActivity.topAlpha=1.0f;
												setAlpha(HomeActivity.topAlpha);
											}
											thread=null;
										}
									});
								}
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					thread.start();
				}else{
					scrollShowTab=false;
					if(thread!=null){
						try {
							thread.interrupt();
							thread=null;
						} catch (Exception e2) {
							// TODO: handle exception
						}
						
					}
				}
				
                
			}
		});
        
        
        loadData();
       
    }
    public View getView(){
    	return view;
    }
    public XListView getXListView(){
    	return mAdapterView;
    }
   boolean showBanner=false;
    public void loadData(){
    	task=new ContentTask(activity, 1, -1);
    	handler.postAtTime(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				pullRefresh();
			}
		}, 1000);
    	
    	 
//    	 task.execute();
    	 /*new Thread(new Runnable() {
 			
 			@Override
 			public void run() {
 				// TODO Auto-generated method stub
 				
                UserImagesJson json=null;
                		if(mode==4||mode==5){
                			HttpUtil.downloadActivityImagesList(handler, -1, 0, activity,DetailActivity.detailActivity.data, mode);
                		}else{
                			json=HttpUtil.downloadPetkingdomImages(handler, -1,mode,activity,-1);
                		}
                		
                if(json!=null&&json.petPictures!=null){
             	   PetPicture pp=null;
             	   DuitangInfo di=null;
             	  final  ArrayList<DuitangInfo> list=new ArrayList<DuitangInfo>();
             	   for(int i=0;i<json.petPictures.size();i++){
             		   pp=json.petPictures.get(i);
             		   di=new DuitangInfo();
             		   di.img_id=pp.img_id;
             		   di.isrc=pp.url;
             		   di.setMsg(pp.cmt);
             		   if(!list.contains(di))
             		   list.add(di);
             	   }
             	   activity.runOnUiThread(new Runnable() {
 					
 					@Override
 					public void run() {
 						// TODO Auto-generated method stub
 						if(list.size()>0&&mAdapter.mInfos.size()>0){
							if(list.get(0).img_id==mAdapter.mInfos.get(0).img_id){
								
            		   return;
            	   }
						}
 						mAdapter.mInfos=new ArrayList<DuitangInfo>();
						mAdapter.addItemTop(list);
 		            	   mAdapter.notifyDataSetChanged();
 					}
 				});
             	   
                }
 			}
 		}).start();*/
         new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<Banner> bs=HttpUtil.bannerList(handler, activity);
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(bs==null||bs.size()==0){
							showBanner=false;
							homeSeePictureFragment.bannersLayout.setVisibility(View.GONE);
//							mAdapterView.mHeaderView.topMargin=padding;
							mAdapterView.mHeaderView.setTopMargin(32);
							LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)mAdapterView.getLayoutParams();
							if(params==null){
								params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
							}
							/*params.width=rootLayout.getWidth();
							params.height=rootLayout.getHeight();*/
//							mAdapterView.setLayoutParams(params);
							LogUtil.i("mi", "banner为空root_parent_height="+rootLayout.getHeight()+",root_parent_width="+rootLayout.getWidth());
						}else{
							
							banners=bs;
							ImageView iv=null;
							points=new ArrayList<ImageView>();
							if(bs.size()>0){
								iv=(ImageView)homeSeePictureFragment.view.findViewById(R.id.point1);
								iv.setVisibility(View.VISIBLE);
								iv.setImageResource(R.drawable.point_red);
								points.add(iv);
							}
							if(bs.size()>1){
								iv=(ImageView)homeSeePictureFragment.view.findViewById(R.id.point2);
								iv.setVisibility(View.VISIBLE);
								points.add(iv);
							}
							if(bs.size()>2){
								iv=(ImageView)homeSeePictureFragment.view.findViewById(R.id.point3);
								iv.setVisibility(View.VISIBLE);
								points.add(iv);
							}
							if(bs.size()>3){
								iv=(ImageView)homeSeePictureFragment.view.findViewById(R.id.point4);
								iv.setVisibility(View.VISIBLE);
								points.add(iv);
							}
							if(bs.size()>4){
								iv=(ImageView)homeSeePictureFragment.view.findViewById(R.id.point5);
								iv.setVisibility(View.VISIBLE);
								points.add(iv);
							}
							if(bs.size()>5){
								iv=(ImageView)homeSeePictureFragment.view.findViewById(R.id.point6);
								iv.setVisibility(View.VISIBLE);
								points.add(iv);
							}
							if(bs.size()>6){
								iv=(ImageView)homeSeePictureFragment.view.findViewById(R.id.point7);
								iv.setVisibility(View.VISIBLE);
								points.add(iv);
							}
							if(bs.size()>7){
								iv=(ImageView)homeSeePictureFragment.view.findViewById(R.id.point8);
								iv.setVisibility(View.VISIBLE);
								points.add(iv);
							}
							
							homeSeePictureFragment.viewPager.removeAllViews();
							LogUtil.i("mi", "banner不为空mAdapterView.h="+mAdapterView.getHeight()+",root_parent_width="+rootLayout.getWidth());
							padding=activity.getResources().getDimensionPixelOffset(R.dimen.one_dip)*108;
							pagerAdapter.notifyDataSetChanged();
							LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)mAdapterView.getLayoutParams();
							if(params==null){
								params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
							}
							circleHandler.postAtTime(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									showBanner=true;
									homeSeePictureFragment.bannersLayout.setVisibility(View.VISIBLE);
									
								}
							},2000);
							if(bs!=null&&bs.size()>1){
								circleHandler.sendEmptyMessageDelayed(1, 4000);
							}
							
							/*params.width=rootLayout.getWidth();
							params.height=rootLayout.getHeight();*/
//							mAdapterView.setLayoutParams(params);
							
						}
					}
				});
			}
		}).start();
    }
    Handler circleHandler=new Handler(){
    	public void handleMessage(Message msg) {
    		if(msg.what==1){
    			currentPosition++;
    			homeSeePictureFragment.viewPager.setCurrentItem(currentPosition);
    			circleHandler.sendEmptyMessageDelayed(1, 4000);
    		}
    	};
    };
    public void setAlpha(float alpha){
    	homeSeePictureFragment.topTabLayout.setAlpha(alpha);
    	if(alpha==0){
    		homeSeePictureFragment.topTabLayout.setVisibility(View.INVISIBLE);
    	}else{
    		homeSeePictureFragment.topTabLayout.setVisibility(View.VISIBLE);
    	}
    	if(showBanner){
			homeSeePictureFragment.bannersLayout.setAlpha(alpha);
			if(alpha==0){
	    		homeSeePictureFragment.bannersLayout.setVisibility(View.INVISIBLE);
	    	}else{
	    		homeSeePictureFragment.bannersLayout.setVisibility(View.VISIBLE);
	    	}
		}else{
			homeSeePictureFragment.bannersLayout.setVisibility(View.GONE);
		}
    	if(HomeActivity.homeActivity!=null){
			HomeActivity.homeActivity.bottomTabLayout.setAlpha(alpha);
			if(alpha==0){
				HomeActivity.homeActivity.bottomTabLayout.setVisibility(View.INVISIBLE);
			}else{
				HomeActivity.homeActivity.bottomTabLayout.setVisibility(View.VISIBLE);
			}
		}
    }
   boolean isrefreshing=false;
    public void pullRefresh(){
    	if(isrefreshing)return;
    	HomeActivity.topAlpha=1.0f;
    	setAlpha(HomeActivity.topAlpha);
    	mAdapterView.updateHeaderHeight(mAdapterView.mHeaderViewHeight);
    	mAdapterView.mHeaderView.setVisibility(View.VISIBLE);
    		mAdapterView.mPullRefreshing = true;
    		mAdapterView.mEnablePullRefresh=true;
			mAdapterView.mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
			if (mAdapterView.mListViewListener != null) {
				isrefreshing=true;
				mAdapterView.mListViewListener.onRefresh();
			}
		    mAdapterView.resetHeaderHeight();
    }

    @Override
    public void onRefresh() {
    	mAdapter.mInfos=new ArrayList<DuitangInfo>();
    	   mAdapter.notifyDataSetChanged();
        AddItemToContainer(++currentPage, 1,-1);

new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
               UserImagesJson json= HttpUtil.downloadPetkingdomImages(handler, -1,mode,activity,-1);
               if(json!=null&&json.petPictures!=null){
            	   PetPicture pp=null;
            	   DuitangInfo di=null;
            	  final  ArrayList<DuitangInfo> list=new ArrayList<DuitangInfo>();
            	  int count=0;
            	   for(int i=0;i<json.petPictures.size();i++){
            		   if(count>=15)break;
            		   pp=json.petPictures.get(i);
            		   di=new DuitangInfo();
            		   di.img_id=pp.img_id;
            		   di.isrc=pp.url;
            		   di.setMsg(pp.cmt);
            		   if(!list.contains(di)){
            			   list.add(di);
            			   count++;
            		   }
            		   
            	   }
            	   
            	   activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						 DiscoveryFragment.isRefresh=false;
						if(list.size()>0&&mAdapter.mInfos.size()>0){
							if(list.get(0).img_id==mAdapter.mInfos.get(0).img_id){
								 mAdapterView.stopRefresh();
								 isrefreshing=false;
            		   return;
            	            }
						}
						mAdapter.mInfos=new ArrayList<DuitangInfo>();
						mAdapter.addItemTop(list);
		            	   mAdapter.notifyDataSetChanged();
		            	   mAdapterView.stopRefresh();
		            	   isrefreshing=false;
					}
				});
            	   
               }else{
            	   isrefreshing=false;
               }
					
				
			}
		})/*.start()*/;
    }

    @Override
    public void onLoadMore() {
    	
    	int img_id=-1;
		if(mAdapter.mInfos!=null&&mAdapter.mInfos.size()>0){
			img_id=(int)mAdapter.mInfos.get(mAdapter.mInfos.size()-1).img_id;
		}
        AddItemToContainer(++currentPage, 2,img_id);
    	
new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				long img_id=-1;
			if(mAdapter.mInfos!=null&&mAdapter.mInfos.size()>0){
				img_id=mAdapter.mInfos.get(mAdapter.mInfos.size()-1).img_id;
			}
               UserImagesJson json= HttpUtil.downloadPetkingdomImages(handler,(int)img_id,mode,activity,-1);
               if(json!=null&&json.petPictures!=null){
            	   PetPicture pp=null;
            	   DuitangInfo di=null;
            	  final  ArrayList<DuitangInfo> list=new ArrayList<DuitangInfo>();
            	  int count=0;
            	   for(int i=0;i<json.petPictures.size();i++){
            		   if(count>=15)break;
            		   pp=json.petPictures.get(i);
            		   di=new DuitangInfo();
            		   di.img_id=pp.img_id;
            		   di.isrc=pp.url;
            		   di.setMsg(pp.cmt);
            		   if(!list.contains(di)){
            			   list.add(di);
            			   count++;
            		   }
            		   
            	   }
            	   activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						mAdapter.addItemLast(list);
		            	   mAdapter.notifyDataSetChanged();
		            	   mAdapterView.stopLoadMore();
					}
				});
            	   
               }
					
				
			}
		})/*.start()*/;

    }
	
	

}
