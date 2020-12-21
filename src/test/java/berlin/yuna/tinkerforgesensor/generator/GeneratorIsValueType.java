package berlin.yuna.tinkerforgesensor.generator;

import berlin.yuna.tinkerforgesensor.model.ValueType;
import berlin.yuna.tinkerforgesensor.model.helper.IsType;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import static berlin.yuna.tinkerforgesensor.generator.GeneratorTest.toHumanReadable;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

public class GeneratorIsValueType {

    private static final Class<?> TARGET_CLASS = IsType.class;
    private static final Class<?> FIELD_TYPE = ValueType.class;
    private static final String FIELD_TYPE_NAME = FIELD_TYPE.getSimpleName().toLowerCase();

    public static JavaFile generate() {
        //CREATE CLASS
        final TypeSpec.Builder resultClass = TypeSpec.classBuilder(TARGET_CLASS.getSimpleName()).addModifiers(PUBLIC);
        resultClass.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "\"unused\"").build());

        //CREATE CONSTRUCTOR
        resultClass.addField(FIELD_TYPE, FIELD_TYPE_NAME, PRIVATE, FINAL);
        resultClass.addMethod(constructor());


        //GENERATE METHODS
        resultClass.addMethods(stream(ValueType.values()).map(GeneratorIsValueType::methodInnerIsTypeOf).collect(toSet()));

        //Path sourceFile = getSourceFile(packageName, className);
        JavaFile.Builder builder = JavaFile.builder(TARGET_CLASS.getPackage().getName(), resultClass.build());
        //GENERATE IMPORTS
        stream(ValueType.values()).forEach(builder::addStaticImport);
        return builder.build();
    }

    private static MethodSpec methodInnerIsTypeOf(final ValueType valueType) {
        return MethodSpec.methodBuilder(toHumanReadable(valueType, false))
                .addModifiers(PUBLIC)
                .returns(boolean.class)
                .addStatement("return $N == $L", FIELD_TYPE_NAME, valueType)
                .build();
    }

    private static MethodSpec constructor() {
        return MethodSpec.constructorBuilder()
                .addParameter(FIELD_TYPE, FIELD_TYPE_NAME, FINAL)
                .addModifiers(PUBLIC).addStatement(format("this.%s = %s", FIELD_TYPE_NAME, FIELD_TYPE_NAME))
                .build();
    }
}
