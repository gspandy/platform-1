package com.hundsun.jresplus.base.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hundsun.jresplus.beans.ObjectFactory;
import com.hundsun.jresplus.common.util.ArrayUtil;

/**
 * 
 * @author Leo
 * 
 */
@Component
public class ValidatorManagerImpl implements ValidatorManager, InitializingBean {
	private final static Logger log=LoggerFactory.getLogger(ValidatorManagerImpl.class);
	@Autowired
	private ObjectFactory objectFactory;

	private Map<Class<? extends Validator>, Object> validators = new HashMap<Class<? extends Validator>, Object>();

	@SuppressWarnings("unchecked")
	public <T extends Validator> T getValidator(Class<T> clazz) {
		return (T) validators.get(clazz);
	}

	public void afterPropertiesSet() throws Exception {
		log.info("Default ValidatorManagerImpl inited.");
		List<? extends Validator> list = objectFactory
				.getBeansOfType4List(Validator.class);
		if (ArrayUtil.isEmpty(list) == false) {
			for (Validator validator : list) {
				validators.put(validator.getClass(), validator);
				log.info("regist Validator [{}]",validator.getClass());
			}
		}
	}

}