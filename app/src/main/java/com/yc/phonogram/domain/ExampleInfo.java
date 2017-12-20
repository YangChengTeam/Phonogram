package com.yc.phonogram.domain;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by zhangkai on 2017/12/19.
 */

public class ExampleInfo {
    private int id;

    @JSONField(name = "ptic_id")
    private int pid;
    private String phonetic;
    private String word;
    @JSONField(name = "word_phonetic")
    private String wordPhonetic;

    private String letter;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordPhonetic() {
        return wordPhonetic;
    }

    public void setWordPhonetic(String wordPhonetic) {
        this.wordPhonetic = wordPhonetic;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}
