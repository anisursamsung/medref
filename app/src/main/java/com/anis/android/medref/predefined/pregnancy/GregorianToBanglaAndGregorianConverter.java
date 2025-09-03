package com.anis.android.medref.predefined.pregnancy;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class GregorianToBanglaAndGregorianConverter {
    private static final String[] BANGLA_MONTHS = {
            "বৈশাখ", "জ্যৈষ্ঠ", "আষাঢ়", "শ্রাবণ", "ভাদ্র", "আশ্বিন",
            "কার্তিক", "অগ্রহায়ণ", "পৌষ", "মাঘ", "ফাল্গুন", "চৈত্র"
    };

    public String convertToBangla(int day, int month, int year) {
        Calendar gregorianDate = Calendar.getInstance();
        gregorianDate.set(year, month - 1, day); // month is 0-based in Calendar

        Calendar banglaNewYear = Calendar.getInstance();
        banglaNewYear.set(year, Calendar.APRIL, 14);

        int banglaYear = year - 593;
        if (gregorianDate.before(banglaNewYear)) {
            banglaYear--;
            banglaNewYear.add(Calendar.YEAR, -1);
        }

        long daysSinceBanglaNewYear = (gregorianDate.getTimeInMillis() - banglaNewYear.getTimeInMillis()) / (24 * 60 * 60 * 1000);

        int banglaMonth = 0;
        int banglaDay = 0;

        if (daysSinceBanglaNewYear < 31 * 6) {
            // First 6 months
            banglaMonth = (int) (daysSinceBanglaNewYear / 31);
            banglaDay = (int) (daysSinceBanglaNewYear % 31) + 1;
        } else {
            // Last 6 months
            long remainingDays = daysSinceBanglaNewYear - (31 * 6);
            if (remainingDays < 30 * 5) {
                banglaMonth = 6 + (int) (remainingDays / 30);
                banglaDay = (int) (remainingDays % 30) + 1;
            } else {
                // Falgun and Chaitra
                remainingDays -= 30 * 5;
                if (remainingDays < getFalgunDays(year)) {
                    banglaMonth = 11;
                    banglaDay = (int) remainingDays + 1;
                } else {
                    banglaMonth = 12;
                    banglaDay = (int) (remainingDays - getFalgunDays(year)) + 1;
                }
            }
        }

        String banglaMonthName = BANGLA_MONTHS[banglaMonth];

        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM, yyyy", Locale.getDefault());
        String englishDate = sdf.format(gregorianDate.getTime());

        return String.format(Locale.getDefault(), "%d %s %d\n%s", banglaDay, banglaMonthName, banglaYear, englishDate);

    }

    private int getFalgunDays(int gregorianYear) {
        // Falgun falls within the same Gregorian year as the Bangla year start
        return isLeapYear(gregorianYear) ? 30 : 29;
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}