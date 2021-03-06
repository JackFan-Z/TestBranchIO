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
import android.text.Html;
import android.text.Spanned;
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
    private String shareFrom;

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
        shareFrom = getIntent().getStringExtra(LoginActivity.EXTRA_SHARE_FROM);
        if (shareFrom != null) {
            TextView shareFromTextView = (TextView) findViewById(R.id.shareFrom);
            shareFromTextView.setText("recommended by\n" + shareFrom);
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
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Send",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String email = input.getText().toString();
//                        onClickEmail(MainActivity.this, email);
                        startMailClient(MainActivity.this, email);
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
        String body = "Recommend the app.\n\nPlease click the link to download/install\n" + downloadLink;

        String uriText = "mailto:" + email +
                "?subject=" + Uri.encode("Recommend test app") +
                "&body=" + Uri.encode(body);

        Uri uri = Uri.parse(uriText);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(uri);
        context.startActivity(intent);
    }

    private void startMailClient(Context context, String email) {
        //String downloadLink = GenerateBranchUrl(context, email);
        //String body = "Recommend the test app\n\nPlease click the link to download/install\n" + downloadLink;
        String body = "John,\n\nit's nice to meet you.\nHere's my Zappoint card for you.\nhttp://www.zappoint.com\n\nJack";

        // Try to open e-mail client with Html.
        // But it only works on Gmail
        // http://stackoverflow.com/questions/11447445/android-how-to-send-html-email-and-force-android-to-send-it-through-g-mail-not?rq=1
//        StringBuilder htmBuilder = new StringBuilder();
//        htmBuilder.append("<p><b>Nice to meet you<b><p>");
//        htmBuilder.append("<img src=\"http://files.parsetfss.com/88643df2-0c5d-4c03-a2d1-17143a6bf20e/tfss-14adac46-f042-487e-ab0f-422a2ea8d013-pic\"/>");
//        htmBuilder.append("<small><p>More content</p></small>");
//        Spanned body = Html.fromHtml(htmBuilder.toString());


        Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", email, null));
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Follow-up from Jack");
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        Uri imageUri = Uri.parse("file://" + "/sdcard/Pictures/hi_msg.jpg");
        emailIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        //shareIntent.setType("image/jpeg");

        context.startActivity(emailIntent);
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
