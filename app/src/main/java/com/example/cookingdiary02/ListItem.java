package com.example.cookingdiary02;

//ListView의 아이템에 표시될 데이터 클래스 정의
public class ListItem {

    private int txt1;
    private String txt2, txt3;

    public int getTxt1() {
        return this.txt1;
    }

    public void setTxt1(int t1) {
        this.txt1 = t1;
    }

    public String getTxt2() {
        return this.txt2;
    }

    public void setTxt2(String t2) {
        this.txt2 = t2;
    }

    public String getTxt3() {
        return this.txt3;
    }

    public void setTxt3(String t3) {
        this.txt3 = t3;
    }

}