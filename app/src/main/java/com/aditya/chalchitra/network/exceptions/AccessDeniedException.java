package com.aditya.chalchitra.network.exceptions;


import com.aditya.chalchitra.constants.AppConstant;

public class AccessDeniedException extends HttpException {
    public AccessDeniedException() {
        super(AppConstant.EXCEPTION.ACCESS_DENIED);
    }
}
