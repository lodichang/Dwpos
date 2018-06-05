package com.dw.component;

import com.dw.controller.TakeOrderIndexController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by wen.jing on 2018/5/11
 */
@Component
public class AttComponent {
    @Autowired
    private TakeOrderIndexController takeOrderIndexController;
}
