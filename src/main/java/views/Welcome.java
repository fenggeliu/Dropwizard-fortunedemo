package views;

import io.dropwizard.views.View;

public class Welcome extends View{

    final private String message;

    public Welcome(String message) {
        super("welcome.ftl");
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
