package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Topic;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.widget.ShowFocusTopics;

public class OtherUserTopicActivity extends Activity implements OnClickListener{
	ImageView imageView1,imageView2,imageView3,
    imageView4,imageView5,imageView6,
    imageView7;
    TextView tv3,tv2,tv1;
    Topic topic;
    LinearLayout bottomLinearLayout1,bottomLinearLayout2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_other_user_topic);
		initView();
		initListener();
	}
	private void initView() {
		// TODO Auto-generated method stub
		imageView1=(ImageView)findViewById(R.id.imageView1);
		imageView2=(ImageView)findViewById(R.id.imageView2);
		imageView3=(ImageView)findViewById(R.id.imageView3);
		imageView4=(ImageView)findViewById(R.id.imageView4);
		imageView5=(ImageView)findViewById(R.id.imageView5);
		imageView6=(ImageView)findViewById(R.id.imageView6);
		imageView7=(ImageView)findViewById(R.id.imageView7);
		tv1=(TextView)findViewById(R.id.textView1);
		tv2=(TextView)findViewById(R.id.textView2);
		tv3=(TextView)findViewById(R.id.textView3);
		bottomLinearLayout1=(LinearLayout)findViewById(R.id.bottom_linearlayout1);
		bottomLinearLayout2=(LinearLayout)findViewById(R.id.bottom_linearlayout2);
		topic=(Topic)getIntent().getSerializableExtra("topic");
		showTopics();
//		imageView5.setImageResource();//�����Ա�
//		tv1.setText(topic.user.nickName);//�����ǳ�
//		tv2.setText(topic.user.race);//��������
//		tv3.setText(topic.user.age);//��������
		
	}
	private void initListener() {
		// TODO Auto-generated method stub
		imageView1.setOnClickListener(this);
		imageView2.setOnClickListener(this);
		imageView4.setOnClickListener(this);
		imageView6.setOnClickListener(this);
		imageView7.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.imageView1:
			this.finish();
			break;
		case R.id.imageView2:
			
			break;
		case R.id.imageView4:
			addOrRemoveFocus();
			break;
		case R.id.imageView6:
			
			break;
		case R.id.imageView7:
			
			break;
		}
	}
	private void addOrRemoveFocus() {
		// TODO Auto-generated method stub
		View view=LayoutInflater.from(this).inflate(R.layout.item_add_or_remove_focus, null);
		bottomLinearLayout2.removeAllViews();
		bottomLinearLayout2.setVisibility(View.VISIBLE);
		bottomLinearLayout2.addView(view);
		TextView add=(TextView)view.findViewById(R.id.textView1);
		TextView cancel=(TextView)view.findViewById(R.id.textView2);
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	    cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bottomLinearLayout2.setVisibility(View.INVISIBLE);
			}
		});
	}
	private void showTopics() {
		// TODO Auto-generated method stub
//		imageView6.setImageResource();
//		imageView7.setImageResource();
/*		View view=LayoutInflater.from(this).inflate(R.layout.item_user_topics, null);
		bottomLinearLayout1.removeAllViews();
		bottomLinearLayout1.addView(view);
		TextView pictures=(TextView)view.findViewById(R.id.textView1);
		TextView focus=(TextView)view.findViewById(R.id.textView3);
		TextView fans=(TextView)view.findViewById(R.id.textView5);
		LinearLayout listViewLinearLayout=(LinearLayout)view.findViewById(R.id.listview_linearLayout);*/
		ShowFocusTopics showFocusTopics=new ShowFocusTopics(this, bottomLinearLayout1,2);
	}

}
