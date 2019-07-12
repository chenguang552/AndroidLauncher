package com.leecg.laucher;
import android.widget.*;
import android.view.*;
import android.content.*;
import java.util.*;

public class MyContactAdapter extends BaseAdapter
{

	Context context;
	List<MyContacts> contacts = new ArrayList<MyContacts>();
	public MyContactAdapter(List<MyContacts> contacts,Context context){
		this.contacts = contacts;
		this.context = context;
	}
	
	@Override
	public int getCount()
	{
		// TODO: Implement this method
		return contacts.size();
	}

	@Override
	public Object getItem(int p1)
	{
		// TODO: Implement this method
		return contacts.get(p1);
	}

	@Override
	public long getItemId(int p1)
	{
		// TODO: Implement this method
		return p1;
	}

	@Override
	public View getView(int p1, View p2, ViewGroup p3)
	{
		// TODO: Implement this method
		Holder holder = null;
		if(p2 == null){
			p2 = LayoutInflater.from(context).inflate(R.layout.contect ,null);
			holder = new Holder();
			holder.ico = p2.findViewById(R.id.iv);
			holder.name = p2.findViewById(R.id.tv);
			p2.setTag(holder);
		}else{
			holder = (Holder) p2.getTag();
		}
		
		holder.ico.setImageDrawable(contacts.get(p1).getIco());
		holder.name.setText(contacts.get(p1).getStrName());
		return p2;
	}
	
	static class Holder{
		ImageView ico;
		TextView name;
	}
	
}
