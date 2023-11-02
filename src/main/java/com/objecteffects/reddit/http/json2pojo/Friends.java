package com.objecteffects.reddit.http.json2pojo;

import java.util.ArrayList;

public class Friends {
    public String kind;
    public Data data;

    static public class Friend {
        public double date;
        public String rel_id;
        public String name;
        public String id;
    }

    static public class Data {
        public ArrayList<Friend> children;
    }
}
