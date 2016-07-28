package mdl.sinlov.test.android.ui.fast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Toast;

import com.robotium.solo.Solo;

import java.util.Locale;


@SuppressLint("NewApi")
@SuppressWarnings("rawtypes")
public class MDLTestSet extends ActivityInstrumentationTestCase2 {

    public static final int DEFAULT_SLEEP_TIME = 1000;
    public static final int DEFAULT_RESULT_TIME = 10000;
    public static final String EN_MSG_TEST_FINISH = "Test finish";
    public static final String EN_MSG_TEST_RUN_TIMES = "Run times: ";
    public static final String EN_MSG_TEST_TIME_FULL = "Time full: ";
    public static final String EN_MSG_TEST_TIME_PRE = "Time pre.: ";
    public static final String EN_MSG_TEST_USE_TIME = " |Use time: ";
    public static final String EN_MSG_TEST_TIME_UTIL = " s.";
    private static String EXCEPTION_HEAD = " exception ";
    private static String EXCEPTION_END = MDLTestSet.class.getName();
    private static String EXCEPTION_SET_ERROR = "Set error" + EXCEPTION_HEAD + EXCEPTION_END;
    private static String EXCEPTION_UI_THREAD_ERROR = "Test UI Thread errror" + EXCEPTION_HEAD + EXCEPTION_END;

    private static Solo mySolo;
    private static Activity myActivity;
    private static Class<?> launchActivityClass;

    /**
     * The default test project cycles
     */
    private int testTimes = 1;

    /**
     * the application can automatically shut down
     */
    private static boolean isAutoClose;

    private static ThirdTestJob testThirdJob;
    private static FastTest fastTest;

    public String getLocaleLanguage() {
        Locale l = Locale.getDefault();
        return String.format("%s-%s", l.getLanguage(), l.getCountry());
    }

    /**
     * Run a thread to be tested APP main thread
     *
     * @param activity {@link Activity}
     * @param runnable {@link Runnable}
     */
    protected void runSingleThreadInMain(Activity activity, Runnable runnable) {
        activity.runOnUiThread(runnable);
    }

    /**
     * UI process establishes a main thread, exhaled a pop-up window
     *
     * @param context  {@link Context}
     * @param showWord {@link String}
     * @return {@link Runnable}
     */
    protected Runnable uiThreadMakeToast(final Context context, final String showWord) {
        return new Runnable() {

            @Override
            public void run() {
                try {
                    Toast.makeText(context, showWord, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, EXCEPTION_UI_THREAD_ERROR, Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MDLTestSet.myActivity = this.getActivity();
        MDLTestSet.mySolo = new Solo(getInstrumentation(), getActivity());
    }

    public void setTestTimes(int testTimes) {
        this.testTimes = testTimes;
    }


    /**
     * set to automatically turn off
     *
     * @param isAutoClose boolean
     */
    public static void setAutoClose(boolean isAutoClose) {
        MDLTestSet.isAutoClose = isAutoClose;
    }

    @Override
    public void tearDown() throws Exception {
        try {
            if (null != MDLTestSet.mySolo && null != MDLTestSet.myActivity) {
                MDLTestSet.mySolo.finishOpenedActivities();
                MDLTestSet.myActivity.finish();
            }
            super.tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Automatic shutdown test applications
     *
     * @throws Exception
     */
    protected void autoClose() throws Exception {
        if (isAutoClose) {
            tearDown();
        } else {
            mySolo.sleep(Integer.MAX_VALUE);
        }
    }

    public void fastTest(FastTest fastTest) throws Exception {
        if (null == fastTest) {
            new Throwable(EXCEPTION_SET_ERROR).printStackTrace();
        } else {
            fastTest.task();
        }
    }

    public void fastTest(ThirdTestJob testJob) throws Exception {
        if (null == fastTest) {
            new Throwable(EXCEPTION_SET_ERROR).printStackTrace();
        } else {
            if (testJob.childTaskBefore()) {
                return;
            } else {
                if (testJob.childTaskRunning()) {
                    return;
                } else {
                    if (testJob.childTaskAfter()) {
                        return;
                    }
                }
            }
        }
    }

    public void fastTestAutoCount(FastTest fastTest) throws Exception {
        if (null == fastTest) {
            new Throwable(EXCEPTION_SET_ERROR).printStackTrace();
        } else {
            MDLTestSet.fastTest = fastTest;
        }
        int testTimesCount = 0;
        float totalTime = 0;
        for (int i = 0; i < this.testTimes; i++) {
            testTimesCount++;
            setUp();
            long runTime = System.currentTimeMillis();
            MDLTestSet.fastTest.task();
            float useTime = (System.currentTimeMillis() - runTime) / 1000.0f;
            totalTime = totalTime + useTime;
            System.out.println(EN_MSG_TEST_RUN_TIMES + testTimesCount + EN_MSG_TEST_USE_TIME + useTime + EN_MSG_TEST_TIME_UTIL);
            autoClose();
        }
        setUp();
        float preTime = totalTime / (float) testTimesCount;
        String word = EN_MSG_TEST_FINISH +
                EN_MSG_TEST_RUN_TIMES + testTimesCount + EN_MSG_TEST_TIME_UTIL + "\n" +
                EN_MSG_TEST_TIME_FULL + totalTime + EN_MSG_TEST_TIME_UTIL + "\n" +
                EN_MSG_TEST_TIME_PRE + preTime + EN_MSG_TEST_TIME_UTIL;
        System.err.println(word);
        runSingleThreadInMain(myActivity, uiThreadMakeToast(myActivity, word));
        mySolo.sleep(DEFAULT_RESULT_TIME);
        tearDown();
    }

    /**
     * Automatic test, and additional timing
     *
     * @throws Exception
     */
    public void fastTestAutoCount(ThirdTestJob testJob) throws Exception {
        if (null == testJob) {
            new Throwable(EXCEPTION_SET_ERROR).printStackTrace();
        } else {
            MDLTestSet.testThirdJob = testJob;
        }
        int testTimesCount = 0;
        float totalTime = 0;
        for (int i = 0; i < this.testTimes; i++) {
            testTimesCount++;
            setUp();
            long runTime = System.currentTimeMillis();
            if (MDLTestSet.testThirdJob.childTaskBefore()) {
                return;
            } else {
                if (MDLTestSet.testThirdJob.childTaskRunning()) {
                    return;
                } else {
                    if (MDLTestSet.testThirdJob.childTaskAfter()) {
                        return;
                    }
                }
            }
            float useTime = (System.currentTimeMillis() - runTime) / 1000.0f;
            totalTime = totalTime + useTime;
            System.out.println(EN_MSG_TEST_RUN_TIMES + testTimesCount + EN_MSG_TEST_USE_TIME + useTime + EN_MSG_TEST_TIME_UTIL);
            autoClose();
        }
        setUp();
        float preTime = totalTime / (float) testTimesCount;
        String word = EN_MSG_TEST_FINISH +
                EN_MSG_TEST_RUN_TIMES + testTimesCount + EN_MSG_TEST_TIME_UTIL + "\n" +
                EN_MSG_TEST_TIME_FULL + totalTime + EN_MSG_TEST_TIME_UTIL + "\n" +
                EN_MSG_TEST_TIME_PRE + preTime + EN_MSG_TEST_TIME_UTIL;
        System.err.println(word);
        runSingleThreadInMain(myActivity, uiThreadMakeToast(myActivity, word));
        mySolo.sleep(DEFAULT_RESULT_TIME);
        tearDown();
    }

    public static Solo mySolo() {
        return mySolo;
    }

    public static Activity myActivity() {
        return myActivity;
    }

    static {
        try {
            launchActivityClass = Class.forName(MDLTestCont.LAUNCH_ACTIVITY);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Before the test must be set up app package and Launcher activity
     */
    @SuppressWarnings("unchecked")
    public MDLTestSet() {
        super(launchActivityClass);
        testThirdJob = null;
    }
}
