package org.looko.mycloud.commonstarter.component;

public class CommonDemo {

    private final Boolean enable;

    public CommonDemo(Boolean enable) {
        this.enable = enable;
    }

    public void showMessage() {
        System.out.println("------ Common Module Loaded: " + enable + " -------");
    }

}
