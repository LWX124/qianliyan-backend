package com.stylefeng.guns.modular.system.factory;

import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.modular.system.model.Accident;
import com.stylefeng.guns.modular.system.model.User;
import com.stylefeng.guns.modular.system.transfer.UserDto;
import com.stylefeng.guns.modular.system.vo.AccidentVo;
import org.springframework.beans.BeanUtils;

/**
 * 用户创建工厂
 *
 * @author kosan
 * @date 2017-05-05 22:43
 */
public class AccidFactory {

    public static Accident createAccid(AccidentVo userDto) {
        if (userDto == null) {
            return null;
        } else {
            Accident accident = new Accident();
            BeanUtils.copyProperties(userDto, accident);
            return accident;
        }
    }
}
