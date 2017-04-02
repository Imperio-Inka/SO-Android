
package com.android.showmeeapp.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.android.showmeeapp.R;
import com.android.showmeeapp.adapter.DrawerAdapter;
import com.android.showmeeapp.pojo.NavDrawerItem;
import com.android.showmeeapp.ui.fragments.HomeFragment;
import com.android.showmeeapp.ui.fragments.MyTagsFragment;
import com.android.showmeeapp.ui.fragments.SettingFragment;
import com.android.showmeeapp.util.MeteorOAuthServices;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.db.memory.InMemoryDatabase;

public class OldMainActivity extends AppCompatActivity implements MeteorCallback, View.OnClickListener {
    Button connectionTest;
    Button getUser;
    Button calendarList;
    Button calendarSync;
    Button calendarInit;
    Button eventsList;
    WebView browser;
    private Meteor mMeteor;
    private GoogleApiClient client;
    //   		  credentialSecret":"79f4o-9oKlW2fsYvl4u-EakLQFNXf3UADsjsOs9fl85
    //            credentialToken":"YCE4ggWJ0n7h7ZnEOHCxSAoJDjqqCOaAYNq3x5hVpBJpNkZX8bCJYQLu4jZzSjEB
    String credentialSecret;
    String credentialToken;

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private Context mContext;
    public static DrawerLayout mDrawerLayout;                                              // Declaring DrawerLayout
    public static RecyclerView mRecyclerView;
    private ActionBarDrawerToggle mDrawerToggle;      // Declaring Action Bar Drawer Toggle
    private String[] navMenuTitles;                                          // Declaring Adapter For Recycler View
    private ArrayList<NavDrawerItem> navDrawerItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_home);
        mContext = this;
        init();
    }

    private void init() {
        navDrawerItems = new ArrayList<NavDrawerItem>();
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        // adding nav drawer items to array
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], R.drawable.ic_home));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], R.drawable.ic_communities));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], R.drawable.ic_pages));

        setDrawer();

        // enable logging of internal events for the library
        Meteor.setLoggingEnabled(true);
//        connectionTest = (Button) findViewById(R.id.connection_test);
//        calendarSync = (Button) findViewById(R.id.calendar_sync);
//        getUser = (Button) findViewById(R.id.get_user);
//        calendarInit = (Button) findViewById(R.id.calendar_init);
//        calendarList = (Button) findViewById(R.id.calendar_list);
//        eventsList = (Button) findViewById(R.id.event_list);
//        connectionTest.setOnClickListener(this);
//        calendarSync.setOnClickListener(this);
//        calendarInit.setOnClickListener(this);
//        calendarList.setOnClickListener(this);
//        getUser.setOnClickListener(this);
//        eventsList.setOnClickListener(this);
        // create a new instance
        mMeteor = new Meteor(this, "wss://www.loopcowstudio.com/websocket", new InMemoryDatabase());

        // register the callback that will handle events and receive messages
        mMeteor.addCallback(this);

        // establish the connection
        System.out.print("Calling connect");

        mMeteor.connect();


//                mMeteor.call("calendars.list", new ResultListener() {
//                    @Override
//                    public void onSuccess(String result) {
//                        Log.d("Result CAlendar list", " result is  : " + mMeteor.getUserId() + result);
//
//                    }
//
//                    @Override
//                    public void onError(String error, String reason, String details) {
//
//                    }
//                });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setDrawer() {
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.Adapter mAdapter = new DrawerAdapter(mContext, navDrawerItems, OldMainActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        }; // Drawer Toggle Object Made
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        //Set Fragment for product
            displayView(0);

    }


    private void displayView(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new MyTagsFragment();
                break;
            case 2:
                fragment = new SettingFragment();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.frame_container, fragment).commit();

            //setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mRecyclerView);
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://im.delight.android.ddp.examples/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://im.delight.android.ddp.examples/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onClick(View v) {

		int id = v.getId();
        switch (id) {
			case R.id.imgIcon:
			case R.id.txtTitle:
                int position = Integer.parseInt(v.getTag().toString());
                displayView(position);
                System.out.println("position : "+position);

                break;
//            case R.id.connection_test:
//                mMeteor.call("conn.test", new ResultListener() {
//                    @Override
//                    public void onSuccess(String result) {
//                        Log.d("Result", result);
//                    }
//
//                    @Override
//                    public void onError(String error, String reason, String details) {
//                        Log.d("Result", error);
//
//                    }
//                });
//
//                break;
//            case R.id.get_user:
//                mMeteor.call("users.currentUser", new ResultListener() {
//                    @Override
//                    public void onSuccess(String result) {
//                        Log.d("Result", " result is  : " + mMeteor.getUserId() + result);
//
//                    }
//
//                    @Override
//                    public void onError(String error, String reason, String details) {
//                        Log.d("Result", error);
//
//                    }
//                });
//
//
//                break;
//            case R.id.calendar_list:
//                String[] school = {"SCU"};
//
//                mMeteor.call("users.setSchool", school, new ResultListener() {
//                    @Override
//                    public void onSuccess(String result) {
//                        Log.d("Result", " set School result is  : " + mMeteor.getUserId() + result);
//
//                    }
//
//                    @Override
//                    public void onError(String error, String reason, String details) {
//
//                        Log.d("Result", "set school result is  : " + error + " reason " + reason);
//
//                    }
//                });
//
//                break;
//            case R.id.calendar_init:
//                String[] params = new String[]{"kia.kayle666@gmail.com", "kkk"};
//                mMeteor.call("calendars.init", params, new ResultListener() {
//                    @Override
//                    public void onSuccess(String result) {
//                        Log.d("Result CAlendar init", " calendar init result is  : " + result);
//
//                    }
//
//                    @Override
//                    public void onError(String error, String reason, String details) {
//                        Log.d("Result CAlendar init", " FAilure reason is : " + reason + " error is " + error);
//
//                    }
//                });
//
//
//                break;
//            case R.id.calendar_sync:
//                String[] params2 = new String[]{"kia.kayle666@gmail.com", "kkk"};
//
//                mMeteor.call("calendars.sync", params2, new ResultListener() {
//                    @Override
//                    public void onSuccess(String result) {
//                        Log.d("Result events feeds", " Events feeds  : " + result);
//
//                    }
//
//                    @Override
//                    public void onError(String error, String reason, String details) {
//                        Log.d("Result events feeds", "Failed Events feeds  : " + reason + " Error " + error);
//
//                    }
//                });
//                break;
//            case R.id.event_list:
//				mMeteor.subscribe()
//                mMeteor.call("events.feeds", new ResultListener() {
//                    @Override
//                    public void onSuccess(String result) {
//                        Log.d("Result events feeds", " Events feeds  : " + result);
//
//                    }
//
//                    @Override
//                    public void onError(String error, String reason, String details) {
//                        Log.d("Result events feeds", "Failed Events feeds  : " + reason + " Error " + error);
//
//                    }
//                });
//
//                break;

        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            System.out.println("Connected! Hell YEa" + url);
//
            if (url.contains("https://www.loopcowstudio.com/_oauth/google?state")) {
                view.evaluateJavascript("JSON.parse(document.getElementById('config').innerHTML)", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        System.out.println("value received" + value);

                        try {
                            JSONObject valueJson = new JSONObject(value);
                            Log.d("Json", "Conversion completed" + valueJson.toString());
                            credentialSecret = valueJson.getString("credentialSecret");
                            credentialToken = valueJson.getString("credentialToken");
                            Log.d("Json", "credential Secret: " + credentialSecret + " credential Token: " + credentialToken);

                        } catch (JSONException e) {
                            Log.d("Json", "Conversion failed");
                            e.printStackTrace();
                        }

                    }
                });
            } else if (url.equalsIgnoreCase("https://www.loopcowstudio.com/_oauth/google")) {
                Log.d("matched", "Final url matched" + url);

                mMeteor.login(credentialToken, credentialSecret, new ResultListener() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d("Freaking", "Success off" + result);

                        mMeteor.loginWithToken(credentialToken, new ResultListener() {
                            @Override
                            public void onSuccess(String result) {
                                Log.d("Hello", "how are you" + result);
                            }

                            @Override
                            public void onError(String error, String reason, String details) {
                                System.out.println("mMeteor.getUserId() is:  "+mMeteor.getUserId());
								Log.d("Hello", "Failed" + error);

                            }
                        });
                    }

                    @Override
                    public void onError(String error, String reason, String details) {
                        Log.d("Freaking", "Buzz off " + error + "Reasons: " + reason + " details " + details);
                    }
                });
            }
//
//        }
        }
    }

    private class MyChromeBrowser extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    }


    @Override
    public void onConnect(final boolean signedInAutomatically) {
        System.out.println("Connected! Hell YEa");
        System.out.println("Is logged in: " + mMeteor.isLoggedIn());
        System.out.println("User ID: " + mMeteor.getUserId());
        browser = (WebView) findViewById(R.id.webview);

        browser.getSettings().setJavaScriptEnabled(true);
        browser.loadUrl(MeteorOAuthServices.google("asdf"));
//        browser.setWebChromeClient(new WebChromeClient());
        browser.setWebViewClient(new MyBrowser());

//		if (signedInAutomatically) {
//			System.out.println("Successfully logged in automatically");
//		}
//		else {
//			// sign up for a new account
//			mMeteor.registerAndLogin("john-doe", "john.doe@example.com", "password1", new ResultListener() {
//
//				@Override
//				public void onSuccess(String result) {
//					System.out.println("Successfully registered: "+result);
//				}
//
//				@Override
//				public void onError(String error, String reason, String details) {
//					System.out.println("Could not register: "+error+" / "+reason+" / "+details);
//				}
//
//			});
//
//			// sign in to the server
//			mMeteor.loginWithUsername("john-doe", "password1", new ResultListener() {
//
//				@Override
//				public void onSuccess(String result) {
//					System.out.println("Successfully logged in: "+result);
//
//					System.out.println("Is logged in: "+mMeteor.isLoggedIn());
//					System.out.println("User ID: "+mMeteor.getUserId());
//				}
//
//				@Override
//				public void onError(String error, String reason, String details) {
//					System.out.println("Could not log in: "+error+" / "+reason+" / "+details);
//				}
//
//			});
//		}
//
//		// subscribe to data from the server
//		String subscriptionId = mMeteor.subscribe("publicMessages");
//
//		// unsubscribe from data again (usually done later or not at all)
//		mMeteor.unsubscribe(subscriptionId);
//
//		// insert data into a collection
//		Map<String, Object> insertValues = new HashMap<String, Object>();
//		insertValues.put("_id", "my-key");
//		insertValues.put("some-number", 3);
//		mMeteor.insert("my-collection", insertValues);
//
//		// update data in a collection
//		Map<String, Object> updateQuery = new HashMap<String, Object>();
//		updateQuery.put("_id", "my-key");
//		Map<String, Object> updateValues = new HashMap<String, Object>();
//		updateValues.put("_id", "my-key");
//		updateValues.put("some-number", 5);
//		mMeteor.update("my-collection", updateQuery, updateValues);
//
//		// remove data from a collection
//		mMeteor.remove("my-collection", "my-key");
//
//		// call an arbitrary method
//        mMeteor.call("conn.test", new ResultListener() {
//            @Override
//            public void onSuccess(String result) {
//                Log.d("Result", result);
//            }
//
//            @Override
//            public void onError(String error, String reason, String details) {
//                Log.d("Result", error);
//
//            }
//        });
    }

    @Override
    public void onDisconnect() {
        System.out.println("Disconnected");
    }

    @Override
    public void onDataAdded(String collectionName, String documentID, String fieldsJson) {
//		System.out.println("Data added to <"+collectionName+"> in document <"+documentID+">");
//		System.out.println("    Added: "+fieldsJson);
    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {
//		System.out.println("Data changed in <"+collectionName+"> in document <"+documentID+">");
//		System.out.println("    Updated: "+updatedValuesJson);
//		System.out.println("    Removed: "+removedValuesJson);
    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {
//		System.out.println("Data removed from <"+collectionName+"> in document <"+documentID+">");
    }

    @Override
    public void onException(Exception e) {
        System.out.println("Exception");
        if (e != null) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        mMeteor.disconnect();
        mMeteor.removeCallback(this);
        // or
        // mMeteor.removeCallbacks();

        // ...

        super.onDestroy();
    }


/*private void testDatabase() {
        // first Meteor#handleMessage has to be made public temporarily

		// mock some data that is being added
		mMeteor.handleMessage("{\"msg\":\"added\",\"collection\":\"people\",\"id\":\"al\",\"fields\":{\"name\":\"Alice\",\"age\":25,\"gender\":\"f\",\"location\":{\"country\":\"Japan\",\"region\":\"Kansai\"}}}");
		mMeteor.handleMessage("{\"msg\":\"added\",\"collection\":\"people\",\"id\":\"bo\",\"fields\":{\"name\":\"Bob\",\"age\":27,\"gender\":\"m\",\"location\":{\"country\":\"Spain\",\"region\":\"Andalusia\"}}}");
		mMeteor.handleMessage("{\"msg\":\"added\",\"collection\":\"people\",\"id\":\"ca\",\"fields\":{\"name\":\"Carol\",\"age\":29,\"gender\":null,\"location\":null}}");
		mMeteor.handleMessage("{\"msg\":\"added\",\"collection\":\"people\",\"id\":\"ev\",\"fields\":{\"name\":\"Eve\",\"age\":31,\"gender\":\"f\",\"location\":{\"country\":\"Australia\",\"region\":null}}}");
		mMeteor.handleMessage("{\"msg\":\"added\",\"collection\":\"settings\",\"id\":\"5h2iJyPMZmDTaSwGC\",\"fields\":{\"appId\":\"92Hn8HKvatWDPP22u\",\"endpoint\":\"http://www.example.com/\",\"clientDelay\":10000,\"enableSomething\":true}}");
		mMeteor.handleMessage("{\"msg\":\"added\",\"collection\":\"companies\",\"id\":\"31c53bca49616e773567920d\",\"fields\":{\"owner\":null,\"isInProgress\":true,\"Description\":\"Acme Inc. is a company\",\"Company\":\"Acme Inc.\",\"Location\":\"Some city, Some country\",\"Region\":\"SomeContinent/SomeOtherContinent\",\"Logo\":\"/assets/i/companies/default-logo.png\",\"Type\":\"Things\",\"Website\":\"http://www.example.com/\",\"prime\":false}}");
		mMeteor.handleMessage("{\"msg\":\"added\",\"collection\":\"versions\",\"id\":\"JyPMZ49616e7735\",\"fields\":{\"version\":\"ae976571be8a6999984eae9da24fc5d948ca80ac\",\"assets\":{\"allCss\":[{\"url\":\"/main.css\"}]}}}");
		mMeteor.handleMessage("{\"msg\":\"added\",\"collection\":\"events\",\"id\":\"2H7ZDva9nhL4F42im\",\"fields\":{\"coords\":{\"type\":\"Point\",\"coordinates\":[1.23,-2.345]},\"events\":[{\"eventId\":\"946221490\",\"eventName\":\"Meteor 101\",\"eventUrl\":\"http://www.example.com/946221490\",\"eventTime\":1000000000000,\"eventUTCOffset\":-3600000}],\"groupId\":2018074068,\"groupName\":\"Meteor 101 A\",\"groupUrlname\":\"Meteor-101-A\"}}");

		// mock some data that is being changed
		mMeteor.handleMessage("{\"msg\":\"changed\",\"collection\":\"people\",\"id\":\"ev\",\"fields\":{\"age\":23,\"location\":{\"region\":\"New South Wales\"}},\"cleared\":[\"gender\"]}");

		// mock some data that is being removed
		mMeteor.handleMessage("{\"msg\":\"removed\",\"collection\":\"people\",\"id\":\"ca\"}");

		// get a reference to the database
		final Database database = mMeteor.getDatabase();

		// wait a second
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// and then check what's in there

				// test the database operations
				System.out.println("Database#count() = " + database.count());
				System.out.println("Database#getCollectionNames() = " + Arrays.toString(database.getCollectionNames()));
				System.out.println("Database#getCollection(\"customers\") = " + database.getCollection("customers"));
				System.out.println("Database#getCollection(\"customers\").count() = " + database.getCollection("customers").count());
				System.out.println("Database#getCollection(\"people\") = " + database.getCollection("people"));

				// print a divider
				System.out.println("----------");

				// get a reference to a collection
				final Collection collection = database.getCollection("people");

				// test the collection operations
				System.out.println("Collection#getName() = " + collection.getName());
				System.out.println("Collection#count() = " + collection.count());
				System.out.println("Collection#getDocumentIds() = " + Arrays.toString(collection.getDocumentIds()));
				System.out.println("Collection#getDocument(\"jo\") = " + collection.getDocument("jo"));
				System.out.println("Collection#getDocument(\"al\") = " + collection.getDocument("al"));
				System.out.println("Collection#getDocument(\"ca\") = " + collection.getDocument("ca"));
				System.out.println("Collection#getDocument(\"ev\") = " + collection.getDocument("ev"));

				// print a divider
				System.out.println("----------");

				// get a reference to a document
				final Document document = collection.getDocument("al");

				// test the document operations
				System.out.println("Document#getId() = " + document.getId());
				System.out.println("Document#count() = " + document.count());
				System.out.println("Document#getFieldNames() = " + Arrays.toString(document.getFieldNames()));
				System.out.println("Document#getField(\"age\") = " + document.getField("age"));

				// print a divider
				System.out.println("----------");

				// test the query builder operations
				System.out.println("Collection#findOne() = " + collection.findOne());
				System.out.println("Collection#find(1) = " + Arrays.toString(collection.find(1)));
				System.out.println("Collection#find(2) = " + Arrays.toString(collection.find(2)));
				System.out.println("Collection#find(5) = " + Arrays.toString(collection.find(5)));
				System.out.println("Collection#find(1, 1) = " + Arrays.toString(collection.find(1, 1)));
				System.out.println("Collection#find(1, 2) = " + Arrays.toString(collection.find(1, 2)));
				System.out.println("Collection#find(2, 1) = " + Arrays.toString(collection.find(2, 1)));
				System.out.println("Collection#find(2, 2) = " + Arrays.toString(collection.find(2, 2)));
				System.out.println("Collection#find() = " + Arrays.toString(collection.find()));
				System.out.println("Collection#whereEqual(\"name\", \"Eve\").find() = " + Arrays.toString(collection.whereEqual("name", "Eve").find()));
				System.out.println("Collection#whereNotEqual(\"name\", \"Eve\").find() = " + Arrays.toString(collection.whereNotEqual("name", "Eve").find()));
				System.out.println("Collection#whereLessThan(\"age\", 27).find() = " + Arrays.toString(collection.whereLessThan("age", 27).find()));
				System.out.println("Collection#whereLessThanOrEqual(\"age\", 27).find() = " + Arrays.toString(collection.whereLessThanOrEqual("age", 27).find()));
				System.out.println("Collection#whereLessThan(\"age\", 25).find() = " + Arrays.toString(collection.whereLessThan("age", 25).find()));
				System.out.println("Collection#whereGreaterThan(\"age\", 23).find() = " + Arrays.toString(collection.whereGreaterThan("age", 23).find()));
				System.out.println("Collection#whereGreaterThanOrEqual(\"age\", 23).find() = " + Arrays.toString(collection.whereGreaterThanOrEqual("age", 23).find()));
				System.out.println("Collection#whereGreaterThan(\"age\", 25).find() = " + Arrays.toString(collection.whereGreaterThan("age", 25).find()));
				System.out.println("Collection#whereNull(\"location\").find() = " + Arrays.toString(collection.whereNull("location").find()));
				System.out.println("Collection#whereNotNull(\"location\").find() = " + Arrays.toString(collection.whereNotNull("location").find()));
				System.out.println("Collection#whereNotNull(\"gender\").whereLessThan(\"age\", 26).find() = " + Arrays.toString(collection.whereNotNull("gender").whereLessThan("age", 26).find()));
			}

		}, 1000);
	}

*/
}

