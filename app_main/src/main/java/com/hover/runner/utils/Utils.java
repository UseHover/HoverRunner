package com.hover.runner.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.google.gson.Gson;
import com.hover.runner.BuildConfig;
import com.hover.runner.R;
import com.hover.runner.enums.ActionRunStatus;
import com.hover.runner.enums.StatusEnums;
import com.hover.runner.models.ActionVariablesDBModel;
import com.hover.runner.models.ActionsModel;
import com.hover.runner.models.RawStepsModel;
import com.hover.runner.models.StreamlinedStepsModel;
import com.hover.runner.settings.SettingsHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

    public final static String TESTER_VERSION = "1.0 (1)";
    private final static String HOVER_TRANSAC_FAILED = "failed";
    public final static String HOVER_TRANSAC_PENDING = "pending";
    public final static String HOVER_TRANSAC_SUCCEEDED = "succeeded";
    private static final String SHARED_PREFS = "_runner";
    private static final String PRO_VARIANT="proDashboard";

    public static SharedPreferences getSharedPrefs(Context context) {
        return context.getSharedPreferences(SettingsHelper.getPackage(context) + SHARED_PREFS, Context.MODE_PRIVATE);
    }

    public static void saveString(String key, String value, Context c) {
        SharedPreferences.Editor editor = Utils.getSharedPrefs(c).edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static void saveInt(String key, int value, Context c) {
        SharedPreferences.Editor editor = Utils.getSharedPrefs(c).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static String getSavedString(String key, Context c) {
        return getSharedPrefs(c).getString(key, "");
    }

    public static int getSavedInt(String key, Context c) {
        return getSharedPrefs(c).getInt(key, 0);
    }

    public static StreamlinedStepsModel getStreamlinedStepsStepsFromRaw(@NonNull String rootCode, @NonNull  JSONArray jsonArray) {
        Gson gson = new Gson();
        RawStepsModel[] rawStepsModel = gson.fromJson(String.valueOf(jsonArray), RawStepsModel[].class);

        if(rootCode.contains("null")) rootCode = "STK#";

        StringBuilder stepSuffix = new StringBuilder();
        ArrayList<String> stepsVariableLabels = new ArrayList<>();
        ArrayList<String> stepsVariableDesc = new ArrayList<>();

        for(RawStepsModel model : rawStepsModel) {
            stepSuffix.append("*").append(model.getValue());
            if(BuildConfig.FLAVOR.equals(PRO_VARIANT)) {
                if(model.isIs_param() || model.getValue().equals("pin")) {
                    stepsVariableLabels.add(model.getValue());
                    stepsVariableDesc.add(model.getDescription());
                }
            }
            else {
                if(model.isIs_param()) {
                    stepsVariableLabels.add(model.getValue());
                    stepsVariableDesc.add(model.getDescription());
                }
            }

        }
        //Taking substring of root code to remove the last #. E.g *737# to become *737
        String readableStep = (rootCode.length() > 0 ? rootCode.substring(0, rootCode.length() - 1) : "") + stepSuffix + "#";
        return new StreamlinedStepsModel(readableStep, stepsVariableLabels, stepsVariableDesc);
    }

    public static ActionRunStatus actionHasAllVariablesFilled(Context c, String actionId, int expectedSize) {
        Pair<Boolean, Map<String, String>> pair = getInitialVariableData(c, actionId);
        Map<String, String> variables = pair.second;
        int filledSize = 0;

        assert  variables !=null;
        for(String value : variables.values()) {
            //REPLACE WHITESPACES TO PREVENT USER ENTER JUST SPACES, WHICH CAN CAUSE ERROR.
            if(value !=null) {
                if(!TextUtils.isEmpty(value.replace(" ", ""))) filledSize  = filledSize + 1;
            }
        }

        if(!BuildConfig.FLAVOR.equals(PRO_VARIANT)) filledSize = filledSize+0;
        Log.d("VAR_SIZE", "expected size: "+expectedSize + ", while filled size is: "+filledSize);
        if(expectedSize == filledSize) return ActionRunStatus.GOOD;
        if(pair.first != null) { if (pair.first) return  ActionRunStatus.SKIPPED; }
        return ActionRunStatus.BAD;
    }

    public static void saveActionVariable(Context c,  String label, String value, String actionId) {
        ActionVariablesDBModel dbModel = ActionVariablesDBModel.create(Utils.getSavedString(actionId, c));
        Map<String, String> mapper;
        if(dbModel == null) mapper = new HashMap<>();
        else {
            if(dbModel.getVarMap() == null) mapper = new HashMap<>();
            else mapper = dbModel.getVarMap();
        }

        mapper.put(label, value.trim());
        Utils.saveString(actionId, new ActionVariablesDBModel(mapper, false).serialize(), c);
    }
    public static void saveSkippedVariable(Context c, String actionId) {
        ActionVariablesDBModel dbModel = ActionVariablesDBModel.create(Utils.getSavedString(actionId, c));
        Map<String, String> mapper;
        if(dbModel == null) mapper = new HashMap<>();
        else {
            if(dbModel.getVarMap() == null) mapper = new HashMap<>();
            else mapper = dbModel.getVarMap();
        }

        mapper.put("", "");
        Utils.saveString(actionId, new ActionVariablesDBModel(mapper, true).serialize(), c);
    }

    public static Pair<Boolean, Map<String, String>> getInitialVariableData(Context c, String actionId) {
        ActionVariablesDBModel model = ActionVariablesDBModel.create(Utils.getSavedString(actionId, c));
        if(model == null) return new Pair<>(false,  new HashMap<>());
        return new Pair<>(model.isSkipped(), model.getVarMap());
    }

    public static boolean validateEmail(String string) {
        if(string == null) return  false;
        return string.matches("(?:[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])");
    }
    public static boolean validatePassword (String string) {
        if(string == null) return  false;
        return string.length() < 40 && string.length() >4 && !string.contains(" ");
    }

    public static String[] convertNormalJSONArrayToStringArray(JSONArray arr) throws JSONException {
        if(arr == null) return new String[]{};
        String[] list = new String[arr.length()];
        for(int i = 0; i < arr.length(); i++){
            list[i] = arr.getString(i);
        }
        return list;
    }

    public static StatusEnums getStatusByString(String status) {
        StatusEnums statusEnums;
        switch (status) {
            case HOVER_TRANSAC_FAILED : statusEnums = StatusEnums.UNSUCCESSFUL;
                break;
            case HOVER_TRANSAC_PENDING: statusEnums =   StatusEnums.PENDING;
                break;
            default: statusEnums = StatusEnums.SUCCESS;
                break;
        }
        return statusEnums;
    }

    public static String formatDate(long timestamp) {
        String pattern = "HH:mm:ss (z) MMM dd, yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return simpleDateFormat.format(timestamp);
    }

    public static String formatDateV2(@Nullable long timestamp) {

        String pattern = "MMM dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return simpleDateFormat.format(timestamp);
    }

    public static String formatDateV3(long timestamp) {
        String pattern = "MMM dd, yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return simpleDateFormat.format(timestamp);
    }



    public static boolean isActionHasCompletedVariables(final ActionsModel runnableModel, final Context c) {
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = new JSONArray(runnableModel.getJsonArrayToString());
        }
        catch (Exception ignored) { }

        int variableSize = Utils.getStreamlinedStepsStepsFromRaw(runnableModel.getRootCode(), jsonArray).getStepVariableLabel().size();
        ActionRunStatus status = Utils.actionHasAllVariablesFilled(c, runnableModel.getActionId(), variableSize);
        return status == ActionRunStatus.GOOD;
    }


    public static String envValueToString(int env) {
        String string = "";
        switch (env) {
            case 0: string = "Normal";
                break;
            case 1: string = "Debug";
                break;
            default: string = "No-SIM";
                break;
        }
        return string;
    }

    public static String nullToString(Object value) {
        if(value == null) return  "None";
        else return value.toString();
    }

    public static List<?> removeDuplicatesFromList(List<?> list) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list = list.stream().distinct().collect(Collectors.toList());
        } else {
            LinkedHashSet<Object> hashSet = new LinkedHashSet<>(list);
            list = new ArrayList<>(hashSet);
        }
        return list;
    }
    static public  Object nonNullDateRange(Object value) {
        if(value == null) return 0;
        else return value;
    }

    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getDeviceId(Context c) {
        try {
            if (SettingsHelper.hasPermissions(c, new String[]{ Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE})) {
                String id = null;
                if (Build.VERSION.SDK_INT < 29) {
                    try {
                        id = ((TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                    } catch (Exception ignored) {}
                }
                if (id == null) { id = Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ANDROID_ID); }
                return id;
            }
        } catch (SecurityException ignored) {  }
        return c.getString(R.string.hsdk_unknown_device_id);
    }
}
