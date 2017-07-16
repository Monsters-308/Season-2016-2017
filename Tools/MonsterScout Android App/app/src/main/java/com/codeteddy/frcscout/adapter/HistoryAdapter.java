package com.codeteddy.frcscout.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.codeteddy.frcscout.QRCodeActivity;
import com.codeteddy.frcscout.R;
import com.codeteddy.frcscout.utils.DatabaseHandler;
import com.codeteddy.frcscout.utils.QRCode;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * @author Alex
 *         Created by Alex on 10.03.2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private ArrayList<QRCode> codes;
    private Context context;
    private DatabaseHandler handler;

    public HistoryAdapter(Context context) {
        handler = new DatabaseHandler(context);
        this.context = context;
        codes = handler.getAllQRCodes();
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);

        return new HistoryViewHolder(itemView);
    }

    public void update() {
        codes.clear();
        codes = handler.getAllQRCodes();
    }

    public void remove(int index) {
        codes.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        final QRCode code = codes.get(position);
        holder.title.setText(code.getId() + ". " + code.getType());
        holder.subtitle.setText(code.getQrcode());
        if (code.isUploaded()) {
            holder.uploaded.setTextColor(Color.parseColor("#8BC34A"));
            holder.uploaded.setText("Synced");
        } else {
            holder.uploaded.setTextColor(Color.parseColor("#F44336"));
            holder.uploaded.setText("Not synced");
        }
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start new Activity
                Intent intent = new Intent(context, QRCodeActivity.class);
                intent.putExtra("PROTOCOL", code.getQrcode());
                intent.putExtra("TYPE", code.getType());
                context.startActivity(intent);
            }
        });

        holder.root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (code.isUploaded()) {
                    //It is already uploaded
                } else {
                    //Try to re-upload it
                    //Toast.makeText(context, "Ready to upload", Toast.LENGTH_SHORT).show();

                    final ProgressDialog dialog = new ProgressDialog(context);
                    dialog.setMessage("Uploading data");
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setIndeterminate(true);
                    dialog.show();

                    //Execute here
                    final AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    String type;
                    if(code.getType().startsWith("MATCH"))
                        type = "MATCH";
                    else
                        type = code.getType();
                    params.put("type", type);
                    params.put("qrcode", code.getQrcode());

                    client.post("http://codeteddy.de/scout/api/create_entry.php", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String s = new String(responseBody);
                            System.out.println(s);
                            if (s.contains("1")) {
                                handler.updateQRCode(new QRCode(code.getId(), code.getType(), code.getQrcode(), true));
                                update();
                                notifyDataSetChanged();
                                dialog.dismiss();
                            } else {
                                dialog.dismiss();
                                new MaterialDialog.Builder(context)
                                        .title("An error happened")
                                        .content("Server error")
                                        .positiveText("Okay")
                                        .onAny(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                //Do nothing
                                            }
                                        })
                                        .show();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            dialog.dismiss();
                            System.out.println("An error happened");

                            new MaterialDialog.Builder(context)
                                    .title("An error happened")
                                    .content("Connection error")
                                    .positiveText("Okay")
                                    .onAny(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                        }
                                    })
                                    .show();
                        }

                    });

                }

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return codes.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        public TextView title, subtitle, uploaded;
        public View root;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            title = (TextView) itemView.findViewById(R.id.title);
            subtitle = (TextView) itemView.findViewById(R.id.subtext);
            uploaded = (TextView) itemView.findViewById(R.id.uploaded);
        }
    }
}
