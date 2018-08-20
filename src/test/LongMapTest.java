import my.education.longmap.LongMap;
import my.education.longmap.LongMapImpl;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class LongMapTest {
    private final String TEST_STRING = "testString";
    private final long ONE = 1L;

    @Test
    public void successfulPutAndGetTest() {
//        Given
        LongMapImpl<String> strings = new LongMapImpl<>();
//        When
        strings.put(ONE, TEST_STRING);
//        Then
        Assert.assertEquals(strings.get(ONE), TEST_STRING);
    }

    @Test(expected = NoSuchElementException.class)
    public void successfulPutAndUnsuccessfulGetTest() {
//        Given
        LongMapImpl<String> strings = new LongMapImpl<>();
//        When
        strings.put(ONE, TEST_STRING);
//        Then should fail with exception
        Assert.assertEquals(strings.get(ONE + 1L), TEST_STRING);
    }

    /*So pity, but there's only 12Gb of RAM on my local machine, and it's not enough  to allocate an array of Node[Integer.MAX_VALUE].
    Even array of long[Integer.MAX_VALUE] is about 8bytes*2147... and  larger then 16G.
    So this test has never run.(((
    * */
    @Ignore
    @Test(expected = InvalidStateException.class)
    public void whenNoMoreRoomToPutNewElementTest() {
//        Given
        LongMapImpl<String> strings = new LongMapImpl<>(Integer.MAX_VALUE);
//        When
        for (long l = 0; l <= Integer.MAX_VALUE + 1L; l++) {
            strings.put(l, "");
        }
    }

    @Test
    public void keysTest() {
//        Given
        LongMapImpl<String> strings = new LongMapImpl<>();
        long[] expected = new long[128];
        for (int i = 0; i < 128; i++) {
            expected[i] = (long) i + Integer.MAX_VALUE;
        }
//        When
        for (long l = Integer.MAX_VALUE; l < Integer.MAX_VALUE + 128L; l++) {
            strings.put(l, "");
        }
        long[] actual = strings.keys();
        Arrays.sort(actual);
//        Then
        Assert.assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    public void keysAfterRemoveElementTest() {
//        Given
        LongMapImpl<String> strings = new LongMapImpl<>();
        long[] expected = new long[128];
        for (int i = 0; i < 128; i++) {
            expected[i] = (long) i + Integer.MAX_VALUE;
        }
        expected[1] = 0;
//        When
        for (long l = Integer.MAX_VALUE; l < Integer.MAX_VALUE + 128L; l++) {
            strings.put(l, "");
        }
        strings.remove(Integer.MAX_VALUE + ONE);
        long[] actual = strings.keys();
        Arrays.sort(actual);
        Arrays.sort(expected);
//        Then
        Assert.assertTrue(Arrays.equals(expected, actual));
    }


    @Test
    public void valuesTest() {
        //        Given
        LongMapImpl<String> strings = new LongMapImpl<>();
        String[] expected = new String[128];
        for (int i = 0; i < 128; i++) {
            expected[i] = String.valueOf((long) i + Integer.MAX_VALUE);
        }
//        When
        for (long l = Integer.MAX_VALUE; l < Integer.MAX_VALUE + 128L; l++) {
            strings.put(l, String.valueOf(l));
        }
        Object[] objects = strings.values();
        String[] actual = new String[objects.length];
        for (int i = 0; i < objects.length; i++) {
            actual[i] = (String) objects[i];
        }
        Arrays.sort(actual);
//        Then
        Assert.assertTrue(Arrays.equals(expected, actual));
    }

    @Test
    public void containsKeyTest() {
//        Given
        LongMapImpl<String> strings = new LongMapImpl<>();
//        When
        strings.put(ONE, TEST_STRING);
//        Then
        Assert.assertTrue(strings.containsKey(ONE));
        Assert.assertFalse(strings.containsKey(ONE + 1));
    }

    @Test
    public void containsValueTest() {
//        Given
        LongMapImpl<String> strings = new LongMapImpl<>();
//        When
        strings.put(ONE, TEST_STRING);
//        Then
        Assert.assertTrue(strings.containsValue(TEST_STRING));
        Assert.assertFalse(strings.containsValue("  "));
    }

    @Test
    public void putWithLongKeysLoadTest() {
        long start = System.currentTimeMillis();
        LongMapImpl<String> strings = new LongMapImpl<>(Integer.MAX_VALUE / 256);
        for (long l = Integer.MAX_VALUE + ONE; l < Integer.MAX_VALUE + Integer.MAX_VALUE / 256L; l++) {
            strings.put(l, String.valueOf(l));
        }
        long stop = System.currentTimeMillis();
        System.out.printf("Method: putWithLongKeysLoadTest. Took time: %d \n", (stop - start));
    }

    @Test
    public void putWithIntKeysLoadTest() {
        long start = System.currentTimeMillis();
        LongMapImpl<String> strings = new LongMapImpl<>(128);
        for (long l = 0; l < Integer.MAX_VALUE / 256; l++) {
            strings.put(l, String.valueOf(l));
        }
        long stop = System.currentTimeMillis();
        System.out.printf("Method: putWithIntKeysLoadTest. Took time: %d \n", (stop - start));
    }

    @Test
    public void successfulRemoveTest() {
//        Given
        LongMap<String> strings = new LongMapImpl<>();
        strings.put(ONE, TEST_STRING);
//        When
        strings.remove(ONE);
//        Then
        Assert.assertTrue(strings.isEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void unsuccessfulRemoveTest() {
//        Given
        LongMap<String> strings = new LongMapImpl<>();
        strings.put(ONE, TEST_STRING);
//        When
        strings.remove(ONE + 1L);
    }

    @Test
    public void isEmptyTest() {
//        Given
        LongMap<String> strings = new LongMapImpl<>();
        Assert.assertTrue(strings.isEmpty());
//        When
        strings.put(ONE, TEST_STRING);
//        Then
        Assert.assertFalse(strings.isEmpty());
    }

    @Test
    public void sizeTest() {
//       Given
        LongMap<String> strings = new LongMapImpl<>();
        Assert.assertTrue(strings.size() == 0);
//       When
        strings.put(ONE, TEST_STRING);
//       Then
        Assert.assertFalse(strings.size() == 0);
    }

    @Test
    public void clearTest() {
//       Given
        LongMap<String> strings = new LongMapImpl<>();
        strings.put(ONE, TEST_STRING);
        Assert.assertFalse(strings.isEmpty());
//       When
        strings.clear();
//       Then
        Assert.assertTrue(strings.isEmpty());
    }

/*
        Just to compare the time and memory consumption
*/

    @Test
    public void putHashMapLoadTest() {
        long start = System.currentTimeMillis();
        Map<Long, String> strings = new HashMap<>(Integer.MAX_VALUE / 128);
        for (long l = 0; l < Integer.MAX_VALUE / 128; l++) {
            strings.put(l, String.valueOf(l));
        }
        long stop = System.currentTimeMillis();
        System.out.printf("Method: putHashMapLoadTest. Took time: %d \n", (stop - start));
    }
}