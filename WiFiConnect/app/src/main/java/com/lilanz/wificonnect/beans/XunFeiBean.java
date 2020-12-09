package com.lilanz.wificonnect.beans;

import java.util.List;


public class XunFeiBean {
    public int sn;

    public boolean ls;

    public int bg;

    public int ed;

    public List<Ws> ws;

    public class Cw {
        public int sc;

        public String w;
    }

    public class Ws {
        public int bg;

        public List<Cw> cw;
    }
}
