package com.aditya.chalchitra.network.exceptions;

import com.aditya.chalchitra.constants.AppConstant;

public class ServerErrorException extends HttpException {

    public ServerErrorException() {
        super(AppConstant.MESSAGE_SERVER_ERROR);
    }
}
