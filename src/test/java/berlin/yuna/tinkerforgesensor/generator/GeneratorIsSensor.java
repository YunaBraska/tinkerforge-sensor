package berlin.yuna.tinkerforgesensor.generator;

import berlin.yuna.tinkerforgesensor.logic.Sensor;
import berlin.yuna.tinkerforgesensor.model.helper.IsSensor;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static berlin.yuna.tinkerforgesensor.generator.GeneratorHelper.firstLetterLow;
import static berlin.yuna.tinkerforgesensor.generator.GeneratorHelper.getClassVersions;
import static java.lang.String.format;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

public class GeneratorIsSensor {

    private static final Class<?> TARGET_CLASS = IsSensor.class;
    private static final Class<?> FIELD_TYPE = Sensor.class;
    private static final String FIELD_TYPE_NAME = FIELD_TYPE.getSimpleName().toLowerCase();

    public static JavaFile generate(final List<JFile> handlers) {
        final List<JFile> sensorList = new ArrayList<>(handlers);

        //CREATE CLASS
        final TypeSpec.Builder resultClass = TypeSpec.classBuilder(TARGET_CLASS.getSimpleName()).addModifiers(PUBLIC);
        resultClass.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class).addMember("value","\"unused\"").build());

        //CREATE CONSTRUCTOR
        resultClass.addField(FIELD_TYPE, FIELD_TYPE_NAME, PRIVATE, FINAL);
        resultClass.addMethod(constructor());

        //GENERATE METHODS
        while (!sensorList.isEmpty()) {
            final JFile handler = sensorList.iterator().next();
            final List<JFile> sensorVersions = getClassVersions(handler, sensorList);

            resultClass.addMethod(method_isHandlerXy(handler, sensorVersions));

            //REMOVE SENSORS VARIANTS FROM LIST
            for (JFile sensorVersion : sensorVersions) {
                sensorList.remove(sensorVersion);
            }
        }

        //Path sourceFile = getSourceFile(packageName, className);
        return JavaFile.builder(TARGET_CLASS.getPackage().getName(), resultClass.build()).build();
    }

    private static MethodSpec method_isHandlerXy(final JFile handler, final List<JFile> sensorVersions) {
        final MethodSpec.Builder method = MethodSpec.methodBuilder(firstLetterLow(handler.getBasicName()))
                .addModifiers(PUBLIC)
                .returns(boolean.class);


        final String returnLine = "return " + sensorVersions.stream()
                .map(sv -> "$T.class").collect(Collectors.joining(", ", FIELD_TYPE_NAME + ".is(", ")"));
        return method.addStatement(returnLine, sensorVersions.stream().map(JFile::getClazz).toArray()).build();
    }

    private static MethodSpec constructor() {
        return MethodSpec.constructorBuilder()
                .addParameter(FIELD_TYPE, FIELD_TYPE_NAME, FINAL)
                .addModifiers(PUBLIC).addStatement(format("this.%s = %s", FIELD_TYPE_NAME, FIELD_TYPE_NAME))
                .build();
    }
}
