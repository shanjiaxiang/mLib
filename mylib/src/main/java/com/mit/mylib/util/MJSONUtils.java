package com.mit.mylib.util;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public class MJSONUtils {

    public static boolean isPrintException = true;

    private MJSONUtils() {
        throw new Error("Do not need instantiate!");
    }

    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));

            String line;
            while((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public static Long getLong(JSONObject jsonObject, String key, Long defaultValue) {
        if(jsonObject != null && !MStringUtils.isEmpty(key)) {
            try {
                return Long.valueOf(jsonObject.getLong(key));
            } catch (JSONException var4) {
                if(isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static Long getLong(String jsonData, String key, Long defaultValue) {
        if(MStringUtils.isEmpty(jsonData)) {
            return defaultValue;
        } else {
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                return getLong(jsonObject, key, defaultValue);
            } catch (JSONException var4) {
                if(isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        }
    }

    public static long getLong(JSONObject jsonObject, String key, long defaultValue) {
        return getLong(jsonObject, key, Long.valueOf(defaultValue)).longValue();
    }

    public static long getLong(String jsonData, String key, long defaultValue) {
        return getLong(jsonData, key, Long.valueOf(defaultValue)).longValue();
    }

    public static Integer getInt(JSONObject jsonObject, String key, Integer defaultValue) {
        if(jsonObject != null && !MStringUtils.isEmpty(key)) {
            try {
                return Integer.valueOf(jsonObject.getInt(key));
            } catch (JSONException var4) {
                if(isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static Integer getInt(String jsonData, String key, Integer defaultValue) {
        if(MStringUtils.isEmpty(jsonData)) {
            return defaultValue;
        } else {
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                return getInt(jsonObject, key, defaultValue);
            } catch (JSONException var4) {
                if(isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        }
    }

    public static int getInt(JSONObject jsonObject, String key, int defaultValue) {
        return getInt(jsonObject, key, Integer.valueOf(defaultValue)).intValue();
    }

    public static int getInt(String jsonData, String key, int defaultValue) {
        return getInt(jsonData, key, Integer.valueOf(defaultValue)).intValue();
    }

    public static Double getDouble(JSONObject jsonObject, String key, Double defaultValue) {
        if(jsonObject != null && !MStringUtils.isEmpty(key)) {
            try {
                return Double.valueOf(jsonObject.getDouble(key));
            } catch (JSONException var4) {
                if(isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static Double getDouble(String jsonData, String key, Double defaultValue) {
        if(MStringUtils.isEmpty(jsonData)) {
            return defaultValue;
        } else {
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                return getDouble(jsonObject, key, defaultValue);
            } catch (JSONException var4) {
                if(isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        }
    }

    public static double getDouble(JSONObject jsonObject, String key, double defaultValue) {
        return getDouble(jsonObject, key, Double.valueOf(defaultValue)).doubleValue();
    }

    public static double getDouble(String jsonData, String key, double defaultValue) {
        return getDouble(jsonData, key, Double.valueOf(defaultValue)).doubleValue();
    }

    public static String getString(JSONObject jsonObject, String key, String defaultValue) {
        if(jsonObject != null && !MStringUtils.isEmpty(key)) {
            try {
                return jsonObject.getString(key);
            } catch (JSONException var4) {
                if(isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static String getString(String jsonData, String key, String defaultValue) {
        if(MStringUtils.isEmpty(jsonData)) {
            return defaultValue;
        } else {
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                return getString(jsonObject, key, defaultValue);
            } catch (JSONException var4) {
                if(isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        }
    }

    public static String getStringCascade(JSONObject jsonObject, String defaultValue, String... keyArray) {
        if(jsonObject != null && keyArray != null && keyArray.length != 0) {
            String data = jsonObject.toString();
            String[] var4 = keyArray;
            int var5 = keyArray.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String key = var4[var6];
                data = getStringCascade(data, key, new String[]{defaultValue});
                if(data == null) {
                    return defaultValue;
                }
            }

            return data;
        } else {
            return defaultValue;
        }
    }

    public static String getStringCascade(String jsonData, String defaultValue, String... keyArray) {
        if(MStringUtils.isEmpty(jsonData)) {
            return defaultValue;
        } else {
            String data = jsonData;
            String[] var4 = keyArray;
            int var5 = keyArray.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String key = var4[var6];
                data = getString(data, key, defaultValue);
                if(data == null) {
                    return defaultValue;
                }
            }

            return data;
        }
    }

    public static String[] getStringArray(JSONObject jsonObject, String key, String[] defaultValue) {
        if(jsonObject != null && !MStringUtils.isEmpty(key)) {
            try {
                JSONArray statusArray = jsonObject.getJSONArray(key);
                if(statusArray == null) {
                    return defaultValue;
                } else {
                    String[] value = new String[statusArray.length()];

                    for(int i = 0; i < statusArray.length(); ++i) {
                        value[i] = statusArray.getString(i);
                    }

                    return value;
                }
            } catch (JSONException var6) {
                if(isPrintException) {
                    var6.printStackTrace();
                }

                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static String[] getStringArray(String jsonData, String key, String[] defaultValue) {
        if(MStringUtils.isEmpty(jsonData)) {
            return defaultValue;
        } else {
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                return getStringArray(jsonObject, key, defaultValue);
            } catch (JSONException var4) {
                if(isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        }
    }

    public static List<String> getStringList(JSONObject jsonObject, String key, List<String> defaultValue) {
        if(jsonObject != null && !MStringUtils.isEmpty(key)) {
            try {
                JSONArray statusArray = jsonObject.getJSONArray(key);
                if(statusArray == null) {
                    return defaultValue;
                } else {
                    List<String> list = new ArrayList();

                    for(int i = 0; i < statusArray.length(); ++i) {
                        list.add(statusArray.getString(i));
                    }

                    return list;
                }
            } catch (JSONException var6) {
                if(isPrintException) {
                    var6.printStackTrace();
                }

                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static List<String> getStringList(String jsonData, String key, List<String> defaultValue) {
        if(MStringUtils.isEmpty(jsonData)) {
            return defaultValue;
        } else {
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                return getStringList(jsonObject, key, defaultValue);
            } catch (JSONException var4) {
                if(isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        }
    }

    public static JSONObject getJSONObject(JSONObject jsonObject, String key, JSONObject defaultValue) {
        if(jsonObject != null && !MStringUtils.isEmpty(key)) {
            try {
                return jsonObject.getJSONObject(key);
            } catch (JSONException var4) {
                if(isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static JSONObject getJSONObject(String jsonData, String key, JSONObject defaultValue) {
        if(MStringUtils.isEmpty(jsonData)) {
            return defaultValue;
        } else {
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                return getJSONObject(jsonObject, key, defaultValue);
            } catch (JSONException var4) {
                if(isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        }
    }

    public static JSONObject getJSONObjectCascade(JSONObject jsonObject, JSONObject defaultValue, String... keyArray) {
        if(jsonObject != null && keyArray != null && keyArray.length != 0) {
            JSONObject js = jsonObject;
            String[] var4 = keyArray;
            int var5 = keyArray.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String key = var4[var6];
                js = getJSONObject(js, key, defaultValue);
                if(js == null) {
                    return defaultValue;
                }
            }

            return js;
        } else {
            return defaultValue;
        }
    }

    public static JSONObject getJSONObjectCascade(String jsonData, JSONObject defaultValue, String... keyArray) {
        if(MStringUtils.isEmpty(jsonData)) {
            return defaultValue;
        } else {
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                return getJSONObjectCascade(jsonObject, defaultValue, keyArray);
            } catch (JSONException var4) {
                if(isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        }
    }

    public static JSONArray getJSONArray(JSONObject jsonObject, String key, JSONArray defaultValue) {
        if(jsonObject != null && !MStringUtils.isEmpty(key)) {
            try {
                return jsonObject.getJSONArray(key);
            } catch (JSONException var4) {
                if(isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static JSONArray getJSONArray(String jsonData, String key, JSONArray defaultValue) {
        if(MStringUtils.isEmpty(jsonData)) {
            return defaultValue;
        } else {
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                return getJSONArray(jsonObject, key, defaultValue);
            } catch (JSONException var4) {
                if(isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue;
            }
        }
    }

    public static boolean getBoolean(JSONObject jsonObject, String key, Boolean defaultValue) {
        if(jsonObject != null && !MStringUtils.isEmpty(key)) {
            try {
                return jsonObject.getBoolean(key);
            } catch (JSONException var4) {
                if(isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue.booleanValue();
            }
        } else {
            return defaultValue.booleanValue();
        }
    }

    public static boolean getBoolean(String jsonData, String key, Boolean defaultValue) {
        if(MStringUtils.isEmpty(jsonData)) {
            return defaultValue.booleanValue();
        } else {
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                return getBoolean(jsonObject, key, defaultValue);
            } catch (JSONException var4) {
                if(isPrintException) {
                    var4.printStackTrace();
                }

                return defaultValue.booleanValue();
            }
        }
    }

    public static Map<String, String> getMap(JSONObject jsonObject, String key) {
        return parseKeyAndValueToMap(getString((JSONObject)jsonObject, key, (String)null));
    }

    public static Map<String, String> getMap(String jsonData, String key) {
        if(jsonData == null) {
            return null;
        } else if(jsonData.length() == 0) {
            return new HashMap();
        } else {
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                return getMap(jsonObject, key);
            } catch (JSONException var3) {
                if(isPrintException) {
                    var3.printStackTrace();
                }

                return null;
            }
        }
    }

    public static Map<String, String> parseKeyAndValueToMap(JSONObject sourceObj) {
        if(sourceObj == null) {
            return null;
        } else {
            Map<String, String> keyAndValueMap = new HashMap();
            Iterator iter = sourceObj.keys();

            while(iter.hasNext()) {
                String key = (String)iter.next();
                keyAndValueMap.put(key, getString(sourceObj, key, ""));
            }

            return keyAndValueMap;
        }
    }

    public static Map<String, String> parseKeyAndValueToMap(String source) {
        if(MStringUtils.isEmpty(source)) {
            return null;
        } else {
            try {
                JSONObject jsonObject = new JSONObject(source);
                return parseKeyAndValueToMap(jsonObject);
            } catch (JSONException var2) {
                if(isPrintException) {
                    var2.printStackTrace();
                }

                return null;
            }
        }
    }
}
