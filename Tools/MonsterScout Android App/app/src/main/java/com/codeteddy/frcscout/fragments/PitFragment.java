package com.codeteddy.frcscout.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import cz.msebera.android.httpclient.Header;

/**
 * @author Alex
 * Created by Alex on 09.03.2017.
 */

public class PitFragment extends Fragment {

    private View root;
    private Button submitButton;
    private EditText editTextComment;
    private Spinner selectEvent;
    private Spinner selectTeam;
    private Spinner selectDriveType;
    private Spinner selectAmountOfWheels;
    private Spinner selectDesign;
    private Spinner selectGearsGround;
    private Spinner selectBallsGround;
    private Spinner selectShootHigh;
    private Spinner selectShootLow;
    private Spinner selectShootGear;
    private Spinner selectClimbing;
    private Spinner selectProblems;
    private Spinner selectOwnRanking;
    private DatabaseHandler handler;
    private String output;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_pit, container, false);

        handler = new DatabaseHandler(getActivity());
        submitButton = (Button)root.findViewById(R.id.submit);
        editTextComment = (EditText)root.findViewById(R.id.inputComment);
        selectEvent = (Spinner)root.findViewById(R.id.selectEvent);
        selectTeam = (Spinner)root.findViewById(R.id.selectTeam);
        selectDriveType = (Spinner)root.findViewById(R.id.selectDriveTrain);
        selectAmountOfWheels = (Spinner)root.findViewById(R.id.selectWheels);
        selectDesign = (Spinner)root.findViewById(R.id.selectDesign);
        selectGearsGround = (Spinner)root.findViewById(R.id.selectGearsGround);
        selectBallsGround = (Spinner)root.findViewById(R.id.selectBallsGround);
        selectShootHigh = (Spinner)root.findViewById(R.id.selectScoreHigh);
        selectShootLow = (Spinner)root.findViewById(R.id.selectScoreLow);
        selectShootGear = (Spinner)root.findViewById(R.id.selectScoreGears);
        selectClimbing = (Spinner)root.findViewById(R.id.selectClimbing);
        selectProblems = (Spinner)root.findViewById(R.id.selectProblems);
        selectOwnRanking = (Spinner)root.findViewById(R.id.selectRanking);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectEvent.getSelectedItem().toString().equals("Please select")){
                    Snackbar.make(root, "Please select an event", Snackbar.LENGTH_SHORT).show();
                }
                else if(selectTeam.getSelectedItem().toString().equals("Please select")){
                    Snackbar.make(root, "Please select a team", Snackbar.LENGTH_SHORT).show();
                }
                else if(selectDriveType.getSelectedItem().toString().equals("Please select")){
                    Snackbar.make(root, "Please select a drive train type", Snackbar.LENGTH_SHORT).show();
                }
                else if(selectAmountOfWheels.getSelectedItem().toString().equals("Please select")){
                    Snackbar.make(root, "Please select an amount of wheels", Snackbar.LENGTH_SHORT).show();
                }
                else if(selectDesign.getSelectedItem().toString().equals("Please select")){
                    Snackbar.make(root, "Please select a robot design", Snackbar.LENGTH_SHORT).show();
                }
                else if(selectBallsGround.getSelectedItem().toString().equals("Please select")){
                    Snackbar.make(root, "Please select if they can pickup balls from the ground", Snackbar.LENGTH_SHORT).show();
                }
                else if(selectGearsGround.getSelectedItem().toString().equals("Please select")){
                    Snackbar.make(root, "Please select if they can pickup gears from the ground", Snackbar.LENGTH_SHORT).show();
                }
                else if(selectShootHigh.getSelectedItem().toString().equals("Please select")){
                    Snackbar.make(root, "Please select if they can score high", Snackbar.LENGTH_SHORT).show();
                }
                else if(selectShootLow.getSelectedItem().toString().equals("Please select")){
                    Snackbar.make(root, "Please select if they can score low", Snackbar.LENGTH_SHORT).show();
                }
                else if(selectShootGear.getSelectedItem().toString().equals("Please select")){
                    Snackbar.make(root, "Please select if they can score gears", Snackbar.LENGTH_SHORT).show();
                }
                else if(selectClimbing.getSelectedItem().toString().equals("Please select")){
                    Snackbar.make(root, "Please select if they can climb", Snackbar.LENGTH_SHORT).show();
                }
                else if(selectProblems.getSelectedItem().toString().equals("Please select")){
                    Snackbar.make(root, "Please select if they had problems", Snackbar.LENGTH_SHORT).show();
                }
                else if(selectOwnRanking.getSelectedItem().toString().equals("Please select")){
                    Snackbar.make(root, "Please select how they rank themselves", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "Ready to go", Toast.LENGTH_SHORT).show();

                    String event = selectEvent.getSelectedItem().toString().replace(": ", ";")+ ";";

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    String name = sharedPreferences.getString("user_name", "Alexander Kaschta") + ";";
                    String team = selectTeam.getSelectedItem().toString() + ";";
                    String driveTrain = String.valueOf(selectDriveType.getSelectedItemPosition() - 1) + ";";
                    String wheels;
                    if(selectAmountOfWheels.getSelectedItem().toString().equals("More")){
                        wheels = "10;";
                    }
                    else{
                        wheels = String.valueOf(selectAmountOfWheels.getSelectedItem().toString()) + ";";
                    }
                    String design = String.valueOf(selectDesign.getSelectedItemPosition() - 1) + ";";
                    String pickupBalls = getYesNoToNumber(selectBallsGround.getSelectedItem().toString());
                    String pickupGears = getYesNoToNumber(selectGearsGround.getSelectedItem().toString());

                    String scoreHigh = getYesNoToNumber(selectShootHigh.getSelectedItem().toString());
                    String scoreLow = getYesNoToNumber(selectShootLow.getSelectedItem().toString());
                    String scoreGear = getYesNoToNumber(selectShootGear.getSelectedItem().toString());
                    String climbing = getYesNoToNumber(selectClimbing.getSelectedItem().toString());
                    String problems = getYesNoToNumber(selectProblems.getSelectedItem().toString());
                    String rank = String.valueOf(selectOwnRanking.getSelectedItemPosition() - 1)+ ";";
                    String comment;
                    if(editTextComment.getText().toString().trim().equals("")){
                        comment = "NO_COMMENT;";
                    }
                    else {
                        comment = editTextComment.getText().toString() + ";";
                    }

                    output = "FRC2017;PIT;" + event + name + team + driveTrain + wheels + design + pickupBalls + pickupGears + scoreHigh + scoreLow + scoreGear + climbing + problems + rank + comment;

                    final Intent intent = new Intent(getActivity(), QRCodeActivity.class);
                    System.out.println(output);
                    intent.putExtra("PROTOCOL", output);
                    intent.putExtra("TYPE", "PIT");

                    final ProgressDialog dialog = new ProgressDialog(getActivity());
                    dialog.setMessage("Uploading data");
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setIndeterminate(true);
                    dialog.show();

                    //Execute here
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("type", "PIT");
                    params.put("qrcode", output);

                    client.post("http://codeteddy.de/scout/api/create_entry.php", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String s = new String(responseBody);
                            System.out.println(s);
                            if (s.contains("1")) {
                                handler.addQRCode(new QRCode(0, "PIT", output, true));
                                //System.out.println("DÖNER");
                                dialog.dismiss();
                                getActivity().startActivity(intent);
                            } else {
                                handler.addQRCode(new QRCode(0, "PIT", output, false));
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
                            handler.addQRCode(new QRCode(0, "PIT", output, false));
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

                    //Handle reset afterwards
                    editTextComment.setText("");
                    selectEvent.setSelection(0);
                    selectTeam.setSelection(0);
                    selectDriveType.setSelection(0);
                    selectAmountOfWheels.setSelection(0);
                    selectDesign.setSelection(0);
                    selectBallsGround.setSelection(0);
                    selectGearsGround.setSelection(0);
                    selectShootGear.setSelection(0);
                    selectShootLow.setSelection(0);
                    selectShootHigh.setSelection(0);
                    selectOwnRanking.setSelection(0);
                    selectClimbing.setSelection(0);
                    selectProblems.setSelection(0);


                }
            }
        });

        return root;
    }


    private String getYesNoToNumber(String input){
        if(input.equals("Yes"))
            return "1;";
        else
            return "0;";
    }
}
