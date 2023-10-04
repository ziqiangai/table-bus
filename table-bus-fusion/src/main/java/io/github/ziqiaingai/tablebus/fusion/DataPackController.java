package io.github.ziqiaingai.tablebus.fusion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("datasheets/dataPack")
@Slf4j
@Tag(name = "DataSheet")
public class DataPackController {
    @Resource
    private ObjectMapper objectMapper;

    @GetMapping("/{id}")
    @Operation(summary = "获取数据表的定义")
    @Parameters({
            @Parameter(name = "id",description = "DataSheet Id 数据表ID",in = ParameterIn.PATH),
    })
    public ObjectNode getDataPack(@PathVariable String id) {
        log.info("get data pack {}", id);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("uuid", UUID.randomUUID().toString());
        objectNode.put("id", id);
        return objectNode;
    }


}
