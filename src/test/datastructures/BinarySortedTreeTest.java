package datastructures;

import my.education.sortedbinarytree.BinarySortedTree;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.lang.System.*;

public class BinarySortedTreeTest {
    private Integer[] ints = new Integer[]{17, 2, 5,3, 8, 31, 23, 0};
    private BinarySortedTree<Integer> intTree = new BinarySortedTree<>();

    @Before
    public void init(){
        for (Integer i : ints) {
            intTree.add(i);
        }
    }
    @Test
    public void addTest() {
        intTree.add(-3);
        out.println();
    }

    @Test
    public void containsTest() {
        for (Integer i : ints) {
            Assert.assertNotNull(intTree.find(i));
            Assert.assertNull(intTree.find(i+1));
        }
    }

    @Test
    public void deleteTest(){
        Assert.assertFalse(intTree.delete(-3));
        intTree.add(-3);
        Assert.assertTrue(intTree.delete(-3));
        Assert.assertTrue(intTree.delete(2));
        out.println();
    }

    @Test
    public void depthVisitTest(){
        List<Integer> ints = intTree.visitInDepth();
        out.println();
    }
    @Test
    public void widthVisitTest(){
        List<Integer> ints = intTree.visitInWidth();
        out.println();
    }

    @Test
    public void util(){
//        As Tuesday is second day of week, so
        int dayOfWeekNow = 2;
        int dayOfWeekAfter100DaysAgo = 100%7 + 2;
        System.out.println(dayOfWeekAfter100DaysAgo);
    }
}
