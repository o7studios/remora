package studio.o7.remora.extensions;

import org.gradle.api.reflect.HasPublicType;
import org.gradle.api.reflect.TypeOf;

public abstract class DefaultRemoraExtension implements RemoraExtension, HasPublicType {

    @Override
    public TypeOf<?> getPublicType() {
        return TypeOf.typeOf(RemoraExtension.class);
    }
}
