package example.jackf.testbranchio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.BranchShortLinkBuilder;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AskForEmail();
            }
        });

        textView = (TextView) findViewById(R.id.userTextView);
        setTitle("Welcome");
        userName = getIntent().getStringExtra(LoginActivity.EXTRA_USER_NAME);
        if (userName != null) {
            textView.setText(userName);
        } else {
            textView.setText("Unknown user");
        }
    }

    private void AskForEmail() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Share the app");
        alertDialog.setMessage("Enter E-mail");

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setText("@zappoint.com");
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Send",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String email = input.getText().toString();
                        onClickEmail(MainActivity.this, email);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private void onClickEmail(Context context, String email) {
        String downloadLink = GenerateBranchUrl(context, email);
        String body = "Recommend the app.\n\nPlease click the line to download\n" + downloadLink;

        String uriText = "mailto:" + email +
                "?subject=" + Uri.encode("Share with you") +
                "&body=" + Uri.encode(body);

        Uri uri = Uri.parse(uriText);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(uri);
        context.startActivity(intent);
    }

    private String GenerateBranchUrl(Context context, String email) {
        BranchShortLinkBuilder shortUrlBuilder = new BranchShortLinkBuilder(context)
                .addTag("testbranchio")
                .setChannel("email")
                .setFeature("feature1")
                .setStage("1")
                .addParameters("share with", email)
                .addParameters("share from", userName);

        // Get URL Asynchronously
        /*
        shortUrlBuilder.generateShortUrl(new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error != null) {
                    Log.e("Branch Error", "Branch create short url failed. Caused by -" + error.getMessage());
                } else {
                    Log.i("Branch", "Got a Branch URL " + url);
                }
            }
        });
        */
        // OR Get the URL synchronously
        String shortUrl = shortUrlBuilder.getShortUrl();
        Log.i("Branch", "Got a Branch URL " + shortUrl);
        return shortUrl;
    }
}
