package studio.o7.remora.extensions.framework;

import lombok.Getter;
import lombok.Setter;
import org.gradle.api.Action;
import org.gradle.api.model.ObjectFactory;

import javax.inject.Inject;

@Getter
@Setter
public class FrameworkExtension {
    private LombokExtension lombok;
    private FastUtilsExtension fastUtils;

    @Inject
    public FrameworkExtension(ObjectFactory factory) {
        this.lombok = factory.newInstance(LombokExtension.class);
        this.fastUtils = factory.newInstance(FastUtilsExtension.class);
    }

    public void lombok(Action<? super LombokExtension> action) {
        action.execute(this.lombok);
    }

    public void fastUtils(Action<? super FastUtilsExtension> action) {
        action.execute(this.fastUtils);
    }
}
