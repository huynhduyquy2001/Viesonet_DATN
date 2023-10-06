package com.viesonet.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class WordBannedService {

    public String wordBanned(String word) {
        List<String> swearing = Arrays.asList(
                "cac",
                "lon",
                "cax",
                "loz",
                "chết",
                "giết",
                "đâm",
                "chém",
                "đĩ",
                "hiếp dâm",
                "suc vat",
                "ba me may",
                "dcm",
                "dcmm",
                "cmm",
                "con cho nay",
                "thang cho nay",
                "súc vật",
                "bà mẹ mày",
                "dcm",
                "dcmm",
                "cmm",
                "con chó",
                "con chó này",
                "thằng chó này",
                "rẻ rách",
                "rác rưởi");

        // Thực hiện xử lý với danh sách 'swearing' ở đây

        // Duyệt qua danh sách 'swearing' và thay thế từng từ nếu chúng xuất hiện trong
        // 'word'
        for (String bannedWord : swearing) {
            word = word.replaceAll("\\b" + bannedWord + "\\b", "***");
        }

        return word;
    }

}
