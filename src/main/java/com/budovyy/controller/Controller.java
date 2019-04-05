package com.budovyy.controller;

import com.budovyy.web.Request;
import com.budovyy.web.ViewModel;


public interface Controller {

    ViewModel process(Request req);

}
