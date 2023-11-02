package io.github.ziqiaingai.tablebus.fusion.convert;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.ziqiaingai.tablebus.fusion.entity.ApitableDatasheetMetaEntity;
import io.github.ziqiaingai.tablebus.fusion.vo.ApitableDatasheetMetaVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ApitableDatasheetMetaConvertTest {

    @Test
    public void testMapScan(){

        ApitableDatasheetMetaEntity entity = new ApitableDatasheetMetaEntity();
        entity.setMetaData("{\"name\":\"zzq\"}");
        ApitableDatasheetMetaVO vo = ApitableDatasheetMetaConvert.INSTANCE.convert(entity);
        System.out.println(vo.getMetaData());
        System.out.println(vo.toString());
    }
}