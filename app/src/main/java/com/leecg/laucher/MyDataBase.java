package com.leecg.laucher;
import android.content.*;
import android.database.sqlite.*;

public class MyDataBase extends SQLiteOpenHelper{
	public MyDataBase(Context context,
										String databaseName,
										int databaseVersion){
      	 super(context,databaseName,
	   				null,databaseVersion);
 	}
	@Override
	public void onCreate(SQLiteDatabase p1)
	{
		// TODO: Implement this method
		p1.execSQL("create table mycontact(_id integer primary key autoincrement,name varchar(64),phone varchar(32),image varchar(256),flag int)");//执行创建表的sql语句
	}

	@Override
	public void onUpgrade(SQLiteDatabase p1, int p2, int p3)
	{
		// TODO: Implement this method
	}
	
}
