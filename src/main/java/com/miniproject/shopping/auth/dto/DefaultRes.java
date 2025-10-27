package com.miniproject.auth.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultRes {
    private Boolean result;
    private String message;
    private String status;

    public static DefaultRes success() {
        return new DefaultRes(null, "success", "200");
    }

    public static DefaultRes fail() {
        return new DefaultRes(null, "fail", "500");
    }



    public static DefaultRes checkDuplicateFail(){
        return new DefaultRes(false,"중복되었습니다.","200");
    }

    public static DefaultRes checkDuplicateFail(String message){
        return new DefaultRes(false, message,"200");
    }

    public static DefaultRes checkDuplicateSuccess(){
        return new DefaultRes(true,"success","200");
    }
}
