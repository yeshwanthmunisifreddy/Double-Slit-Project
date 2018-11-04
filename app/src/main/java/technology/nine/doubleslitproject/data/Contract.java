package technology.nine.doubleslitproject.data;

import android.provider.BaseColumns;

public class Contract {
    public Contract() {
    }
    static  class ImageRefrence implements BaseColumns{
        static final String  IMAGEREFERENCE_TABLE_NAME = "imageReference";
        static final String URI ="uri";
    }
}
