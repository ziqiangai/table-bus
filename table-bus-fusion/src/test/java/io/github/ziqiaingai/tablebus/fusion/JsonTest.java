package io.github.ziqiaingai.tablebus.fusion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class JsonTest {

    @Test
    public void test() throws IOException {
        System.out.println("Hello World!");
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = (ObjectNode) objectMapper.readTree(new File("/Users/zzq/vikadata_vika_datasheet_meta.json"));
        int[] i = {0};
        jsonNode.get("views").forEach(jsonNode1 -> {
            ((ObjectNode) jsonNode1).set("rows", objectMapper.createArrayNode());
            System.out.println(i[0]++);
        });
        System.out.println(jsonNode);
    }
}
