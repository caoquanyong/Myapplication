package com.example.myapplication.Device;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bluetoothlibrary.entity.Peripheral;
import com.example.myapplication.R;

import java.util.ArrayList;

public class LeDeviceListAdapter extends BaseAdapter {

	// Adapter for holding devices found through scanning.

	private ArrayList<Peripheral> mLeDevices;
	private LayoutInflater mInflator;
	private Activity mContext;


	public LeDeviceListAdapter(Activity c ,ArrayList<Peripheral> showDevice) {
		super();
		mContext = c;
		mLeDevices = showDevice;
		mInflator = mContext.getLayoutInflater();
	}
	public Peripheral getDevice(int position) {
		return mLeDevices.get(position);
	}

	public void clear() {
		mLeDevices.clear();
	}

	@Override
	public int getCount() {
		return mLeDevices.size();
	}

	@Override
	public Object getItem(int i) {
		return mLeDevices.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;
		// General ListView optimization code.
		if (view == null) {
//			view = mInflator.inflate(R.layout.listitem_device, null);
//			viewHolder = new ViewHolder();
//			viewHolder.deviceAddress = (TextView) view
//					.findViewById(R.id.device_address);
//			viewHolder.deviceName = (TextView) view
//					.findViewById(R.id.device_name);
//			viewHolder.type = (TextView) view
//					.findViewById(R.id.type);
//			viewHolder.sn = (TextView) view
//					.findViewById(R.id.sn);
			view = mInflator.inflate(R.layout.newadapteritem, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView=view.findViewById(R.id.icon);
			viewHolder.deviceName=view.findViewById(R.id.name);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		Peripheral device = mLeDevices.get(i);
		final String deviceName = device.getPreipheralMAC();

		final String model=device.getModel();
		if(model.equals("WBP202")){
			viewHolder.deviceName.setText("血压仪");
			viewHolder.imageView.setImageResource(R.mipmap.ic_launcher);
		}else if(model.equals("M70C")){
			viewHolder.deviceName.setText("血氧仪");
			viewHolder.imageView.setImageResource(R.mipmap.ic_launcher);
		}

//		if (deviceName != null && deviceName.length() > 0)
////			viewHolder.deviceName.setText(deviceName);
////		else
////			viewHolder.deviceName.setText(R.string.unknown_device);
////		viewHolder.deviceAddress.setText(device.getBluetooth());
////		viewHolder.type.setText(device.getModel());
////		viewHolder.sn.setText(device.getPreipheralSN());
		return view;
	}


	class ViewHolder {

		ImageView imageView;
		TextView deviceName;
//		TextView deviceName;
//		TextView deviceAddress;
//		TextView type;
//		TextView sn;
	}
}
