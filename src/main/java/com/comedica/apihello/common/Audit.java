package com.comedica.apihello.common;

import java.util.Date;
import lombok.Data;

@Data
public class Audit {
    
    private String uuid;
    private String thread;
    private Date date;
    private String type;
    private String url;
    private String method;
    private String params;
    private String body;
    private String verb;

}
