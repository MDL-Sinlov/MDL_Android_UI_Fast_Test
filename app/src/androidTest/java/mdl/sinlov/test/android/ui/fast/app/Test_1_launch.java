package mdl.sinlov.test.android.ui.fast.app;

import mdl.sinlov.test.android.ui.fast.FastTest;
import mdl.sinlov.test.android.ui.fast.MDLTestSet;
import mdl.sinlov.test.android.ui.fast.ThirdTestJob;

/**
 * <pre>
 *     sinlov
 *
 *     /\__/\
 *    /`    '\
 *  ≈≈≈ 0  0 ≈≈≈ Hello world!
 *    \  --  /
 *   /        \
 *  /          \
 * |            |
 *  \  ||  ||  /
 *   \_oo__oo_/≡≡≡≡≡≡≡≡o
 *
 * </pre>
 * Created by "sinlov" on 16/7/28.
 */
public class Test_1_launch extends MDLTestSet {
    public void test_base() throws Exception {
        setTestTimes(5);
        setAutoClose(true);
        FastTest f = new FastTest() {
            @Override
            public void task() {
                mySolo().sleep(1000);
            }
        };
        fastTest(f);
    }

    public void test_ThirdJoB() throws Exception{
        setTestTimes(5);
        setAutoClose(true);
        ThirdTestJob t = new ThirdTestJob() {
            @Override
            public boolean childTaskBefore() {
                mySolo().sleep(3000);
                return false;
            }

            @Override
            public boolean childTaskRunning() {
                mySolo().sleep(3000);
                return false;
            }

            @Override
            public boolean childTaskAfter() {
                mySolo().sleep(3000);
                return false;
            }
        };
        fastTestAutoCount(t);
    }
}
