package studio.o7.remora.extensions.framework;

import lombok.Getter;
import lombok.Setter;
import org.gradle.api.model.ObjectFactory;

@Getter
@Setter
public class FrameworkExtension {
    private LombokExtension lombok;

    public FrameworkExtension(ObjectFactory factory) {
        lombok = factory.newInstance(LombokExtension.class);
    }
}
