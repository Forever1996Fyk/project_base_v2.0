package com.javaweb.MichaelKai.thymeleaf;

import com.javaweb.MichaelKai.thymeleaf.attribute.SelectDictAttrProcessor;
import com.javaweb.MichaelKai.thymeleaf.attribute.SelectListAttrProcessor;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-05-24 15:06
 **/
public class BaseDialect extends AbstractProcessorDialect implements IExpressionObjectDialect {

    private static final String NAME = "BaseDialect";
    private static final String PREFIX = "base";
    private IExpressionObjectFactory expressionObjectFactory = null;

    public BaseDialect() {
        super(NAME, PREFIX, StandardDialect.PROCESSOR_PRECEDENCE);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        LinkedHashSet processors = new LinkedHashSet();
        processors.add(new SelectDictAttrProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new SelectListAttrProcessor(TemplateMode.HTML, dialectPrefix));
        return processors;
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        if (this.expressionObjectFactory == null) {
            this.expressionObjectFactory = new BaseExpressionObjectFactory();
        }
        return this.expressionObjectFactory;
    }
}
