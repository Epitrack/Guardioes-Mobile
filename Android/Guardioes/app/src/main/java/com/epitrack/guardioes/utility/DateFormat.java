package com.epitrack.guardioes.utility;

import com.epitrack.guardioes.view.menu.profile.InterestActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Miquéias Lopes on 12/09/15.
 */
public final class DateFormat {

    public static String getDate(String date, String format) {

        SimpleDateFormat userFormat = new SimpleDateFormat(format);
        SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strReturn = "";

        try {
            Date userDate = apiFormat.parse(date);
            strReturn = userFormat.format(userDate);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            return strReturn;
        }
    }

    public static String getDate(String date) {

        SimpleDateFormat userFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strReturn = "";

        try {
            Date userDate = userFormat.parse(date);
            strReturn = apiFormat.format(userDate);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            return strReturn;
        }
    }

    public static int getDateDiff(String date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        //int year = calendar.get(Calendar.YEAR);

        Date userDate = null;
        Date today = null;
        int diffDate = -1;

        try {
            userDate = format.parse(date);
            today = CalendarDay.today().getDate();
            //today = format.parse(today.toString());

            long diff = today.getTime() - userDate.getTime();

            //long diffSeconds = diff / 1000 % 60;
            //long diffMinutes = diff / (60 * 1000) % 60;
            //long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            long diffYears = diffDays / 365;
            diffDate = ((int) diffYears);

            //diffDate = year - Integer.parseInt(userFormat.format(userDate).toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return diffDate;
    }

    public static boolean isDate(String date) {
        boolean bReturn = true;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false); // aqui o pulo do gato

        try {
            df.parse (date);
            bReturn = true;
        } catch (ParseException ex) {
            bReturn = false;
        } finally {
            return  bReturn;
        }
    }
}
