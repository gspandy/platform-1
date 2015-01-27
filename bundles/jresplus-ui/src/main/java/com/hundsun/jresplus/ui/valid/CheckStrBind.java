package com.hundsun.jresplus.ui.valid;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.Future;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.hundsun.jresplus.common.util.StringUtil;
import com.hundsun.jresplus.util.RegexUtil;

/**
 * 
 * @author Leo
 * 
 */
@Service
public class CheckStrBind {

	@Autowired
	private LocalValidatorFactoryBean validator;

	public void getCheckStr(StringBuilder name, Class<?> clazz,
			Map<String, String> result) {
		StringBuilder path = null;
		if (name != null && name.length() > 0) {
			path = name;
			path.append(".");
		} else {
			path = new StringBuilder();
		}
		Set<PropertyDescriptor> propertyDescriptors = validator
				.getConstraintsForClass(clazz).getConstrainedProperties();
		if (propertyDescriptors.size() == 0) {
			return;
		}
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			StringBuilder sb = new StringBuilder();
			String type = propertyDescriptor.getElementClass().getName();
			if ((type.startsWith("java.lang.") || type.equals("int")
					|| type.equals("float") || type.equals("long") || type
						.equals("double")) == false) {
				getCheckStr(path.append(propertyDescriptor.getPropertyName()),
						propertyDescriptor.getElementClass(), result);
			} else {
				Set<ConstraintDescriptor<?>> constraintDescriptors = propertyDescriptor
						.getConstraintDescriptors();
				if (constraintDescriptors.size() > 0) {
					for (ConstraintDescriptor<?> constraintDescriptor : constraintDescriptors) {
						StringBuilder builder = getCheckStrByAnnotation(constraintDescriptor
								.getAnnotation());
						if (builder.length() > 0) {
							sb.append(builder).append(";");
						}
					}
					if (sb.length() > 2) {
						result.put((path.toString() + propertyDescriptor
								.getPropertyName()),
								sb.deleteCharAt(sb.length() - 1).toString());
					}
				}
			}
		}
	}

	public void getCheckStr(String name, Annotation[] annotations,
			Map<String, String> result) {
		StringBuilder sb = new StringBuilder();
		for (Annotation annotation : annotations) {
			StringBuilder builder = getCheckStrByAnnotation(annotation);
			if (builder.length() > 0) {
				sb.append(builder).append(";");
			}
		}
		if (sb.length() > 2) {
			result.put(name, sb.deleteCharAt(sb.length() - 1).toString());
		}
	}

	private StringBuilder getCheckStrByAnnotation(Annotation annotation) {
		StringBuilder sb = new StringBuilder();
		if (annotation.annotationType() == NotEmpty.class) {
			sb.append("required");
		} else if (annotation.annotationType() == Range.class) {
			Range range = (Range) annotation;
			sb.append("range(").append(range.min()).append(",")
					.append(range.max()).append(")");
		} else if (annotation.annotationType() == Length.class) {
			Length length = (Length) annotation;
			sb.append("length(").append(length.min()).append(",")
					.append(length.max()).append(")");
		} else if (annotation.annotationType() == URL.class) {
			sb.append("url");
		} else if (annotation.annotationType() == Email.class) {
			sb.append("email");
		} else if (annotation.annotationType() == Future.class) {
			sb.append("future");
		} else if (annotation.annotationType() == Past.class) {
			sb.append("past");
		} else if (annotation.annotationType() == Pattern.class) {
			Pattern pattern = (Pattern) annotation;
			sb.append(getPattern(pattern.regexp()));
		} else if (annotation.annotationType() == ScriptValid.class) {
			ScriptValid jsValid = (ScriptValid) annotation;
			sb.append(jsValid.name());
		}
		return sb;
	}

	private String getPattern(String regexp) {
		if (StringUtil.isNotEmpty(regexp)) {
			if (regexp.equals(RegexUtil.IDCARD)) {
				return "idcard";
			}
			if (regexp.equals(RegexUtil.DATE)) {
				return "date";
			}
			if (regexp.equals(RegexUtil.ZIPCODE)) {
				return "zipcode";
			}
			if (regexp.equals(RegexUtil.MOBILE)) {
				return "mobile";
			}
			if (regexp.equals(RegexUtil.TEL)) {
				return "tel";
			}
		}
		return "";
	}

}
