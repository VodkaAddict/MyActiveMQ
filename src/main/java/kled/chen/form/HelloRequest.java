package kled.chen.form;

import java.io.Serializable;

public class HelloRequest implements Serializable {

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "HelloRequest{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
