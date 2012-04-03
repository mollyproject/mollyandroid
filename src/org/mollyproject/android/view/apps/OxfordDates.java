package org.mollyproject.android.view.apps;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Table.Cell;
import com.google.common.collect.TreeBasedTable;

public class OxfordDates {
	
	/*
	# * Convert from an ox date to a normal date                ox_to_normal
	# * Convert from a normal date to an ox date                normal_to_ox
	# * Find the most recent start-of-term for a given date     term_start
	# * Convert a (year, term) pair to a string                 term_as_string
	# * Convert a string to a (year, term) pair                 term_from_string */
	
	// This will only cover from week -2 to week 12
	public static Calendar calendar = Calendar.getInstance(); // our Swiss-Army knife of Java datetime
	
	public enum Term {
		MICHAELMAS (0 , "michaelmas", new String[] {"10 OCT 2010", "09 OCT 2011"} ),
		HILARY (1 , "hilary", new String[] {"16 JAN 2011", "15 JAN 2012"}),
		TRINITY (2 , "trinity", new String[] {"01 MAY 2011", "22 APR 2012"});

		public final int termInt;
	    public final String termString;
	    public final String[] startDates;
	    
	    Term (int termInt, String termString, String[] startDates) {
	    	this.termInt = termInt;
	    	this.termString = termString;
	    	this.startDates = startDates;
	    }
	    
	    public static Term termLookup(int termInt)
	    {
	    	for (Term term : Term.values())
	    	{
	    		if (term.termInt == termInt)
	    			return term;
	    	}
	    	return null;
	    }
	    
	    public static Date termStarts(int termInt, int year) throws ParseException
	    {
	    	Date startDate = null;
	    	for (Term term : Term.values())
	    	{
	    		if (term.termInt == termInt)
	    		{
		    		for (String startDateStr : term.startDates)
		    		{
		    			startDate = normalDateFormat.parse(startDateStr);
		    			calendar.setTime(startDate);
		    			if (calendar.get(Calendar.YEAR) == year)
		    			{
		    				return startDate;
		    			}
		    		}
	    		}
	    	}
	    	return null;
	    }
	    
	    // the following 2 parameters are only used for the termStarts() method and should not
	    // be used for any other thing else
	    public static int dateDiff;
	    public static int whichTerm; 
	    
		public static Date termStarts(Date date) throws ParseException
		{
			
			// return the start of term for a given date and store the date difference
			//Set<Cell<Integer,Integer,String>> termStartsSet = termStartDates.cellSet();
			//for (Cell<Integer,Integer,String> dateCell : termStartsSet)
			Date startDate = null;
			for (Term term : Term.values())
			{
				for (String startDateStr : term.startDates)
				{
					//String dateString = dateCell.getValue();
					startDate = normalDateFormat.parse(startDateStr);
					dateDiff = dateDiff(startDate, date);
					whichTerm = term.termInt;
					/*System.out.println("start: " + startDate);
					System.out.println("date: " + date);
					System.out.println("diff: " + dateDiff);
					System.out.println();*/
					if (dateDiff < 0 & dateDiff >= -21)
					{
						// We've found a day in week -2-0
						return startDate;
					}
					else if (dateDiff >= 0 & dateDiff <= 84)
					{
						//We've found a day in week 1-12
						return startDate;
					}
					// else this start date is not the right one
				}
			}
			// no need to re-init whichTerm and dateDiff at this point because 
			return null;
		}
	    
	}
	
	public static final DateFormat normalDateFormat = new SimpleDateFormat("d MMM yyyy");
	
	public enum Day {
		SUNDAY (Calendar.SUNDAY, "SUNDAY"),
		MONDAY (Calendar.MONDAY, "MONDAY"),
		TUESDAY (Calendar.TUESDAY, "TUESDAY"),
		WEDNESDAY (Calendar.WEDNESDAY, "WEDNESDAY"),
		THURSDAY (Calendar.THURSDAY, "THURSDAY"),
		FRIDAY (Calendar.FRIDAY, "FRIDAY"),
		SATURDAY (Calendar.SATURDAY, "SATURDAY");
		
		public final int dayInt;
		public final String dayString;
		Day (int dayInt, String dayString)
		{
			this.dayInt = dayInt;
			this.dayString = dayString;
		}
		
		public static Day dayLookup(int dayInt)
		{
			for (Day day : Day.values())
			{
				if (day.dayInt == dayInt)
				{
					return day;
				}
			}
			return null;
		}
		
		public static Day dayLookup(String dayString)
		{
			for (Day day : Day.values())
			{
				if (day.dayString == dayString)
				{
					return day;
				}
			}
			return null;
		}
	}
	
	public enum Month {
		JANUARY (Calendar.JANUARY, "JAN"),
		FEBRUARY (Calendar.FEBRUARY, "FEB"),
		MARCH (Calendar.MARCH, "MAR"),
		APRIL (Calendar.APRIL, "APR"),
		MAY (Calendar.MAY, "MAY"),
		JUNE (Calendar.JUNE, "JUN"),
		AUGUST (Calendar.AUGUST, "AUG"),
		SEPTEMBER (Calendar.SEPTEMBER, "SEP"),
		OCTOBER (Calendar.OCTOBER, "OCT"),
		NOVEMBER (Calendar.NOVEMBER, "NOV"),
		DECEMBER (Calendar.DECEMBER, "DEC");
		
		public final int monthInt;
		public final String monthString;
		Month (int monthInt, String monthString)
		{
			this.monthInt = monthInt;
			this.monthString = monthString;
		}
		
		public static Month monthLookup(int monthInt)
		{
			for (Month month : Month.values())
			{
				if (month.monthInt == monthInt)
				{
					return month;
				}
			}
			return null;
		}
		
		public static Month monthLookup(String monthString)
		{
			for (Month month : Month.values())
			{
				if (month.monthString == monthString)
				{
					return month;
				}
			}
			return null;
		}
	}
	
	public static Date oxToNormal(int year, int term, int week, int day) throws Exception
	{
		Date termStart = Term.termStarts(term, year); //normalDateFormat.parse(termStartDates.get(year, term));
		calendar.setTime(termStart);
		// now the calendar is pointing at the start of term, add the date offset
		if (week < -2 || week > 10)
		{
			throw new Exception("Invalid Oxford Dates");
		}
		if (week == 0)
			calendar.add(Calendar.DATE, -day);
		else
			calendar.add(Calendar.DATE, (week-1)*7 + day);
		return calendar.getTime();
	}
	
	private static int dateDiff(Date date1, Date date2)
	{
		Calendar calendar_ = Calendar.getInstance();
		calendar.setTime(date1);
		calendar_.setTime(date2);
		int dayInYear = calendar_.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR);
		int years = calendar_.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
		/*if (isLeap(calendar_.get(Calendar.YEAR)) & !isLeap(calendar.get(Calendar.YEAR)))
			years = years*365 + 1;
		else if (!isLeap(calendar_.get(Calendar.YEAR)) & isLeap(calendar.get(Calendar.YEAR)))
			years = years*365 - 1;
		else*/ 
		int diff = dayInYear + years*365;
		return diff;
	}
	
	public static boolean isLeap(int year)
	{
		return (year % 4 == 0 & year %100 != 0);
	}
	
	public static int[] normalToOx(Date date) throws Exception
	{
		int[] oxDate = new int[4];
		Date startDate = Term.termStarts(date);
		if (startDate == null)
			throw new Exception("The date given is invalid");
		
		// If reached here, the date should be valid:
		calendar.setTime(startDate);
		// year, term, week, day 
		oxDate[0] = calendar.get(Calendar.YEAR);
		oxDate[1] = Term.whichTerm;
		System.out.println("Date diff: " + Term.dateDiff);
		if (Term.dateDiff < 0)
		{
			// < 0 and still valid
			oxDate[2] = (int) Math.floor((double) Term.dateDiff/7) + 1; // week starts on Sunday, hence the offset -1
			oxDate[3] = ((21 + Term.dateDiff) % 7) + 1;
		}
		else
		{
			oxDate[2] = (int) Math.floor((double) Term.dateDiff/7) + 1; // week starts on Sunday, hence the offset +1
			oxDate[3] = (Term.dateDiff % 7) + 1; // % 7, we get 0-6, the 1 is the offset
		}
		
		/*System.out.println(date + " is:");
		System.out.println("Year: " + oxDate[0]);
		System.out.println("Term: " + termStrings.get(oxDate[1]));
		System.out.println("Week: " + oxDate[2]);
		System.out.println("Day : " + oxDate[3]);
		System.out.println("in Oxford time");
		System.out.println();*/
		return oxDate;
	}
	
	public static String printOxDate(int[] oxDate)
	{
		// example method for printing the date in the much beloved Oxford format
		String dateToPrint;
		
		// Day of the week:
		dateToPrint = Day.dayLookup(oxDate[3]).dayString;
		
		// Week number:
		dateToPrint += ", Week " + oxDate[2];
		
		// Term:
		dateToPrint += ", " + Term.termLookup(oxDate[1]);
		
		// Year:
		dateToPrint += ", " + oxDate[0];
		
		return dateToPrint;
	}
	
	public static int termToInt(String termString)
	{
		return Term.valueOf(termString).termInt;
	}
	
	public static int dayToInt(String dayString)
	{
		return Day.dayLookup(dayString).dayInt;
	}
	
	public static String printNormalDate(Date date)
	{
		String dateToPrint;
		calendar.setTime(date);
		dateToPrint = Day.dayLookup(calendar.get(Calendar.DAY_OF_WEEK)).dayString;
		dateToPrint += ", " + calendar.get(Calendar.DAY_OF_MONTH);
		dateToPrint += ", " + Month.monthLookup(calendar.get(Calendar.MONTH)).monthString;
		dateToPrint += " " + calendar.get(Calendar.YEAR);
		return dateToPrint;
	}
}
