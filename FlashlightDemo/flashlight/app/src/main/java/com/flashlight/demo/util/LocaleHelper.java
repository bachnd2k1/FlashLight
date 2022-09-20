package com.flashlight.demo.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.fragment.app.FragmentManager;

import com.flashlight.demo.BaseApplication;
import com.flashlight.demo.BuildConfig;
import com.flashlight.demo.R;
import com.flashlight.demo.dialog.ChangeLanguageDialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class LocaleHelper {
    public static final String[] COUNTRY_CODES = new String[]{
            "af","am", "ar", "az", "be", "bem", "bg", "bh", "bn", "bs", "ca", "co", "cs", "da", "de", "el", "en", "en-rAU","en-rCA",
            "en-rDO", "en-rGB", "en-rHK", "en-rIE","en-rIN" ,"en-rJO", "en-rMY", "en-rNZ", "en-rPH", "en-rSG", "en-rZA", "es",
            "es-rAR", "es-rCL", "es-rCO", "es-rES", "es-rMX", "es-rPE", "es-rUS", "es-rUY", "es-rVE", "et" ,"eu", "fa", "fi",
            "fr", "fy", "gu", "ha", "hi", "hr", "ht", "ht", "hu", "hy", "id", "ig", "is", "it", "iw", "ja", "jw", "ka", "kk", "km", "kn", "ko", "la",
            "lo", "lt", "lv", "mk", "ml", "mn", "mr", "ms", "my", "nb", "ne", "nl", "ny", "pl", "pt", "pt-rBR", "pt-rPT",
            "ro", "ru",  "si", "sk", "sl", "so", "sq", "sr", "sv", "sw", "te", "th", "tl", "tr", "uk", "ur", "vi", "zh-rCN", "zh-rHK", "zh-rTW", "zu"};

    private static final String KEY_SELECTED_LANGUAGE_POSITION = "KEY_SELECTED_LANGUAGE_POSITION";
    public static String LANGUAGE_SELECTED = BuildConfig.APPLICATION_ID + "LANGUAGE_SELECTED";
    private static final String DEFAULT_LANGUAGE = "en";

    public static Context setLocale(Context c) {
        return updateResources(c, getLanguage(c));
    }

    public static Context setNewLocale(Context c, String language) {
        persistLanguage(c, language);
//        updateResources(c, language);
        return updateResources(c, language);
    }

    public static String getLanguage(Context c) {
        return PreferencesHelper.getInstances().getStringSPr(LANGUAGE_SELECTED, c, Locale.getDefault().getLanguage() + (TextUtils.isEmpty(Locale.getDefault().getCountry()) ? "" : ("-" + Locale.getDefault().getCountry())));
    }

    @SuppressLint("ApplySharedPref")
    private static void persistLanguage(Context c, String language) {
        PreferencesHelper.getInstances().setStringSPr(LANGUAGE_SELECTED, language, c);
    }

    private static Context updateResources(Context context, String language) {
        Locale locale;
        if (language.equals(MyUtils.getString(R.string.auto))) {
            locale = Resources.getSystem().getConfiguration().locale;
        } else {
            switch (language) {
                case "zh-rCN":
                case "zh":
                    locale = Locale.SIMPLIFIED_CHINESE;
                    break;
                case "zh-rTW":
                    locale = Locale.TRADITIONAL_CHINESE;
                    break;
                default:
                    String[] spk = language.split("-");
                    if (spk.length > 1) {
                        locale = new Locale(spk[0], spk[1]);
                    } else {
                        locale = new Locale(spk[0]);
                    }
                    break;
            }
        }
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = res.getConfiguration();
        config.setLocale(locale);
        context = context.createConfigurationContext(config);
        applyLanguageForApplicationContext(language);
        return context;
    }

    public static Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
//        return Build.VERSION.SDK_INT >= 24 ? config.getLocales().get(0) : config.locale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return config.getLocales().get(0);
        } else{
            //noinspection deprecation
            return config.locale;
        }
    }

    public static ArrayList<String> getListCountries() {
        ArrayList<String> countries = new ArrayList<>();
        Locale locale;
        for (String code : LocaleHelper.COUNTRY_CODES) {
            String[] spk = code.split("-");
            if (spk.length > 1) {
                locale = new Locale(spk[0], spk[1]);
            } else {
                locale = new Locale(code);
            }
            countries.add(LocaleHelper.toDisplayCase(locale.getDisplayName(locale)));
        }
        Collections.sort(countries);
        countries.add(0, MyUtils.getString(R.string.auto));
        return countries;
    }

    public static String getLanguageCodeByDisplayName(String displayName) {
        if (MyUtils.getString(R.string.auto).equalsIgnoreCase(displayName))
            return displayName;
        for (String key : COUNTRY_CODES) {
            Locale locale;
            String[] spk = key.split("-");
            if (spk.length > 1) {
                locale = new Locale(spk[0], spk[1]);
            } else {
                locale = new Locale(key);
            }
            if (displayName.equalsIgnoreCase(locale.getDisplayName(locale))) {
                return key;
            }
        }
        return DEFAULT_LANGUAGE;
    }

    public static void showChangeLanguageDialog(FragmentManager supportFragmentManager) {
        ChangeLanguageDialogFragment dialogFragment = new ChangeLanguageDialogFragment();
        dialogFragment.show(supportFragmentManager, "");
    }

    public static String getSelectedLanguage(Context context) {
      return PreferencesHelper.getInstances().getStringSPr(KEY_SELECTED_LANGUAGE_POSITION, context, MyUtils.getString(R.string.auto));
    }

    public static void setSelectedLanguage(String language, Context context) {
        PreferencesHelper.getInstances().setStringSPr(KEY_SELECTED_LANGUAGE_POSITION, language, context);
    }

    public static String toDisplayCase(String s) {
        final String ACTIONABLE_DELIMITERS = " '-/";
        StringBuilder sb = new StringBuilder();
        boolean capNext = true;

        for (char c : s.toCharArray()) {
            c = (capNext) ? Character.toUpperCase(c) : Character.toLowerCase(c);
            sb.append(c);
            capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0);
        }
        return sb.toString();
    }

    public static void applyLanguageForApplicationContext() {
        applyLanguageForApplicationContext(getLanguage(BaseApplication.getInstance()));
    }

    public static void applyLanguageForApplicationContext(String language) {
        final Resources resources = BaseApplication.getInstance().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        final Configuration configuration = resources.getConfiguration();
        Locale locale;
        if (language.equals(BaseApplication.getInstance().getString(R.string.auto))) {
            locale = Resources.getSystem().getConfiguration().locale;
        } else {

            switch (language) {
                case "zh-rCN":
                case "zh":
                    locale = Locale.SIMPLIFIED_CHINESE;
                    break;
                case "zh-rTW":
                    locale = Locale.TRADITIONAL_CHINESE;
                    break;
                default:
                    String[] spk = language.split("-");
                    if (spk.length > 1) {
                        locale = new Locale(spk[0], spk[1]);
                    } else {
                        locale = new Locale(spk[0]);
                    }
                    break;
            }
        }
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, dm);
    }
}
