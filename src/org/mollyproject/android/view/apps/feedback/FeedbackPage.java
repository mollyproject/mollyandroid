package org.mollyproject.android.view.apps.feedback;

import java.io.UnsupportedEncodingException;

import org.mollyproject.android.R;
import org.mollyproject.android.controller.MollyModule;
import org.mollyproject.android.view.apps.ContentPage;
import org.mollyproject.android.view.apps.Page;
import org.mollyproject.android.view.apps.UnimplementedPage;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class FeedbackPage extends ContentPage {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		name = MollyModule.FEEDBACK_PAGE;
		LinearLayout feedbackLayout = (LinearLayout) getLayoutInflater().inflate
				(R.layout.feedback, contentLayout, false);
		contentLayout.addView(feedbackLayout);
		
		final EditText feedbackEmail = (EditText) feedbackLayout.findViewById(R.id.feedbackEmail);
		final EditText feedbackBody = (EditText) feedbackLayout.findViewById(R.id.feedbackBody);
		
		Button submitButton = (Button) feedbackLayout.findViewById(R.id.submitButton);
		submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new FeedbackTask(FeedbackPage.this, false, true).execute(feedbackBody.getText().toString(),
						feedbackEmail.getText().toString());
			}
		});
	}
	@Override
	public Page getInstance() {
		return this;
	}

	@Override
	public String getQuery() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
	}
}
