package com.leecg.laucher;

import android.app.*;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.net.*;
import android.os.*;
import android.support.v4.view.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.view.View.*;

public class MainActivity extends Activity {
	TextView phoneNumTextView;
	String phoneNumber = "";
	ViewPager viewPager;
	ArrayList<View> pageView;
	GridView gvContacts;
	List<MyContacts> lMyContacts;
	MyContactAdapter myAdapter ;
	MyDataBase myDataBase;
	Button addContactButton;
	boolean addButtonFlag = false;
	PopInputWindow popInputWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		// 若没有则创建一个数据库
		myDataBase = new MyDataBase(MainActivity.this,"laucher",2);
		lMyContacts = new ArrayList<MyContacts>();
		viewPager = findViewById(R.id.mainViewPager);
		
		// 载入pagerview分页
		LayoutInflater inflater =getLayoutInflater();
        View view1 = inflater.inflate(R.layout.call, null);
        View view2 = inflater.inflate(R.layout.member, null);

		pageView =new ArrayList<View>();
		pageView.add(view1);
		pageView.add(view2);
		
		PagerAdapter pagerAdapter=new PagerAdapter(){
			
			@Override
            //获取当前窗体界面数
            public int getCount() {
                // TODO Auto-generated method stub
                return pageView.size();
            }

            @Override
            //判断是否由对象生成界面
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0==arg1;
            }
            //使从ViewGroup中移出当前View
            public void destroyItem(View arg0, int arg1, Object arg2) {
                ((ViewPager) arg0).removeView(pageView.get(arg1));
            }

            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            public Object instantiateItem(View arg0, int arg1){
                ((ViewPager)arg0).addView(pageView.get(arg1));
				if(arg1 == 0){
					//判断，若为call，则实例化textview
					phoneNumTextView = findViewById(R.id.phoneNumber);
					phoneNumTextView.setText(phoneNumber);
					//phoneNumTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
				}
				if(arg1 == 1 ){
					gvContacts = findViewById(R.id.mgv);
					addContactButton = findViewById(R.id.addContact);
					addContactButton.setOnLongClickListener(
						new OnLongClickListener(){
							@Override
							public boolean onLongClick(View p1)
							{
								// TODO: Implement this method
								if(addButtonFlag == false){
									addContactButton.setTextColor(0xffffffff);
									addButtonFlag = true;
								}else{
									addContactButton.setTextColor(0x00000000);
									addButtonFlag = false;
								}
								return true;
							}
					});
					
					addContactButton.setOnClickListener(
						new OnClickListener(){

							@Override
							public void onClick(View p1)
							{
								// TODO: Implement this method
								if(addButtonFlag){
									
									
									popInputWindow = new PopInputWindow(MainActivity.this,
										new OnClickListener(){

											@Override
											public void onClick(View p1)
											{
												// TODO: Implement this method
												MyContacts myContacts = new MyContacts();
												myContacts.setIco(getResources().getDrawable(R.drawable.cai));
			
												myContacts.setStrName(popInputWindow.text_name.getText().toString().trim());
				
												myContacts.setPhoneNumber(popInputWindow.text_mobile.getText().toString().trim());
			
												if(insertContact(myDataBase,  myContacts) == 0){
				
													lMyContacts.add(myContacts);
			
												}
									
												myAdapter.notifyDataSetChanged();
		
												
									
									
												gvContacts.setAdapter(myAdapter);
												
												popInputWindow.dismiss();
											}
		
									});
									
									popInputWindow.showAtLocation(findViewById(R.id.addContact),Gravity.CENTER, 0, 0);
								}
								//addButtonFlag = false;
								//addContactButton.setTextColor(0x00000000);
							}			
					});
					//从数据库取联系人
					lMyContacts.clear();
					initContactsView(myDataBase,lMyContacts);
					//gvContacts.setLayoutParams(new GridView.LayoutParams( 80,80));
					//Context context = null;
					//MyContactAdapter
					myAdapter = new MyContactAdapter(lMyContacts, MainActivity.this);
					gvContacts.setAdapter(myAdapter);

					gvContacts.setOnItemClickListener(
							new AdapterView.OnItemClickListener(){
							@Override
							public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
							{
								// TODO: Implement this method
								phoneNumber = lMyContacts.get(p3).getPhoneNumber();
								
								Intent intent = new Intent(Intent.ACTION_CALL);
 
								Uri data = Uri.parse("tel:" + phoneNumber);
  
								intent.setData(data);
 
								startActivity(intent);
								
								phoneNumber = "";
							}
					});
					
					gvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

							@Override
							public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
							{
								// TODO: Implement this method
								final int itemId = p3;
								PopupMenu popupMenu = new PopupMenu(MainActivity.this,p2);
							
								popupMenu.getMenuInflater().inflate(R.menu.popmenu, popupMenu.getMenu()); 
    
								
								popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
									
									@Override
									public boolean onMenuItemClick(MenuItem item) {

										
										SQLiteDatabase dbDelete = myDataBase.getWritableDatabase();

										dbDelete.delete("mycontact","name='"+lMyContacts.get(itemId).getStrName()+"' and phone='"+lMyContacts.get(itemId).getPhoneNumber()+"'",null);

										dbDelete.close();
										//从数据库取联系人
										
										//List<MyContacts> lMyContacts = new ArrayList<MyContacts>();
									
										lMyContacts.clear();
										initContactsView(myDataBase,lMyContacts);
										//MyContactAdapter myAdapter = new MyContactAdapter(lMyContacts,MainActivity.this);	
										
										myAdapter.notifyDataSetChanged();
										
										gvContacts.setAdapter(myAdapter);
										
										Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();

										return false;
									}
								});
								
								popupMenu.show();
								return true;
							}
							
					});
				}
                return pageView.get(arg1);
            }
		};
		
		viewPager.setAdapter(pagerAdapter);
		
		viewPager.setCurrentItem(0);
		
		
		
		
		
    }
	public void initContactsView(MyDataBase myDataBase,List<MyContacts> lMyContacts){
		SQLiteDatabase db = myDataBase.getReadableDatabase();
	
		Cursor cursor=db.query("mycontact",new String[]{"*"}, null, null, null, null, null);
		
		for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
			MyContacts mMyContact = new MyContacts();
			
			mMyContact.setStrName(cursor.getString(1));
	
			mMyContact.setPhoneNumber(cursor.getString(2));
		
			mMyContact.setIco(getResources().getDrawable(R.drawable.cai));
	
			lMyContacts.add(mMyContact);
		}
		
		cursor.close();
		
		db.close();
	}
	
	public int insertContact(MyDataBase myDataBase,MyContacts myContacts){
		SQLiteDatabase db = myDataBase.getWritableDatabase();
		ContentValues value = new ContentValues();
		
		value.put("name",myContacts.getStrName());
		value.put("phone",myContacts.getPhoneNumber());
		value.put("image","null");
		value.put("flag",1);
		long lRet = db.insert("mycontact",null,value);
		if(lRet == -1){
			Toast.makeText(MainActivity.this, "添加联系人失败", Toast.LENGTH_SHORT).show();
			return -1;
		}
		
		db.close();
		return 0;
	}
	
	public void oneNumClick1(View view){
		phoneNumber += "1";
		phoneNumTextView.setText(phoneNumber);
	}
	public void oneNumClick2(View view){
		phoneNumber += "2";
		phoneNumTextView.setText(phoneNumber);
	}
	public void oneNumClick3(View view){
		phoneNumber += "3";
		phoneNumTextView.setText(phoneNumber);
	}
	public void oneNumClick4(View view){
		phoneNumber += "4";
		phoneNumTextView.setText(phoneNumber);
	}
	public void oneNumClick5(View view){
		phoneNumber += "5";
		phoneNumTextView.setText(phoneNumber);
	}
	public void oneNumClick6(View view){
		phoneNumber += "6";
		phoneNumTextView.setText(phoneNumber);
	}
	public void oneNumClick7(View view){
		phoneNumber += "7";
		phoneNumTextView.setText(phoneNumber);
	}
	public void oneNumClick8(View view){
		phoneNumber += "8";
		phoneNumTextView.setText(phoneNumber);
	}
	public void oneNumClick9(View view){
		phoneNumber += "9";
		phoneNumTextView.setText(phoneNumber);
	}
	public void oneNumClick0(View view){
		phoneNumber += "0";
		phoneNumTextView.setText(phoneNumber);
	}
	
	public void oneNumClickSharp(View view){
		phoneNumber += "#";
		phoneNumTextView.setText(phoneNumber);
	}
	
	public void oneNumClickDelete(View view){
		if(phoneNumber.length() == 0)
			return;
		phoneNumber = phoneNumber.substring(0,phoneNumber.length() - 1);
		phoneNumTextView.setText(phoneNumber);
	}
	public void oneNumClickCall(View view){
		Intent intent = new Intent(Intent.ACTION_CALL);
 
		Uri data = Uri.parse("tel:" + phoneNumber);
  
		intent.setData(data);
 
		startActivity(intent);
		
		phoneNumber = "";
		
		phoneNumTextView.setText("");
	}
	
}
