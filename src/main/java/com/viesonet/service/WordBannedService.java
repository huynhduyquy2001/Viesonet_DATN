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

        List<String> swearingEnglish = Arrays.asList(
                "fuck",
                "fuck you",
                "shit",
                "piss off",
                "dick head",
                "asshole",
                "son of a bitch",
                "bastard",
                "bitch",
                "damn",
                "cunt",
                "blood",
                "hell",
                "piss",
                "suck",
                "freakin",
                "holy shit",
                "motherfucker",
                "nigga",
                "pussy",
                "slut",
                "cock",
                "crap",
                "bull shit",
                "jesus fuck",
                "shit ass",
                "shite");

        // Duyệt qua danh sách 'swearing' và thay thế từng từ nếu chúng xuất hiện trong
        // 'word'
        for (String bannedWord : swearing) {
            word = word.replaceAll("\\b" + bannedWord + "\\b", "***");
        }
        for (String bannedWord : swearingEnglish) {
            word = word.replaceAll("\\b" + bannedWord + "\\b", "***");
        }

        return word;
    }

}
