package apt.tutorial;

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSItem;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FeedActivity extends ListActivity {
	public static final String FEED_URL = "apt.tutorial.FEED_URL";
	
	private InstanceState state = null;
	
	private static class InstanceState {
		RSSFeed feed = null;
		FeedHandler handler = null;
	}
	
	private static class FeedHandler extends Handler {
		private FeedActivity activity = null;
		
		FeedHandler(FeedActivity activity) {
			attach(activity);
		}
		
		void attach(FeedActivity activity) {
			this.activity = activity;
		}
		
		void detach() {
			attach(null);
		}
		
		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == RESULT_OK) {
				activity.setFeed((RSSFeed)msg.obj);
			}
			else {
				activity.displayErrorMessage((Exception)msg.obj);
			}
		}
	}
	
	private class FeedAdapter extends BaseAdapter {
		RSSFeed feed = null;
		
		FeedAdapter(RSSFeed feed) {
			super();
			
			this.feed = feed;
		}
		
		public int getCount() {
			return feed.getItems().size();
		}
		
		public Object getItem(int position) {
			return feed.getItems().get(position);
		}
		
		public long getItemId(int position) {
			return position;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			
			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				
				row = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
			}
			
			RSSItem item = (RSSItem)getItem(position);
			
			((TextView)row).setText(item.getTitle());
			
			return row;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		state = (InstanceState)getLastNonConfigurationInstance();
		
		if (state == null) {
			state = new InstanceState();
			state.handler = new FeedHandler(this);
			
			Intent i = new Intent(this, FeedService.class);
			
			i.putExtra(FeedService.EXTRA_URL, getIntent().getStringExtra(FEED_URL));
			i.putExtra(FeedService.EXTRA_MESSENGER, new Messenger(state.handler));
			
			startService(i);
		}
		else {
			if (state.handler != null) {
				state.handler.attach(this);
			}
			
			if (state.feed != null) {
				setFeed(state.feed);
			}
		}
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		if (state.handler != null) {
			state.handler.detach();
		}
		
		return state;
	}
	
	public void displayErrorMessage(Throwable t) { // with a sensible name, unlike the tutorial's version...
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle("Exception!").setMessage(t.toString()).setPositiveButton("OK", null).show();
	}

	public void setFeed(RSSFeed feed) {
		state.feed = feed;
		setListAdapter(new FeedAdapter(feed));
	}
}
