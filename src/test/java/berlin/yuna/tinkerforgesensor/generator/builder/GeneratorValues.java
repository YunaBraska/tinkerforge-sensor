package berlin.yuna.tinkerforgesensor.generator.builder;


import berlin.yuna.tinkerforgesensor.model.builder.Values;
import berlin.yuna.tinkerforgesensor.model.sensor.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.type.RollingList;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.concurrent.CopyOnWriteArrayList;

import static berlin.yuna.tinkerforgesensor.generator.GeneratorTest.toHumanReadable;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.SOUND_SPECTRUM;
import static berlin.yuna.tinkerforgesensor.model.type.ValueType.SOUND_SPECTRUM_CHUNK;
import static com.squareup.javapoet.WildcardTypeName.subtypeOf;
import static java.util.Arrays.asList;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

public class GeneratorValues {

    public static JavaFile generate(final List<Class<? extends Sensor>> sensors) {
        final Class<Values> generateClass = Values.class;
        final StackTraceElement trace = Thread.currentThread().getStackTrace()[1];
        final String autogenerated = "Autogenerated with [" + trace.getFileName().replace(".java", "") + ":" + trace.getMethodName() + "]";
        final List<Class<? extends Sensor>> sensorList = new ArrayList<>(sensors);
        Collections.reverse(sensorList);

        //TYPE DEFINITIONS
        final ParameterizedTypeName type_ListOfSensor = ParameterizedTypeName.get(List.class, Sensor.class);
        final ClassName type_ClassOfGenerics = ClassName.get(Class.class);

        //CREATE CLASS
        final TypeSpec.Builder resultClass = TypeSpec.classBuilder(generateClass.getSimpleName())
                .addModifiers(PUBLIC)
                .superclass(ParameterizedTypeName.get(ClassName.get(CopyOnWriteArrayList.class), TypeVariableName.get(Sensor.class)))
                .addJavadoc("$N\n", autogenerated);

        //CONSTRUCTOR
        resultClass.addMethod(constructor_empty());
        resultClass.addMethod(constructor_collection());
        resultClass.addMethod(constructor_array());

        //CREATE METHODS
        final MethodSpec method_getMap = method_getMap();
        final MethodSpec method_getMapMin = method_getMapMin();
        final MethodSpec method_getMapMax = method_getMapMax();
        final MethodSpec method_getMapAvg = method_getMapAvg();
        final MethodSpec method_getMapSum = method_getMapSum();
        resultClass.addMethods(asList(method_getMap, method_getMapMin, method_getMapMax, method_getMapAvg, method_getMapSum));

        final MethodSpec method_getFb = method_getFallBack(method_getMap, "");
        final MethodSpec method_getFb_Min = method_getFallBack(method_getMapMin, "_Min");
        final MethodSpec method_getFb_Max = method_getFallBack(method_getMapMax, "_Max");
        final MethodSpec method_getFb_Avg = method_getFallBack(method_getMapAvg, "_Avg");
        final MethodSpec method_getFb_Sum = method_getFallBack(method_getMapSum, "_Sum");
        resultClass.addMethods(asList(method_getFb, method_getFb_Min, method_getFb_Max, method_getFb_Avg, method_getFb_Sum));

        final MethodSpec method_get = method_get(method_getFb, "");
        final MethodSpec method_getMin = method_get(method_getFb_Min, "_Min");
        final MethodSpec method_getMax = method_get(method_getFb_Max, "_Max");
        final MethodSpec method_getAvg = method_get(method_getFb_Avg, "_Avg");
        final MethodSpec method_getSum = method_get(method_getFb_Sum, "_Sum");
        resultClass.addMethods(asList(method_get, method_getMin, method_getMax, method_getAvg, method_getSum));

        final MethodSpec method_getList = method_getList();
        resultClass.addMethods(asList(method_getValueType(SOUND_SPECTRUM, method_getList), method_getValueType(SOUND_SPECTRUM_CHUNK, method_getList), method_getList));

        //GENERATE METHODS
        for (ValueType valueType : ValueType.values()) {
            resultClass.addMethod(method_valueXy(valueType, "", method_get));
            resultClass.addMethod(method_valueXy(valueType, "_Min", method_getMin));
            resultClass.addMethod(method_valueXy(valueType, "_Max", method_getMax));
            resultClass.addMethod(method_valueXy(valueType, "_Avg", method_getAvg));
            resultClass.addMethod(method_valueXy(valueType, "_Sum", method_getSum));
        }

        //Path sourceFile = getSourceFile(packageName, className);
        return JavaFile.builder(generateClass.getPackage().getName(), resultClass.build()).build();
    }

    private static MethodSpec method_getValueType(final ValueType valueType, final MethodSpec parent) {
        return MethodSpec.methodBuilder("list" + toHumanReadable(valueType, true))
                .addModifiers(PUBLIC)
                .addStatement("return $N($T.$L)", parent.name, ValueType.class, valueType)
                .returns(ParameterizedTypeName.get(RollingList.class, Long.class))
                .build();
    }

    private static MethodSpec method_getList() {
        return MethodSpec.methodBuilder("getList")
                .addModifiers(PUBLIC)
                .addParameter(ValueType.class, "valueType", FINAL)
                .addCode("for (Sensor sensor : this) {\n")
                .addCode("  if (sensor.valueMap().containsKey(valueType)) {\n")
                .addCode("    return (RollingList<Long>) sensor.valueMap().get(valueType);\n")
                .addCode("  }\n")
                .addCode("}\n")
                .addCode("return new RollingList<>(0);\n")
                .returns(ParameterizedTypeName.get(RollingList.class, Long.class))
                .build();
    }

    private static MethodSpec method_valueXy(final ValueType valueType, final String suffix, final MethodSpec parentMethod) {
        return MethodSpec.methodBuilder(toHumanReadable(valueType, false) + suffix)
                .addModifiers(PUBLIC)
                .addStatement("return $N($T.$L)", parentMethod.name, ValueType.class, valueType.name())
                .returns(parentMethod.returnType)
                .build();
    }

    private static MethodSpec method_getMap() {
        final String parameterName = "valueType";
        return MethodSpec.methodBuilder("getMap")
                .addModifiers(PUBLIC)
                .addParameter(ValueType.class, parameterName, FINAL)
                .addStatement("final $T<$T, $T> result = new $T<>()", HashMap.class, Sensor.class, Long.class, HashMap.class)
                .addCode("for ($T sensor : this) {\n", Sensor.class)
                .addStatement("  final $T<$T> sensorValues = ($T<$T>) sensor.valueMap().get(valueType)", RollingList.class, Long.class, RollingList.class, Long.class)
                .addCode("  if (sensorValues != null && !sensorValues.isEmpty()) {\n")
                .addStatement("    result.put(sensor, sensorValues.getLast())")
                .addCode("  }\n")
                .addCode("}\n")
                .addStatement("return result")
                .returns(ParameterizedTypeName.get(ClassName.get(HashMap.class), ClassName.get(Sensor.class), ClassName.get(Long.class)))
                .build();
    }

    private static MethodSpec method_getMapMax() {
        final String parameterName = "valueType";
        return MethodSpec.methodBuilder("getMap_Max")
                .addModifiers(PUBLIC)
                .addParameter(ValueType.class, parameterName, FINAL)
                .addStatement("final $T<$T, $T> result = new $T<>()", HashMap.class, Sensor.class, Long.class, HashMap.class)
                .addCode("for ($T sensor : this) {\n", Sensor.class)
                .addStatement("  final $T<$T> sensorValues = ($T<$T>) sensor.valueMap().get(valueType)", RollingList.class, Long.class, RollingList.class, Long.class)
                .addCode("  if (sensorValues != null && !sensorValues.isEmpty()) {\n")
                .addStatement("    result.put(sensor, sensorValues.stream().mapToLong(send -> send).summaryStatistics().getMax())")
                .addCode("  }\n")
                .addCode("}\n")
                .addStatement("return result")
                .returns(ParameterizedTypeName.get(ClassName.get(HashMap.class), ClassName.get(Sensor.class), ClassName.get(Long.class)))
                .build();
    }

    private static MethodSpec method_getMapMin() {
        final String parameterName = "valueType";
        return MethodSpec.methodBuilder("getMap_Min")
                .addModifiers(PUBLIC)
                .addParameter(ValueType.class, parameterName, FINAL)
                .addStatement("final $T<$T, $T> result = new $T<>()", HashMap.class, Sensor.class, Long.class, HashMap.class)
                .addCode("for ($T sensor : this) {\n", Sensor.class)
                .addStatement("  final $T<$T> sensorValues = ($T<$T>) sensor.valueMap().get(valueType)", RollingList.class, Long.class, RollingList.class, Long.class)
                .addCode("  if (sensorValues != null && !sensorValues.isEmpty()) {\n")
                .addStatement("    result.put(sensor, sensorValues.stream().mapToLong(send -> send).summaryStatistics().getMin())")
                .addCode("  }\n")
                .addCode("}\n")
                .addStatement("return result")
                .returns(ParameterizedTypeName.get(ClassName.get(HashMap.class), ClassName.get(Sensor.class), ClassName.get(Long.class)))
                .build();
    }

    private static MethodSpec method_getMapAvg() {
        final String parameterName = "valueType";
        return MethodSpec.methodBuilder("getMap_Avg")
                .addModifiers(PUBLIC)
                .addParameter(ValueType.class, parameterName, FINAL)
                .addStatement("final $T<$T, $T> result = new $T<>()", HashMap.class, Sensor.class, Long.class, HashMap.class)
                .addCode("for ($T sensor : this) {\n", Sensor.class)
                .addStatement("  final $T<$T> sensorValues = ($T<$T>) sensor.valueMap().get(valueType)", RollingList.class, Long.class, RollingList.class, Long.class)
                .addCode("  if (sensorValues != null && !sensorValues.isEmpty()) {\n")
                .addStatement("    result.put(sensor, (long) sensorValues.stream().mapToLong(send -> send).summaryStatistics().getAverage())")
                .addCode("  }\n")
                .addCode("}\n")
                .addStatement("return result")
                .returns(ParameterizedTypeName.get(ClassName.get(HashMap.class), ClassName.get(Sensor.class), ClassName.get(Long.class)))
                .build();
    }

    private static MethodSpec method_getMapSum() {
        final String parameterName = "valueType";
        return MethodSpec.methodBuilder("getMap_Sum")
                .addModifiers(PUBLIC)
                .addParameter(ValueType.class, parameterName, FINAL)
                .addStatement("final $T<$T, $T> result = new $T<>()", HashMap.class, Sensor.class, LongSummaryStatistics.class, HashMap.class)
                .addCode("for ($T sensor : this) {\n", Sensor.class)
                .addStatement("  final $T<$T> sensorValues = ($T<$T>) sensor.valueMap().get(valueType)", RollingList.class, Long.class, RollingList.class, Long.class)
                .addCode("  if (sensorValues != null && !sensorValues.isEmpty()) {\n")
                .addStatement("    result.put(sensor, sensorValues.stream().mapToLong(send -> send).summaryStatistics())")
                .addCode("  }\n")
                .addCode("}\n")
                .addStatement("return result")
                .returns(ParameterizedTypeName.get(ClassName.get(HashMap.class), ClassName.get(Sensor.class), ClassName.get(LongSummaryStatistics.class)))
                .build();
    }

    private static MethodSpec method_get(final MethodSpec parentMethod, final String suffix) {
        return MethodSpec.methodBuilder("get" + suffix)
                .addModifiers(PUBLIC)
                .addParameter(ValueType.class, "valueType", FINAL)
                .addStatement("return $N(valueType, $N)", parentMethod.name, ((ClassName) parentMethod.returnType).simpleName().equals(Long.class.getSimpleName()) ? "0L" : "new " + ((ClassName) parentMethod.returnType).simpleName() + "()")
                .returns(parentMethod.returnType)
                .build();
    }

    private static MethodSpec method_getFallBack(final MethodSpec parentMethod, final String suffix) {
        final TypeName returnType = ((ParameterizedTypeName) parentMethod.returnType).typeArguments.get(((ParameterizedTypeName) parentMethod.returnType).typeArguments.size() - 1);
        return MethodSpec.methodBuilder("get" + suffix)
                .addModifiers(PUBLIC)
                .addParameter(ValueType.class, "valueType", FINAL)
                .addParameter(returnType, "fallBack", FINAL)
                .addStatement("final $T<$T, $T> result = $N(valueType)", HashMap.class, Sensor.class, returnType, parentMethod.name)
                .addStatement("return result.isEmpty() ? fallBack : result.values().iterator().next()")
                .returns(returnType)
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