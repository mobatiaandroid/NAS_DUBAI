package com.mobatia.naisapp.activities.canteen_new.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.mobatia.naisapp.BuildConfig;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen_new.CartActivity;
import com.mobatia.naisapp.activities.canteen_new.ConfirmedOrderActivity;
import com.mobatia.naisapp.activities.canteen_new.model.CartItemDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.ConfirmedDetailModel;
import com.mobatia.naisapp.activities.login.LoginActivity;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class ConfirmedDetailRecyclerAdapter extends RecyclerView.Adapter<ConfirmedDetailRecyclerAdapter.MyViewHolder> implements URLConstants, StatusConstants, JSONConstants, IntentPassValueConstants, NameValueConstants {

    private Context mContext;
    private ArrayList<ConfirmedDetailModel> mCartDetailArrayList;
    String dept;

    String delivery_date="";
    String ordered_user_type="";
    String student_id="";
    String parent_id="";
    String staff_id="";
    String item_id="";
    String quantity="";
    String canteen_cart_id="";
    String price="";

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView cartitemname,cartitemprice,monthTxt,totalItemsTxt;
        ImageView cartitemimage,remove_itemcart;
        RelativeLayout bgLinear;
        ElegantNumberButton cartitemcount;
        LinearLayout linearlayout;
        TextView statusTxt;
        public MyViewHolder(View view) {
            super(view);

            cartitemname = (TextView) view.findViewById(R.id.cartitemname);
            cartitemprice = (TextView) view.findViewById(R.id.cartitemprice);
            cartitemimage = (ImageView) view.findViewById(R.id.cartitemimage);
            remove_itemcart = (ImageView) view.findViewById(R.id.remove_itemcart);
            cartitemcount = view.findViewById(R.id.cartitemcount);
            linearlayout = view.findViewById(R.id.linearlayout);
            statusTxt = view.findViewById(R.id.statusTxt);
            totalItemsTxt = view.findViewById(R.id.totalItemsTxt);

        }
    }


    public ConfirmedDetailRecyclerAdapter(Context mContext, ArrayList<ConfirmedDetailModel> mCartDetailArrayList, String delivery_date, String ordered_user_type, String student_id, String parent_id, String staff_id) {
        this.mContext = mContext;
        this.mCartDetailArrayList = mCartDetailArrayList;
        this.delivery_date = delivery_date;
        this.ordered_user_type = ordered_user_type;
        this.student_id = student_id;
        this.parent_id = parent_id;
        this.staff_id = staff_id;

    }

    @Override
    public ConfirmedDetailRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_conifrmed_detail, parent, false);

        return new ConfirmedDetailRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ConfirmedDetailRecyclerAdapter.MyViewHolder holder, int position) {

        holder.cartitemprice.setText(mCartDetailArrayList.get(position).getPrice()+" AED");
        holder.cartitemname.setText(mCartDetailArrayList.get(position).getItem_name());
        if (mCartDetailArrayList.get(position).getItem_status().equalsIgnoreCase("0"))
        {
            holder.statusTxt.setText("Canceled");
            holder.totalItemsTxt.setVisibility(View.VISIBLE);
            holder.cartitemcount.setVisibility(View.GONE);
            holder.remove_itemcart.setVisibility(View.GONE);
            holder.totalItemsTxt.setText(String.valueOf(mCartDetailArrayList.get(position).getItem_total())+" Items");
            holder.linearlayout.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
            holder.statusTxt.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        else if (mCartDetailArrayList.get(position).getItem_status().equalsIgnoreCase("1"))
        {
            holder.statusTxt.setText("Active");
            holder.totalItemsTxt.setVisibility(View.GONE);
            holder.cartitemcount.setVisibility(View.VISIBLE);
            holder.remove_itemcart.setVisibility(View.VISIBLE);
            holder.totalItemsTxt.setText(String.valueOf(mCartDetailArrayList.get(position).getItem_total())+" Items");
            holder.linearlayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            holder.statusTxt.setTextColor(mContext.getResources().getColor(R.color.orange_circle));
        }
        else
        {
            holder.statusTxt.setText("Delivered");
            holder.totalItemsTxt.setVisibility(View.VISIBLE);
            holder.cartitemcount.setVisibility(View.GONE);
            holder.remove_itemcart.setVisibility(View.GONE);
            holder.totalItemsTxt.setText(String.valueOf(mCartDetailArrayList.get(position).getItem_total())+" Items");
            holder.linearlayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            holder.statusTxt.setTextColor(mContext.getResources().getColor(R.color.sign_password));
        }
        if (!mCartDetailArrayList.get(position).getItem_image().equals("")) {

            Picasso.with(mContext).load(AppUtils.replace(mCartDetailArrayList.get(position).getItem_image().toString())).placeholder(R.drawable.canteendefaultitem).fit().into(holder.cartitemimage);
        }
        else
        {

            holder.cartitemimage.setImageResource(R.drawable.canteendefaultitem);
        }
        holder.cartitemcount.setNumber(mCartDetailArrayList.get(position).getQuantity().trim());


        holder.cartitemcount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                System.out.println("New Value"+newValue);
                System.out.println("New Value clicked position"+position);
                canteen_cart_id=mCartDetailArrayList.get(position).getId();
                quantity=String.valueOf(newValue);
                price=mCartDetailArrayList.get(position).getPrice();
                System.out.println("New Value canteen_cart_id"+canteen_cart_id);
                if (AppUtils.isNetworkConnected(mContext)) {
                    getCartUpdate(URL_CANTEEN_CONFIRMED_ORDER_EDIT,canteen_cart_id,quantity);
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                }
            }
        });
        holder.remove_itemcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDialogAlertLogout(mContext, "Confirm", "Do you want to cancel the order?", R.drawable.questionmark_icon, R.drawable.round,mCartDetailArrayList.get(position).getId());



            }
        });
    }


    @Override
    public int getItemCount() {
        return mCartDetailArrayList.size();
    }
    public  void getCartUpdate(final String URL,String canteen_cart_id,String quantity)
    {
        try {
            String deviceBrand = android.os.Build.MANUFACTURER;
            String deviceModel = Build.MODEL;
            String osVersion = android.os.Build.VERSION.RELEASE;
            String devicename=deviceBrand+" "+deviceModel+" "+osVersion;
            String version= BuildConfig.VERSION_NAME;
            final VolleyWrapper manager = new VolleyWrapper(URL);
//            stud_id = studentsModelArrayList.get(0).getmId();
            String[] name = {JTAG_ACCESSTOKEN,"canteen_preorder_item_id","quantity","portal","device_type","device_name","app_version","user_type","parent_id","student_id"};
            String[] value = {PreferenceManager.getAccessToken(mContext),canteen_cart_id,quantity,"1","2",devicename,version,"1",PreferenceManager.getUserId(mContext),""};
            System.out.println("Values ::::" + PreferenceManager.getAccessToken(mContext) + "hfhgfhdfghd::::" + PreferenceManager.getStaffId(mContext));
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    Log.e("responseSuccess uItem", successResponse);
                    String responsCode = "";
                    if (successResponse != null) {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            if (rootObject.optString(JTAG_RESPONSE) != null) {
                                responsCode = rootObject.optString(JTAG_RESPONSECODE);
                                if (responsCode.equals(RESPONSE_SUCCESS)) {
                                    JSONObject respObject = rootObject.getJSONObject(JTAG_RESPONSE);
                                    String statusCode = respObject.optString(JTAG_STATUSCODE);
                                    if (statusCode.equals(STATUS_SUCCESS))
                                    {
                                        ConfirmedOrderActivity.getConfirmedOrderList(URL_CANTEEN_CONFIRMED_ORDER_ITEM,ordered_user_type,student_id,parent_id,staff_id);

                                    }
                                    else {
                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Cannot continue. Please try again later", R.drawable.exclamationicon, R.drawable.round);

                                    }

                                } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getCartUpdate(URL,canteen_cart_id,quantity);

                                } else if (responsCode.equals(RESPONSE_ERROR)) {
//								CustomStatusDialog(RESPONSE_FAILURE);
                                    //Toast.makeText(mContext,"Failure",Toast.LENGTH_SHORT).show();
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Cannot continue. Please try again later", R.drawable.exclamationicon, R.drawable.round);

                                }
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Cannot continue. Please try again later", R.drawable.exclamationicon, R.drawable.round);

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Cannot continue. Please try again later", R.drawable.exclamationicon, R.drawable.round);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public  void getCartCancel(final String URL,String canteen_cart_id)
    {
        try {
            String deviceBrand = android.os.Build.MANUFACTURER;
            String deviceModel = Build.MODEL;
            String osVersion = android.os.Build.VERSION.RELEASE;
            String devicename=deviceBrand+" "+deviceModel+" "+osVersion;
            String version= BuildConfig.VERSION_NAME;
            final VolleyWrapper manager = new VolleyWrapper(URL);
//            stud_id = studentsModelArrayList.get(0).getmId();
            String[] name = {JTAG_ACCESSTOKEN,"canteen_preorder_item_id","portal","device_type","device_name","app_version","user_type","parent_id","student_id"};
            String[] value = {PreferenceManager.getAccessToken(mContext),canteen_cart_id,"1","2",devicename,version,"1",PreferenceManager.getUserId(mContext),""};
            System.out.println("Values ::::" + PreferenceManager.getAccessToken(mContext) + "hfhgfhdfghd::::" + PreferenceManager.getStaffId(mContext));
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    Log.e("responseSuccess uItem", successResponse);
                    String responsCode = "";
                    if (successResponse != null) {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            if (rootObject.optString(JTAG_RESPONSE) != null) {
                                responsCode = rootObject.optString(JTAG_RESPONSECODE);
                                if (responsCode.equals(RESPONSE_SUCCESS)) {
                                    JSONObject respObject = rootObject.getJSONObject(JTAG_RESPONSE);
                                    String statusCode = respObject.optString(JTAG_STATUSCODE);

                                    ConfirmedOrderActivity.getConfirmedOrderList(URL_CANTEEN_CONFIRMED_ORDER_ITEM,ordered_user_type,student_id,parent_id,staff_id);

                                } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getCartCancel(URL,canteen_cart_id);

                                } else if (responsCode.equals(RESPONSE_ERROR)) {
//								CustomStatusDialog(RESPONSE_FAILURE);
                                    //Toast.makeText(mContext,"Failure",Toast.LENGTH_SHORT).show();
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Cannot continue. Please try again later", R.drawable.exclamationicon, R.drawable.round);

                                }
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Cannot continue. Please try again later", R.drawable.exclamationicon, R.drawable.round);

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Cannot continue. Please try again later", R.drawable.exclamationicon, R.drawable.round);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public  void showDialogAlertLogout( Context activity, String msgHead, String msg, int ico,int bgIcon,String id)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_layout);
        ImageView icon = (ImageView) dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView textHead = (TextView) dialog.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCartCancel(URL_CANTEEN_CONFIRMED_ORDER_ITEM_CELL_CANCEL,id);
                dialog.dismiss();
            }
        });
        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
