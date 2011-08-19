package org.mollyproject.android.view.apps.library;

import org.json.JSONException;
import org.json.JSONObject;
import org.mollyproject.android.R;
import org.mollyproject.android.view.apps.ComplexMapResultTask;
import org.mollyproject.android.view.apps.PageWithMap;

import android.text.Html;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LibraryBookResultTask extends ComplexMapResultTask{

	public LibraryBookResultTask(LibraryBookResultPage page,
			boolean toDestroyPageAfterFailure, boolean dialogEnabled) {
		super(page, toDestroyPageAfterFailure, dialogEnabled);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void updateView(JSONObject jsonContent) {
		super.updateView(jsonContent);
		if (!exceptionCaught)
		{
			try {
				LayoutInflater inflater = page.getLayoutInflater();
				LinearLayout mapLayout = ((PageWithMap) page).getMapLayout();
				
				LinearLayout bookInfoLayout = (LinearLayout) inflater.inflate(R.layout.book_info_layout, 
						mapLayout, false);
				mapLayout.addView(bookInfoLayout,1); //add the info in between breadcrumbs and map
				
				JSONObject book = jsonContent.getJSONObject("item");
				TextView bookTitle = (TextView) bookInfoLayout.findViewById(R.id.bookTitle);
				TextView bookDetails = (TextView) bookInfoLayout.findViewById(R.id.bookDetails);
				
				bookTitle.setText(book.getString("title"));
				
				String details = new String();
				if (!book.isNull("publisher"))
				{
					details = details + "<b>Publisher:</b>" + addNonBreakingSpaces(5) 
							+ book.getString("publisher");
				}
				if (!book.isNull("description"))
				{
					details = details + "<br/>" + "<b>Description:</b>"
							+ addNonBreakingSpaces(1) + book.getString("description");
				}
				if (!book.isNull("isbns"))
				{
					if (book.getJSONArray("isbns").length() > 0)
					{
						details = details + "<br/>" + "<b>ISBN:</b>" + addNonBreakingSpaces(14)
								+ book.getJSONArray("isbns").getString(0);
					}
				}
				bookDetails.setText(Html.fromHtml(details));
			} catch (JSONException e) {
				e.printStackTrace();
				jsonException = true;
			}
		}
	}
	
	public String addNonBreakingSpaces(int i)
	{
		String spaces = new String();
		for (int j = 0; j < i; j++)
		{
			spaces = spaces + "&nbsp;";
		}
		return spaces;
	}
}
