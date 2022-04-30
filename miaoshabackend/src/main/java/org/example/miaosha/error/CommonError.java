package org.example.miaosha.error;

public interface CommonError {

    int getErrCode();

    String getErrMsg();

    CommonError setErrMsg(String errMsg);

}
