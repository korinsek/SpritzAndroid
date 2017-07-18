package app.si.kamino.readerapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.test.ActivityInstrumentationTestCase2;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by DenisK on 18. 07. 2017.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    MainActivity activity;
    Reader view;
    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        view = (Reader)activity.findViewById(R.id.readerView);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void useAppContext()  {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("app.si.kamino.readerapp", appContext.getPackageName());
    }

    @SmallTest
    public void testReaderView() {
        assertNotNull(view);
    }

    @SmallTest
    public void testWordsCount() {
        assertEquals(activity.getString(R.string.fairy_tale).split("\\s").length, view.words.size());
    }

    @SmallTest
    public void testWordsSequence() {
        String[] wordsInput = activity.getString(R.string.fairy_tale).split("\\s");
        String[] result = new String[view.words.size()];
        for (int i=0;i<result.length;i++)
            result[i] = view.words.get(i).getWord();
        assertArrayEquals(wordsInput,result);
    }
}