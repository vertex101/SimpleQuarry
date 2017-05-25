package com.mrdimka.simplequarry.utils;

import java.util.Calendar;

public class DateUtils
{
	public static boolean isAprilFoolsDay()
	{
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		return month == 4 && day == 1;
	}
}