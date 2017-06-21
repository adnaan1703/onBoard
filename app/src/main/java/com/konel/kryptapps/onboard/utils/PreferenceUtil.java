package com.konel.kryptapps.onboard.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;


public final class PreferenceUtil extends Handler {
    // default values
    public static final String USER_NAME = "user_name";
    public static final String USER_ID = "user_id";
    public static final String FCM_TOKEN = "fcm_token";
    private static final String DEFAULT_STRING = "";
    private static final boolean DEFAULT_BOOLEAN = false;
    private static final int DEFAULT_INT = 0;
    private static final long DEFAULT_LONG = 0;
    private static final float DEFAULT_FLOAT = 0;
    // delay used if caller is called without begin transaction
    private static final long TRANSACTION_DELAY = 10;
    // preference name
    private static final String PREFERENCE_NAME = "uc";
    // event for handler to commit in SharedPreferences
    private static final int COMMIT_TRANSACTION = 100;
    // single instance
    private static PreferenceUtil sInstance;
    private final PreferenceWrapper wrapper;

    private PreferenceUtil(Context context) {
        wrapper = new PreferenceWrapper(context);
    }

    /**
     * Initialises the instance for {@link PreferenceUtil}
     *
     * @param context application context to initialize the SharedPreferences
     */
    public static void init(Context context) {
        if (null == sInstance) {
            sInstance = new PreferenceUtil(context);
        }
    }

    /**
     * Gets an already created instance of {@link PreferenceUtil}
     *
     * @return returns an created instance
     * @throws IllegalStateException if {@link PreferenceUtil#init(Context)} is not called first
     */
    private static PreferenceUtil currentInstance() {
        if (null == sInstance) {
            throw new IllegalStateException("PreferenceUtil init must be called first");
        }
        return sInstance;
    }

    /**
     * Gets int value from SharedPreferences
     *
     * @param key key for the value
     * @return value stored in SharedPreferences, else {@value #DEFAULT_INT} if not found
     */
    public static int getInt(String key) {
        return getInt(key, DEFAULT_INT);
    }

    /**
     * Gets int value from SharedPreferences
     *
     * @param key      key for the value
     * @param defValue default value if not present
     * @return value stored in SharedPreferences, else defValue if not found
     */
    public static int getInt(String key, int defValue) {
        return currentInstance().getWrapper().getValue(key, defValue);
    }

    /**
     * Sets int value in SharedPreferences
     *
     * @param key   key for the value
     * @param value value to be stored
     */
    public static void setInt(String key, int value) {
        currentInstance().getWrapper().setValue(key, value);
    }

    /**
     * Gets float value from SharedPreferences
     *
     * @param key key for the value
     * @return value stored in SharedPreferences, else {@value #DEFAULT_FLOAT} if not found
     */
    public static float getFloat(String key) {
        return getFloat(key, DEFAULT_FLOAT);
    }

    /**
     * Gets float value from SharedPreferences
     *
     * @param key      key for the value
     * @param defValue default value if not present
     * @return value stored in SharedPreferences, else @defValue if not found
     */
    public static float getFloat(String key, float defValue) {
        return currentInstance().getWrapper().getValue(key, defValue);
    }

    /**
     * Sets float value in SharedPreferences
     *
     * @param key   key for the value
     * @param value value to be stored
     */
    public static void setFloat(String key, float value) {
        currentInstance().getWrapper().setValue(key, value);
    }

    /**
     * Gets long value from SharedPreferences
     *
     * @param key key for the value
     * @return value stored in SharedPreferences, else {@value #DEFAULT_LONG} if not found
     */
    public static long getLong(String key) {
        return getLong(key, DEFAULT_LONG);
    }

    /**
     * Gets long value from SharedPreferences
     *
     * @param key      key for the value
     * @param defValue default value if not present
     * @return value stored in SharedPreferences, else @defValue if not found
     */
    public static long getLong(String key, long defValue) {
        return currentInstance().getWrapper().getValue(key, defValue);
    }

    /**
     * Sets long value in SharedPreferences
     *
     * @param key   key for the value
     * @param value value to be stored
     */
    public static void setLong(String key, long value) {
        currentInstance().getWrapper().setValue(key, value);
    }

    /**
     * Gets boolean value from SharedPreferences
     *
     * @param key key for the value
     * @return value stored in SharedPreferences, else {@value #DEFAULT_BOOLEAN} if not found
     */
    public static boolean getBoolean(String key) {
        return getBoolean(key, DEFAULT_BOOLEAN);
    }

    /**
     * Gets boolean value from SharedPreferences
     *
     * @param key      key for the value
     * @param defValue default value if not present
     * @return value stored in SharedPreferences, else @defValue if not found
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return currentInstance().getWrapper().getValue(key, defValue);
    }

    /**
     * Sets boolean value in SharedPreferences
     *
     * @param key   key for the value
     * @param value value to be stored
     */
    public static void setBoolean(String key, boolean value) {
        currentInstance().getWrapper().setValue(key, value);
    }

    /**
     * Gets String value from SharedPreferences
     *
     * @param key key for the value
     * @return value stored in SharedPreferences, else {@value #DEFAULT_STRING} if not found
     */
    public static String getString(String key) {
        return getString(key, DEFAULT_STRING);
    }

    /**
     * Gets boolean value from SharedPreferences
     *
     * @param key      key for the value
     * @param defValue default value if not present
     * @return value stored in SharedPreferences, else @defValue if not found
     */
    public static String getString(String key, String defValue) {
        return currentInstance().getWrapper().getValue(key, defValue);
    }

    /**
     * Sets boolean value in SharedPreferences
     *
     * @param key   key for the value
     * @param value value to be stored
     */
    public static void setString(String key, String value) {
        currentInstance().getWrapper().setValue(key, value);
    }

    /**
     * Removes key from SharedPreferences
     *
     * @param key which has to removed from SharedPreferences
     */
    public static void remove(String key) {
        currentInstance().getWrapper().setValue(key, null);
    }

    public static boolean contains(String key) {
        return currentInstance().getWrapper().contains(key);
    }

    PreferenceWrapper getWrapper() {
        return wrapper;
    }

    static class PreferenceWrapper extends Handler {
        private static final HandlerThread PERSIST_THREAD;

        static {
            PERSIST_THREAD = new HandlerThread(PreferenceUtil.class.getSimpleName());
            PERSIST_THREAD.setPriority(HandlerThread.MIN_PRIORITY);
            PERSIST_THREAD.start();
        }

        private final Context context;
        private final Map<String, Object> writeMap;
        private final ReentrantLock writeLock;

        public PreferenceWrapper(Context context) {
            this.context = context;
            writeMap = new HashMap<>();
            writeLock = new ReentrantLock();
        }


        public <T> T getValue(String key, T defaultValue) {
            writeLock.lock();
            //Logger.logD(this, "getValue : key %s, value %s", key, defaultValue);
            T result = defaultValue;
            try {
                if (writeMap.containsKey(key)) {
                    Object o = writeMap.get(key);
                    // if null key is set to delete
                    if (null != o) {
                        // found case
                        result = (T) o;
                    }
                } else {
                    Object o = getPreference().getAll().get(key);
                    if (null != o) {
                        // found case
                        result = (T) o;
                    }
                }
            } finally {
                //Logger.logD(this, "getValue : value returned %s", result);
                writeLock.unlock();
            }
            return result;
        }

        private SharedPreferences getPreference() {
            return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

        public void setValue(String key, Object value) {
            writeLock.lock();
            try {
                writeMap.put(key, value);
            } finally {
                requestCommit();
                writeLock.unlock();
            }
        }

        public boolean contains(String key) {
            writeLock.lock();
            boolean value = false;
            try {
                value = writeMap.containsKey(key) || getPreference().contains(key);
            } finally {
                writeLock.unlock();
            }
            return value;
        }

        private void requestCommit() {
            removeMessages(COMMIT_TRANSACTION);
            sendEmptyMessageDelayed(COMMIT_TRANSACTION, TRANSACTION_DELAY);
        }

        @SuppressLint("CommitPrefEdits")
        @Override
        public void handleMessage(Message msg) {
            writeLock.lock();

            SharedPreferences.Editor edit = getPreference().edit();
            try {
                Set<Map.Entry<String, Object>> entries = writeMap.entrySet();
                for (Map.Entry<String, Object> entry : entries) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (value instanceof String) {
                        edit.putString(key, (String) value);
                    } else if (value instanceof Boolean) {
                        edit.putBoolean(key, (Boolean) value);
                    } else if (value instanceof Integer) {
                        edit.putInt(key, (Integer) value);
                    } else if (value instanceof Long) {
                        edit.putLong(key, (Long) value);
                    } else if (value instanceof Float) {
                        edit.putFloat(key, (Float) value);
                    } else {
                        edit.remove(key);
                    }
                }
            } finally {
                edit.commit();
                writeMap.clear();
                writeLock.unlock();
            }
        }
    }
}