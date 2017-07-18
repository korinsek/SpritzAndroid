package app.si.kamino.readerapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by DenisK on 18. 07. 2017.
 */
public class SpritzTest {
    @Test
    public void PivotEmpty() {
        assertEquals(0,Spritz.GetPivot(""));
        assertEquals(0,Spritz.GetPivot("     "));
    }
    @Test
    public void PivotWords() {
        assertEquals(2,Spritz.GetPivot("palcek"));
        assertEquals(1,Spritz.GetPivot("smuk"));
        assertEquals(3,Spritz.GetPivot("grenajugpopir"));
        assertEquals(4,Spritz.GetPivot("in na cevape z kajmakom"));
    }
}