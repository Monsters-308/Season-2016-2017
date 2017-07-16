package com.codeteddy.frcscout.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.codeteddy.frcscout.QRCodeActivity;
import com.codeteddy.frcscout.R;
import com.codeteddy.frcscout.utils.Constants;
import com.codeteddy.frcscout.utils.DatabaseHandler;
import com.codeteddy.frcscout.utils.LengthTextWatcher;
import com.codeteddy.frcscout.utils.QRCode;
import com.codeteddy.frcscout.utils.TeamNumberWatcher;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

/**
 * @author Alex
 * @version 1.0.0
 *          Created by Alex on 24.02.2017.
 */

public class CommentFragment extends Fragment {

    private View root;
    private Spinner selectTeam;
    private Spinner selectEvent;
    private EditText messge;
    private Button button;
    private DatabaseHandler handler;
    private String output;

    public CommentFragment() {
        //Keep the empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_comment, container, false);

        handler = new DatabaseHandler(getActivity());
        selectEvent = (Spinner)root.findViewById(R.id.selectEvent);
        selectTeam = (Spinner)root.findViewById(R.id.selectTeam);
        messge = (EditText)root.findViewById(R.id.input_message);
        button = (Button)root.findViewById(R.id.buttom_Submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String event = selectEvent.getSelectedItem().toString();
                String team = selectTeam.getSelectedItem().toString();
                String message = messge.getText().toString();

                if(event.equals("Please select")){
                    Snackbar.make(root, "Please select an event", Snackbar.LENGTH_SHORT).show();
                }
                else if(team.equals("Please select")){
                    Snackbar.make(root, "Please select a team", Snackbar.LENGTH_SHORT).show();
                }
                else if(message.replace(";", "").equals("")){
                    Snackbar.make(root, "Please enter a message", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    //Generate QR-Code
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    String name = sharedPreferences.getString("user_name", "Alexander Kaschta");

                    output = commentToProtocol(name, event, team, message);

                    final Intent intent = new Intent(getActivity(), QRCodeActivity.class);
                    intent.putExtra("PROTOCOL", output);
                    intent.putExtra("TYPE", "COMMENT");


                    final ProgressDialog dialog = new ProgressDialog(getActivity());
                    dialog.setMessage("Uploading data");
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setIndeterminate(true);
                    dialog.show();

                    //Execute here
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("type", "COMMENT");
                    params.put("qrcode", output);

                    client.post("http://codeteddy.de/scout/api/create_entry.php", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String s = new String(responseBody);
                            System.out.println(s);
                            if (s.contains("1")) {
                                handler.addQRCode(new QRCode(0, "COMMENT", output, true));
                                //System.out.println("DÖNER");
                                dialog.dismiss();
                                getActivity().startActivity(intent);
                            } else {
                                handler.addQRCode(new QRCode(0, "COMMENT", output, false));
                                dialog.dismiss();
                                new MaterialDialog.Builder(getActivity())
                                        .title("An error happened")
                                        .content("Server error")
                                        .positiveText("Okay")
                                        .onAny(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                getActivity().startActivity(intent);
                                            }
                                        })
                                        .show();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            error.printStackTrace();
                            dialog.dismiss();
                            handler.addQRCode(new QRCode(0, "COMMENT", output, false));
                            System.out.println("An error happened");

                            new MaterialDialog.Builder(getActivity())
                                    .title("An error happened")
                                    .content("Connection error")
                                    .positiveText("Okay")
                                    .onAny(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            getActivity().startActivity(intent);
                                        }
                                    })
                                    .show();
                        }

                    });

                    selectEvent.setSelection(0);
                    selectTeam.setSelection(0);
                    messge.setText("");

                }

            }
        });


        return root;
    }


    private String commentToProtocol(String name, String event, String team, String message) {
        return "FRC2017;COMMENT;" + event.replace(": ", ";") + ";" + name + ";"+  team + ":" + message + ";";
    }

}
