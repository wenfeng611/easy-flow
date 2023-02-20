package com.wf.flow.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
public class ApiResponse {
    private int code = 200;
    private String msg = "success";
    private Object data = null;
    private int count;

    public ApiResponse() {
        super();
        this.code = 200;
        this.msg = "success";
    }

    public ApiResponse(int code, String message, Object data) {
        super();
        this.code = code;
        this.msg = message;
        this.data = data;
    }

    public ApiResponse(int code, String message) {
        super();
        this.code = code;
        this.msg = message;
    }

    public ApiResponse(Object data) {
        super();
        this.code = 200;
        this.msg = "success";
        this.data = data;
    }

    public ApiResponse(Object data, int count) {
        super();
        this.code = 200;
        this.msg = "success";
        this.data = data;
        this.count = count;
    }

    public static ApiResponse success(Object data){
       return new ApiResponse(200,"success",data);
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class Fail extends ApiResponse {

        public Fail(String result, int code, String message) {
            super();
            this.setCode(code);
            this.setMsg(message);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @ToString(callSuper = true)
    public static class PageRows extends Ok {

        boolean more_page = false;

        public PageRows(Object obj, boolean more_page) {
            super(obj);
            this.more_page = more_page;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @ToString(callSuper = true)
    public static class Ok extends ApiResponse {

        public Ok(String message) {
            super();
            this.setMsg(message);
        }

        public Ok(Object obj) {
            super();
            this.setData(obj);
        }

        public Ok(String message, Object obj) {
            super();
            this.setMsg(message);
            this.setData(obj);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @ToString(callSuper = true)
    public static class Row extends ApiResponse {
        private Object row = null;

        public Row(Object obj) {
            super();
            this.row = obj;
        }
    }
}
