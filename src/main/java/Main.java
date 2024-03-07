import service.HttpService;
import service.impl.HttpServiceImpl;

import static commons.MyHttpServer.PORT;

public class Main {
    public static void main(String[] args){
        HttpService service = new HttpServiceImpl();

        service.start(PORT);
    }
}


