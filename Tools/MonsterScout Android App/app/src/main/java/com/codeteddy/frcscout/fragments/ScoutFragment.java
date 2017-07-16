package com.codeteddy.frcscout.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.SignalStrength;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.codeteddy.frcscout.QRCodeActivity;
import com.codeteddy.frcscout.R;
import com.codeteddy.frcscout.utils.DatabaseHandler;
import com.codeteddy.frcscout.utils.QRCode;
import com.codeteddy.frcscout.utils.Uploader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * @author Alexander Kaschta
 *         Created by Alex on 15.02.2017.
 */

public class ScoutFragment extends Fragment {

    private View root;
    private Button buttonSubmit;
    private EditText editTextComment;
    private CheckBox checkBoxBalls;
    private CheckBox checkBoxGears;
    private CheckBox checkBoxLine;
    private Spinner spinnerTeam;
    private Spinner spinnerEvent;
    private Spinner spinnerMatchType;
    private Spinner spinnerMatch;
    private Spinner spinnerAlliance;
    private Spinner spinnerStartingPosition;
    private Spinner spinnerHighAccuracy;
    private Spinner spinnerLowAccuracy;
    private Spinner spinnerGears;
    private Spinner spinnerClimbing;
    private Spinner spinnerFinished;
    private Spinner spinnerStrategy;
    private DatabaseHandler handler;
    public String output;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_scout, container, false);

        //Initialize components
        handler = new DatabaseHandler(getActivity());

        buttonSubmit = (Button) root.findViewById(R.id.submit);
        editTextComment = (EditText) root.findViewById(R.id.inputComment);
        checkBoxBalls = (CheckBox) root.findViewById(R.id.toggleBalls);
        checkBoxGears = (CheckBox) root.findViewById(R.id.toggleGear);
        checkBoxLine = (CheckBox) root.findViewById(R.id.toggleLine);
        spinnerEvent = (Spinner) root.findViewById(R.id.selectEvent);
        spinnerMatchType = (Spinner) root.findViewById(R.id.selectMatchType);
        spinnerMatch = (Spinner) root.findViewById(R.id.selectMatchNumber);
        spinnerAlliance = (Spinner) root.findViewById(R.id.selectAlliance);
        spinnerTeam = (Spinner) root.findViewById(R.id.selectTeam);
        spinnerStartingPosition = (Spinner) root.findViewById(R.id.selectStartingPosition);
        spinnerHighAccuracy = (Spinner) root.findViewById(R.id.selectHighGoal);
        spinnerLowAccuracy = (Spinner) root.findViewById(R.id.selectLowGoal);
        spinnerGears = (Spinner) root.findViewById(R.id.selectGears);
        spinnerClimbing = (Spinner) root.findViewById(R.id.selectClimbing);
        spinnerFinished = (Spinner) root.findViewById(R.id.selectFinish);
        spinnerStrategy = (Spinner) root.findViewById(R.id.selectStrategy);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Handle any click on this button

                if (spinnerEvent.getSelectedItem().toString().equals("Please select")) {
                    Snackbar.make(root, "Please select an event", Snackbar.LENGTH_SHORT).show();
                } else if (spinnerMatchType.getSelectedItem().toString().equals("Please select")) {
                    Snackbar.make(root, "Please select a match type", Snackbar.LENGTH_SHORT).show();
                } else if (spinnerMatch.getSelectedItem().toString().equals("Please select")) {
                    Snackbar.make(root, "Please select a match number", Snackbar.LENGTH_SHORT).show();
                } else if (spinnerAlliance.getSelectedItem().toString().equals("Please select")) {
                    Snackbar.make(root, "Please select an alliance", Snackbar.LENGTH_SHORT).show();
                } else if (spinnerTeam.getSelectedItem().toString().equals("Please select")) {
                    Snackbar.make(root, "Please select a team", Snackbar.LENGTH_SHORT).show();
                } else if (spinnerStartingPosition.getSelectedItem().toString().equals("Please select")) {
                    Snackbar.make(root, "Please select an starting position", Snackbar.LENGTH_SHORT).show();
                } else if (spinnerHighAccuracy.getSelectedItem().toString().equals("Please select")) {
                    Snackbar.make(root, "Please select an high goal accuracy", Snackbar.LENGTH_SHORT).show();
                } else if (spinnerLowAccuracy.getSelectedItem().toString().equals("Please select")) {
                    Snackbar.make(root, "Please select an low goal accuracy", Snackbar.LENGTH_SHORT).show();
                } else if (spinnerGears.getSelectedItem().toString().equals("Please select")) {
                    Snackbar.make(root, "Please select how many gears got delivered", Snackbar.LENGTH_SHORT).show();
                } else if (spinnerClimbing.getSelectedItem().toString().equals("Please select")) {
                    Snackbar.make(root, "Please select if they climbed", Snackbar.LENGTH_SHORT).show();
                } else if (spinnerFinished.getSelectedItem().toString().equals("Please select")) {
                    Snackbar.make(root, "Please select if they finished the game", Snackbar.LENGTH_SHORT).show();
                } else {
                    if (spinnerStrategy.getSelectedItem().toString().equals("Please select")) {
                        Snackbar.make(root, "Please select a strategy", Snackbar.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(getActivity(), "Ready to go", Toast.LENGTH_SHORT).show();

                        //Generate string
                        String event = spinnerEvent.getSelectedItem().toString().replace(": ", ";") + ";";
                        String matchtype;
                        if (spinnerMatchType.getSelectedItemId() == 1) {
                            matchtype = "MATCH_QUALIFICATION";
                        } else if (spinnerMatchType.getSelectedItemId() == 2) {
                            matchtype = "MATCH_QUARTERFINALS";
                        } else if (spinnerMatchType.getSelectedItemId() == 3) {
                            matchtype = "MATCH_SEMIFINALS";
                        } else{
                            matchtype = "MATCH_FINALS";
                        }
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        String name = sharedPreferences.getString("user_name", "Alexander Kaschta") + ";";
                        String matchNumber = spinnerMatch.getSelectedItem().toString() + ";";
                        String alliance = spinnerAlliance.getSelectedItem().toString() + ";";
                        String team = spinnerTeam.getSelectedItem().toString() + ";";
                        String startingPosition = String.valueOf(spinnerStartingPosition.getSelectedItemPosition() - 1) + ";";
                        String autoBalls;
                        if (checkBoxBalls.isChecked()) {
                            autoBalls = "1;";
                        } else {
                            autoBalls = "0;";
                        }
                        String autoGears;
                        if (checkBoxGears.isChecked()) {
                            autoGears = "1;";
                        } else {
                            autoGears = "0;";
                        }
                        String autoLine;
                        if (checkBoxLine.isChecked()) {
                            autoLine = "1;";
                        } else {
                            autoLine = "0;";
                        }

                        String highGoal = String.valueOf(spinnerHighAccuracy.getSelectedItemPosition() - 1) + ";";
                        String lowGoal = String.valueOf(spinnerLowAccuracy.getSelectedItemPosition() - 1) + ";";
                        String gears = String.valueOf(spinnerGears.getSelectedItem().toString()) + ";";

                        String climbing = getYesNoToNumber(spinnerClimbing.getSelectedItem().toString());
                        String finished = getYesNoToNumber(spinnerFinished.getSelectedItem().toString());
                        String strategy = String.valueOf(spinnerStrategy.getSelectedItemPosition() - 1) + ";";

                        String comment;
                        if (editTextComment.getText().toString().trim().equals("")) {
                            comment = "NO_COMMENT;";
                        } else {
                            comment = editTextComment.getText().toString() + ";";
                        }

                        output = "FRC2017;" + matchtype + ";" + event + name + matchNumber + alliance + team + startingPosition + autoBalls + autoGears + autoLine + highGoal + lowGoal + gears + climbing + finished + strategy + comment;

                        System.out.println(output);

                        final Intent intent = new Intent(getActivity(), QRCodeActivity.class);
                        intent.putExtra("PROTOCOL", output);
                        intent.putExtra("TYPE", matchtype);

                        final ProgressDialog dialog = new ProgressDialog(getActivity());
                        dialog.setMessage("Uploading data");
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.setIndeterminate(true);
                        dialog.show();

                        //Execute here
                        AsyncHttpClient client = new AsyncHttpClient();
                        RequestParams params = new RequestParams();
                        params.put("type", "MATCH");
                        params.put("qrcode", output);

                        final String finalMatchtype = matchtype;
                        client.post("http://codeteddy.de/scout/api/create_entry.php", params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String s = new String(responseBody);
                                System.out.println(s);
                                if (s.contains("1")) {
                                    handler.addQRCode(new QRCode(0, finalMatchtype, output, true));
                                    //System.out.println("DÖNER");
                                    dialog.dismiss();
                                    getActivity().startActivity(intent);
                                } else {
                                    handler.addQRCode(new QRCode(0, finalMatchtype, output, false));
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
                                handler.addQRCode(new QRCode(0, finalMatchtype, output, false));
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


                        //Clear the whole thing
                        editTextComment.setText("");
                        checkBoxLine.setSelected(false);
                        checkBoxGears.setSelected(false);
                        checkBoxBalls.setSelected(false);

                        spinnerEvent.setSelection(0);
                        spinnerTeam.setSelection(0);
                        spinnerMatch.setSelection(0);
                        spinnerAlliance.setSelection(0);
                        spinnerStrategy.setSelection(0);
                        spinnerStartingPosition.setSelection(0);
                        spinnerLowAccuracy.setSelection(0);
                        spinnerHighAccuracy.setSelection(0);
                        spinnerGears.setSelection(0);
                        spinnerClimbing.setSelection(0);
                        spinnerFinished.setSelection(0);

                    }
                }
            }
        });

        return root;
    }


    private String getYesNoToNumber(String input) {
        if (input.equals("Yes"))
            return "1;";
        else
            return "0;";
    }


}
