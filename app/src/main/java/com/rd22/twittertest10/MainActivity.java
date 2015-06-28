package com.rd22.twittertest10;

import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import twitter4j.Status;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import android.content.Intent;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "your key here";
    private static final String TWITTER_SECRET = "your secret here";
    private TwitterLoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 16) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                System.out.println("The user is successfully logged in");
                //code her
                String secret = result.data.getAuthToken().secret;
                String token = result.data.getAuthToken().token;
                System.out.println("secret : "+result.data.getAuthToken().secret);

                ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(true)
                        .setOAuthConsumerKey(TWITTER_KEY)
                        .setOAuthConsumerSecret(TWITTER_SECRET)
                        .setOAuthAccessToken(token)
                        .setOAuthAccessTokenSecret(secret);
                TwitterFactory tf = new TwitterFactory(cb.build());

             //   AccessToken accessToken = new AccessToken(result.data.getAuthToken().token, result.data.getAuthToken().secret);
                twitter4j.Twitter twitter = tf.getInstance();
                System.out.println("got the access token and the secret");

                try{
                    User user =  twitter.createFriendship("github");
                    System.out.println("you are friends with " + user.getName());
//                    Status status = twitter.updateStatus("This is a test status");
//                    System.out.println("It Looks like you are friends");

                   /* List<Status> statuses = twitter.getHomeTimeline();
                    System.out.println("Showing home timeline.");
                    for (Status status : statuses) {
                        System.out.println(status.getUser().getName() + ":" +
                                status.getText());
                    }*/
                }
                catch(twitter4j.TwitterException te){
                    System.out.println("There was an error in the request");
                    te.printStackTrace();
                }
                catch(Exception e){
                    System.out.println("There was an unknown exception ");
                    e.printStackTrace();
                }

                System.out.println("Finished with the app");
            }

            @Override
            public void failure(TwitterException exception) {
                System.out.println("The user failed to log in");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
