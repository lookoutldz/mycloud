package org.looko.mycloud.commonstarter.component;

import lombok.extern.slf4j.Slf4j;
import org.looko.mycloud.commonstarter.entity.property.CommonProperties;

@Slf4j
public class CommonPropertyCheck {

    private final CommonProperties commonProperties;

    public CommonPropertyCheck(CommonProperties commonProperties) {
        this.commonProperties = commonProperties;
    }

    public void logProperty() {
        log.info(commonProperties.toString());
    }

}
