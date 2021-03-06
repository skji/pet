package com.aidigame.hisun.imengstar.adapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.aidigame.hisun.imengstar.bean.Gift;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.util.ImageUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ToneGenerator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 商城 虚拟礼物列表
 * @author admin
 *
 */
public class MarketGridViewAdapter extends BaseAdapter {
	Context context;
	List<Gift> list;
	ClickGiftListener clickGiftListener;
    public MarketGridViewAdapter(Context context,List<Gift> list){
    	this.context=context;
    	this.list=list;
    	BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
//		options.inSampleSize=4;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.noimg)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
    }
    public void updateList(List<Gift> list){
    	this.list=list;
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
	/*	if(list.size()%2!=0){
			return list.size()/2+1;
		}*/
		return list.size()/2;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.item_two_gift_box, null);
			holder=new Holder();
			
			holder.tabLayout1=(RelativeLayout)convertView.findViewById(R.id.tab_layout1);
			
			holder.giftSignRlayout1=(RelativeLayout)convertView.findViewById(R.id.gift_sign_layout1);
			holder.giftSignRlayout2=(RelativeLayout)convertView.findViewById(R.id.gift_sign_layout2);
			
			
			holder.giftIV1=(ImageView)convertView.findViewById(R.id.gift_image_iv1);
			holder.giftIV2=(ImageView)convertView.findViewById(R.id.gift_image_iv2);
			
			
			holder.giftNameTv1=(TextView)convertView.findViewById(R.id.gift_name1);
			holder.giftNameTv2=(TextView)convertView.findViewById(R.id.gift_name2);
			
			holder.giftRqTv1=(TextView)convertView.findViewById(R.id.rq_num1);
			holder.giftRqTv2=(TextView)convertView.findViewById(R.id.rq_num2);
			
			
			
			holder.giftNumTv1=(TextView)convertView.findViewById(R.id.gift_num1);
			holder.giftNumTv2=(TextView)convertView.findViewById(R.id.gift_num2);
			
			
			holder.giftPriceTv1=(TextView)convertView.findViewById(R.id.gold_num1);
			holder.giftPriceTv2=(TextView)convertView.findViewById(R.id.gold_num2);
			
			
			holder.has_gift_llayout1=(LinearLayout)convertView.findViewById(R.id.has_gift_layout1);
			holder.no_gift_llayout1=(LinearLayout)convertView.findViewById(R.id.no_gift_layout1);
			holder.has_gift_llayout2=(LinearLayout)convertView.findViewById(R.id.has_gift_layout2);
			holder.no_gift_llayout2=(LinearLayout)convertView.findViewById(R.id.no_gift_layout2);
			
			
			holder.llayout1=(LinearLayout)convertView.findViewById(R.id.layout1);
			holder.llayout2=(LinearLayout)convertView.findViewById(R.id.layout2);
			
			
			
			
			
			convertView.setTag(holder);	
		}else{
			holder=(Holder)convertView.getTag();
		}
		final Gift gift1=list.get(position*2);
		final Gift gift2=list.get(position*2+1);
		
		
		loadImage(holder.giftIV1, gift1);
		/*try {
			holder.giftIV1.setImageBitmap(BitmapFactory.decodeStream(context.getResources().getAssets().open(""+gift1.no+".png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		holder.giftNameTv1.setText(gift1.name);
		if(gift1.add_rq>0){
			holder.giftRqTv1.setText("+"+gift1.add_rq);
			holder.giftSignRlayout1.setBackgroundResource(R.drawable.gift_sign_good);
		}else{
			holder.giftRqTv1.setText(""+gift1.add_rq);
			holder.giftSignRlayout1.setBackgroundResource(R.drawable.gift_sign_bad);
		}
		
		if(gift1.boughtNum>0){
			holder.has_gift_llayout1.setVisibility(View.VISIBLE);
			holder.no_gift_llayout1.setVisibility(View.GONE);
			holder.giftNumTv1.setText(""+gift1.boughtNum);
		}else{
			holder.has_gift_llayout1.setVisibility(View.GONE);
			holder.no_gift_llayout1.setVisibility(View.VISIBLE);
			holder.giftPriceTv1.setText(""+gift1.price);
		}
		
		
		
		
		
		loadImage(holder.giftIV2, gift2);
		/*try {
			holder.giftIV2.setImageBitmap(BitmapFactory.decodeStream(context.getResources().getAssets().open(""+gift2.no+".png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		holder.giftNameTv2.setText(gift2.name);
		if(gift2.add_rq>0){
			holder.giftRqTv2.setText("+"+gift2.add_rq);
			holder.giftSignRlayout2.setBackgroundResource(R.drawable.gift_sign_good);
		}else{
			holder.giftRqTv2.setText(""+gift2.add_rq);
			holder.giftSignRlayout2.setBackgroundResource(R.drawable.gift_sign_bad);
		}
		
		if(gift2.boughtNum>0){
			holder.has_gift_llayout2.setVisibility(View.VISIBLE);
			holder.no_gift_llayout2.setVisibility(View.GONE);
			holder.giftNumTv2.setText(""+gift2.boughtNum);
		}else{
			holder.has_gift_llayout2.setVisibility(View.GONE);
			holder.no_gift_llayout2.setVisibility(View.VISIBLE);
			holder.giftPriceTv2.setText(""+gift2.price);
		}
		
		
		holder.giftIV1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(clickGiftListener!=null){
					clickGiftListener.clickGift(gift1);
				}
			}
		});
        holder.giftIV2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(clickGiftListener!=null){
					clickGiftListener.clickGift(gift2);
				}
			}
		});
		
		
		
		return convertView;
	}
	DisplayImageOptions displayImageOptions;
	public void loadImage(final ImageView icon,final Gift gift){
			
			ImageLoader imageLoader=ImageLoader.getInstance();
			imageLoader.displayImage(gift.detail_image_url, icon, displayImageOptions,new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					// TODO Auto-generated method stub
					String name="gift"+gift.no+"_";
					if(loadedImage!=null&&StringUtil.isEmpty(gift.detail_image_path)){
					   File f=new File(Constants.Picture_Root_Path+File.separator+name+".jpg");
					   if(f.exists()){
						   gift.detail_image_path=Constants.Picture_Root_Path+File.separator+name+".jpg";
					   }else{
						   String path=ImageUtil.compressImageByName(loadedImage, name);
							if(!StringUtil.isEmpty(path)){
								gift.detail_image_path=path;
							}else{
								gift.detail_image_path=ImageUtil.compressImage(loadedImage, name);
							}
					   }
					
					
					/*BitmapFactory.Options options=new BitmapFactory.Options();
					options.inSampleSize=16;
					icon.setImageBitmap(BitmapFactory.decodeFile(article.share_path, options));*/
					}
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	class Holder{
		
		RelativeLayout tabLayout1,giftSignRlayout1,giftSignRlayout2;
		ImageView giftIV1,giftIV2;
		TextView  giftNameTv1,giftRqTv1,giftNumTv1,giftPriceTv1,
		          giftNameTv2,giftRqTv2,giftNumTv2,giftPriceTv2;
		LinearLayout has_gift_llayout1,no_gift_llayout1,
		             has_gift_llayout2,no_gift_llayout2,
		             llayout1,llayout2;
		
	}
	public void setClickGiftListener(ClickGiftListener clickGiftListener){
		this.clickGiftListener=clickGiftListener;
	}
	public static interface ClickGiftListener{
		void clickGift(Gift gift);
	}

}
