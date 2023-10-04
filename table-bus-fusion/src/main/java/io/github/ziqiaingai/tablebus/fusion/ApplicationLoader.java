package io.github.ziqiaingai.tablebus.fusion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import org.springframework.beans.factory.annotation.Value;

@Component
@Slf4j
public class ApplicationLoader implements ApplicationRunner {

    @Value("${server.port}")
    private int serverPort;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("see api swagger doc: http://{}:{}/doc.html", InetAddress.getLocalHost().getHostAddress(), serverPort);
    }
}