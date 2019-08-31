package berlin.yuna.tinkerforgesensor.generator.builder;


import berlin.yuna.tinkerforgesensor.model.JFile;
import berlin.yuna.tinkerforgesensor.model.builder.Values;
import berlin.yuna.tinkerforgesensor.model.sensor.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.ValueType;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static berlin.yuna.tinkerforgesensor.generator.GeneratorHelper.SEPARATOR;
import static berlin.yuna.tinkerforgesensor.generator.GeneratorTest.toHumanReadable;
import static com.squareup.javapoet.WildcardTypeName.subtypeOf;
import static java.util.Arrays.asList;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

public class GeneratorValues {

    public static JavaFile generate(final List<JFile> sensors) {
        final Class<Values> generateClass = Values.class;
        final StackTraceElement trace = Thread.currentThread().getStackTrace()[1];
        final String autogenerated = "Autogenerated with [" + trace.getFileName().replace(".java", "") + ":" + trace.getMethodName() + "]";

        //CREATE CLASS
        final TypeSpec.Builder resultClass = TypeSpec.classBuilder(generateClass.getSimpleName())
                .addModifiers(PUBLIC)
                .superclass(ParameterizedTypeName.get(ClassName.get(CopyOnWriteArrayList.class), TypeVariableName.get(Sensor.class)))
                .addJavadoc("$N" + SEPARATOR, autogenerated);

        //CONSTRUCTOR
        resultClass.addMethod(constructor_empty());
        resultClass.addMethod(constructor_collection());
        resultClass.addMethod(constructor_array());

        //CREATE METHODS
        final MethodSpec method_getMapStatistics = method_getMapStatistics();
        final MethodSpec method_getMapList = method_getMapList();
        final MethodSpec method_getMap = method_getMap();

        final MethodSpec method_getStatistics = method_getStatistics(method_getMapStatistics);
        final MethodSpec method_getList = method_getList(method_getMapList);
        final MethodSpec method_get = method_get(method_getMap);
        resultClass.addMethods(asList(method_get, method_getList, method_getStatistics, method_getMap, method_getMapList, method_getMapStatistics));

        //GENERATE METHODS
        for (ValueType valueType : ValueType.values()) {
            final MethodSpec method_getXyStatisticsVi = method_getXyStatisticsVi(valueType, method_getStatistics);
            final MethodSpec method_getXyStatistics = method_getXyStatistics(valueType, method_getXyStatisticsVi);
            final MethodSpec method_getXyListTi = method_getXyListTi(valueType, method_getList);
            final MethodSpec method_getXyList = method_getXyList(method_getXyListTi);
            final MethodSpec method_getXyVi = method_getXyVi(valueType, method_get);
            final MethodSpec method_getXy = method_getXy(method_getXyVi);

            resultClass.addMethods(asList(method_getXy, method_getXyVi, method_getXyList, method_getXyListTi, method_getXyStatistics, method_getXyStatisticsVi));
        }

        //Path sourceFile = getSourceFile(packageName, className);
        return JavaFile.builder(generateClass.getPackage().getName(), resultClass.build()).build();
    }

    private static MethodSpec method_get(final MethodSpec getMap) {
        final Class<Long> returnType = Long.class;
        return MethodSpec.methodBuilder("get")
                .addModifiers(PUBLIC)
                .addParameter(ValueType.class, "valueType", FINAL)
                .addParameter(returnType, "fallBack", FINAL)
                .addParameter(Number.class, "valueIndex", FINAL)
                .addStatement("final $T<$T, $T> result = $N(valueType, -1, valueIndex)", HashMap.class, Sensor.class, returnType, getMap.name)
                .addStatement("return result.isEmpty() ? fallBack : result.values().iterator().next()")
                .returns(returnType)
                .build();
    }

    private static MethodSpec method_getList(final MethodSpec getMapList) {
        final TypeName returnType = ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(Long.class));
        return MethodSpec.methodBuilder("getList")
                .addModifiers(PUBLIC)
                .addParameter(ValueType.class, "valueType", FINAL)
                .addParameter(Number.class, "timeIndex", FINAL)
                .addStatement("final $T<$T, $T> result = $N(valueType, timeIndex)", HashMap.class, Sensor.class, returnType, getMapList.name)
                .addStatement("return result.isEmpty() ? new $T<>() : result.values().iterator().next()", ArrayList.class)
                .returns(returnType)
                .build();
    }

    private static MethodSpec method_getStatistics(final MethodSpec getMapStatistics) {
        final Class returnType = LongSummaryStatistics.class;
        return MethodSpec.methodBuilder("getStatistics")
                .addModifiers(PUBLIC)
                .addParameter(ValueType.class, "valueType", FINAL)
                .addParameter(Number.class, "valueIndex", FINAL)
                .addStatement("final $T<$T, $T> result = $N(valueType, valueIndex)", HashMap.class, Sensor.class, returnType, getMapStatistics.name)
                .addStatement("return result.isEmpty() ? new $T() : result.values().iterator().next()", LongSummaryStatistics.class)
                .returns(returnType)
                .build();
    }

    private static MethodSpec method_getXy(final MethodSpec getXyVi) {
        return MethodSpec.methodBuilder(getXyVi.name)
                .addModifiers(PUBLIC)
                .addStatement("return $N(-1)", getXyVi.name)
                .returns(getXyVi.returnType)
                .build();
    }

    private static MethodSpec method_getXyVi(final ValueType valueType, final MethodSpec parentMethod) {
        return MethodSpec.methodBuilder(toHumanReadable(valueType, false))
                .addModifiers(PUBLIC)
                .addParameter(Number.class, "valueIndex", FINAL)
                .addStatement("return $N($T.$L, -1L, valueIndex)", parentMethod.name, ValueType.class, valueType.name())
                .returns(Long.class)
                .build();
    }

    private static MethodSpec method_getXyList(final MethodSpec getXyListTi) {
        return MethodSpec.methodBuilder(getXyListTi.name)
                .addModifiers(PUBLIC)
                .addStatement("return $N(-1)", getXyListTi.name)
                .returns(ParameterizedTypeName.get(List.class, Long.class))
                .build();
    }

    private static MethodSpec method_getXyListTi(final ValueType valueType, final MethodSpec getList) {
        return MethodSpec.methodBuilder(toHumanReadable(valueType, false) + "_List")
                .addModifiers(PUBLIC)
                .addParameter(Number.class, "timeIndex", FINAL)
                .addStatement("return $N($T.$L, timeIndex)", getList.name, ValueType.class, valueType)
                .returns(ParameterizedTypeName.get(List.class, Long.class))
                .build();
    }

    private static MethodSpec method_getXyStatistics(final ValueType valueType, final MethodSpec getXyStatisticsVi) {
        return MethodSpec.methodBuilder(toHumanReadable(valueType, false) + "_Statistics")
                .addModifiers(PUBLIC)
                .addStatement("return $N(-1)", getXyStatisticsVi.name)
                .returns(LongSummaryStatistics.class)
                .build();
    }

    private static MethodSpec method_getXyStatisticsVi(final ValueType valueType, final MethodSpec getStatistics) {
        return MethodSpec.methodBuilder(toHumanReadable(valueType, false) + "_Statistics")
                .addModifiers(PUBLIC)
                .addParameter(Number.class, "valueIndex", FINAL)
                .addStatement("return $N($T.$L, valueIndex)", getStatistics.name, ValueType.class, valueType.name())
                .returns(LongSummaryStatistics.class)
                .build();
    }

    private static MethodSpec method_getMap() {
        final String nameValueType = "valueType";
        final String nameTimeIndex = "timeIndex";
        final String nameValueIndex = "valueIndex";
        return MethodSpec.methodBuilder("getMap")
                .addModifiers(PUBLIC)
                .addParameter(ValueType.class, nameValueType, FINAL)
                .addParameter(Number.class, nameTimeIndex, FINAL)
                .addParameter(Number.class, nameValueIndex, FINAL)
                .addStatement("final $T<$T, $T> result = new $T<>()", HashMap.class, Sensor.class, Long.class, HashMap.class)
                .addCode("for ($T sensor : this) {" + SEPARATOR, Sensor.class)
                .addStatement("  final $T value = sensor.getValue($N, $N.intValue(), $N.intValue())", Number.class, nameValueType, nameTimeIndex, nameValueIndex)
                .addCode("  if (value != null ) {" + SEPARATOR)
                .addStatement("    result.put(sensor, value.longValue())")
                .addCode("  }" + SEPARATOR)
                .addCode("}" + SEPARATOR)
                .addStatement("return result")
                .returns(ParameterizedTypeName.get(ClassName.get(HashMap.class), ClassName.get(Sensor.class), ClassName.get(Long.class)))
                .build();
    }

    private static MethodSpec method_getMapList() {
        final String nameValueType = "valueType";
        final String nameTimeIndex = "timeIndex";
        return MethodSpec.methodBuilder("getMap")
                .addModifiers(PUBLIC)
                .addParameter(ValueType.class, nameValueType, FINAL)
                .addParameter(Number.class, nameTimeIndex, FINAL)
                .addStatement("final $T<$T, $T<$T>> result = new $T<>()", HashMap.class, Sensor.class, List.class, Long.class, HashMap.class)
                .addCode("for ($T sensor : this) {" + SEPARATOR, Sensor.class)
                .addStatement("  final $T<$T> values = sensor.getValueList($N, $N.intValue())", List.class, Number.class, nameValueType, nameTimeIndex)
                .addCode("  if (!values.isEmpty()) {" + SEPARATOR)
                .addStatement("    result.put(sensor, values.stream().map(Number::longValue).collect($T.toList()))", Collectors.class)
                .addCode("  }" + SEPARATOR)
                .addCode("}" + SEPARATOR)
                .addStatement("return result")
                .returns(
                        ParameterizedTypeName.get(
                                ClassName.get(HashMap.class),
                                ClassName.get(Sensor.class),
                                ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(Long.class))
                        )
                )
                .build();
    }

    private static MethodSpec method_getMapStatistics() {
        final String parameterName = "valueType";
        return MethodSpec.methodBuilder("getMapStatistics")
                .addModifiers(PUBLIC)
                .addParameter(ValueType.class, parameterName, FINAL)
                .addParameter(Number.class, "valueIndex", FINAL)
                .addStatement("final $T<$T, $T> result = new $T<>()", HashMap.class, Sensor.class, LongSummaryStatistics.class, HashMap.class)
                .addCode("for ($T sensor : this) {\n", Sensor.class)
                .addStatement("  final $T<$T> sensorValues = sensor.getValueListVertical(valueType, valueIndex.intValue())", List.class, Number.class)
                .addCode("  if (!sensorValues.isEmpty()) {\n")
                .addStatement("    result.put(sensor, sensorValues.stream().mapToLong(Number::longValue).summaryStatistics())")
                .addCode("  }\n")
                .addCode("}\n")
                .addStatement("return result")
                .returns(ParameterizedTypeName.get(ClassName.get(HashMap.class), ClassName.get(Sensor.class), ClassName.get(LongSummaryStatistics.class)))
                .build();
    }

    private static MethodSpec constructor_empty() {
        return MethodSpec.constructorBuilder().addModifiers(PUBLIC).build();
    }

    private static MethodSpec constructor_collection() {
        return MethodSpec.constructorBuilder()
                .addModifiers(PUBLIC)
                .addParameter(ParameterizedTypeName.get(ClassName.get(Collection.class), subtypeOf(Sensor.class)), "collection", FINAL)
                .addStatement("super(collection)")
                .build();
    }

    private static MethodSpec constructor_array() {
        return MethodSpec.constructorBuilder()
                .addModifiers(PUBLIC)
                .addParameter(ArrayTypeName.of(Sensor.class), "toCopyIn", FINAL)
                .addStatement("super(toCopyIn)")
                .build();
    }
}