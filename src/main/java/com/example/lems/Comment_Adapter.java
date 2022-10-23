package com.example.lems;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Comment_Adapter extends BaseAdapter implements OnClickListener{
    private Context mContext;
    private Activity mActivity;
    private ArrayList<com.example.lems.Comment_Item> arr;
    private int pos;
    private MainActivity ma;
    //	private Typeface myFont;
    public Comment_Adapter(Context mContext, Activity mActivity, MainActivity mc, ArrayList<com.example.lems.Comment_Item> arr_item) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.arr = arr_item;
        this.ma = mc;
//		myFont = Typeface.createFromAsset(mContext.getAssets(), "BareunDotum.ttf");
    }
    @Override
    public int getCount() {
        return arr.size();
    }
    @Override
    public Object getItem(int position) {
        return arr.get(position);
    }
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            int res = 0;
            LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.activity_comment_item, null);
        }
        pos = position;
        if(arr.size() != 0){
            TextView ci_nickname_text = (TextView)convertView.findViewById(R.id.ci_nickname_text);
            ci_nickname_text.setText(arr.get(pos).getNickname());
            TextView ci_content_text = (TextView)convertView.findViewById(R.id.ci_content_text);
            ci_content_text.setText(arr.get(pos).getContent());
            TextView ci_date_text = (TextView)convertView.findViewById(R.id.ci_date);
            ci_date_text.setText(arr.get(pos).getDate());
            Button ci_delete_btn = (Button)convertView.findViewById(R.id.ci_delete_btn);
            ci_delete_btn.setOnClickListener(this);
            ci_delete_btn.setTag(pos+"");

        }
        return convertView;
    }
    public void onClick(View v){
        final int tag = Integer.parseInt(v.getTag().toString());
        switch(v.getId()){
            case R.id.ci_delete_btn:
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(mActivity);
                alertDlg.setPositiveButton("삭제", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick( DialogInterface dialog, int which ) {
                        //ma.deleteArr(tag);
                        Toast.makeText(mContext, "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDlg.setNegativeButton("취소", null);
                alertDlg.setTitle("댓글 삭제");
                alertDlg.setMessage("정말 삭제 하시겠습니까?");
                alertDlg.show();
                break;
        }
    }
}