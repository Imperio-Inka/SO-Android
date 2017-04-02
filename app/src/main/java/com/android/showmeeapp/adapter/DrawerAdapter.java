package com.android.showmeeapp.adapter;
/**
 * Created by kushahuja on 7/3/16.
 */


import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.showmeeapp.R;
import com.android.showmeeapp.model.UserDetailModel.UserModel;
import com.android.showmeeapp.pojo.NavDrawerItem;


public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {
	Context mContext;
	private static final int TYPE_HEADER = 0;
	private static final int TYPE_ITEM = 1;
	private View.OnClickListener clickListener;
	private ArrayList<NavDrawerItem> lstTitle;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		int Holderid;

		TextView txtTitle, txtUserName, txtUserLocation;
		ImageView imgvwProfile, imgvwIcon;

		public ViewHolder(View itemView, int ViewType) {

			super(itemView);
			Log.e("Drawer  ::: ", "Drawer Initiated");

			if (ViewType == TYPE_ITEM) {
				txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
				imgvwIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
				Holderid = 1;
			} else {
				imgvwProfile = (ImageView) itemView.findViewById(R.id.img_profile);
				String image = UserModel.getInstance().getServices().getGoogle().getPicture();
				image.replace("\\", "");
				new DownloadImageTask((ImageView) itemView.findViewById(R.id.img_profile)).execute(image);
				System.out.println(image);
//				imgvwProfile.setImageDrawable();
				txtUserName = (TextView) itemView.findViewById(R.id.txt_user_name);
				txtUserName.setText(UserModel.getInstance().getServices().getGoogle().getGivenName()+" "+UserModel.getInstance().getServices().getGoogle().getFamilyName());
				txtUserLocation = (TextView) itemView.findViewById(R.id.txt_user_location);
//				txtUserName.setText(UserModel.getInstance().getServices().getGoogle().);


				Holderid = 0;
			}
		}
	}


	public DrawerAdapter(Context context, ArrayList<NavDrawerItem> lsttitle, View.OnClickListener clickListener) {
		this.mContext = context;
		this.lstTitle = lsttitle;
		this.clickListener = clickListener;
	}

	@Override
	public DrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		if (viewType == TYPE_ITEM) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_drawer_list_item, parent, false);
			ViewHolder vhItem = new ViewHolder(v, viewType);
			return vhItem;

		} else if (viewType == TYPE_HEADER) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_toolbar, parent, false);
			ViewHolder vhHeader = new ViewHolder(v, viewType);
			return vhHeader;
		}
		return null;

	}

	@Override
	public void onBindViewHolder(DrawerAdapter.ViewHolder holder, int position) {
		if (holder.Holderid == 1) {
			try {
				holder.txtTitle.setText(lstTitle.get(position - 1).getTitle());
				holder.imgvwIcon.setImageResource(lstTitle.get(position - 1).getIcon());
				holder.imgvwIcon.setOnClickListener(clickListener);
				holder.imgvwIcon.setTag(position);
				holder.txtTitle.setOnClickListener(clickListener);
				holder.txtTitle.setTag(position);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {

		}
	}

	@Override
	public int getItemCount() {
		return lstTitle.size() + 1;
	}

	@Override
	public int getItemViewType(int position) {
		if (isPositionHeader(position))
			return TYPE_HEADER;

		return TYPE_ITEM;
	}

	private boolean isPositionHeader(int position) {
		return position == 0;
	}


	private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}
}


