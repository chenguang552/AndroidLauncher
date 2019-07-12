package com.leecg.laucher;
import android.graphics.drawable.*;

public class MyContacts
{
	String strName;
	String phoneNumber;
	Drawable ico;

	public void setIco(Drawable ico)
	{
		this.ico = ico;
	}

	public Drawable getIco()
	{
		return ico;
	}
	
	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public void setStrName(String strName)
	{
		this.strName = strName;
	}

	public String getStrName()
	{
		return strName;
	}
}
