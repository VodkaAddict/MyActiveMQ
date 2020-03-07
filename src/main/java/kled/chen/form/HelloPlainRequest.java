package kled.chen.form;

public class HelloPlainRequest {
    public HelloPlainRequest(){

    }

    public HelloPlainRequest(String msg) {
        this.msg = msg;
    }

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "HelloPlainRequest{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
