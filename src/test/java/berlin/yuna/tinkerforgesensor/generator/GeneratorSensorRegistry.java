package berlin.yuna.tinkerforgesensor.generator;


import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor;
import berlin.yuna.tinkerforgesensor.model.driver.bricklet.Sensor.DeviceFactory;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.tinkerforge.Device;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.squareup.javapoet.WildcardTypeName.subtypeOf;
import static java.util.Arrays.asList;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

public class GeneratorSensorRegistry {

    public static JavaFile generate(final List<Class<? extends Sensor>> sensorList) {
        String deviceLowName = firstLetterLow(Device.class.getSimpleName());
        TypeName classOfAnyDevice = ParameterizedTypeName.get(ClassName.get(Class.class), subtypeOf(Device.class));
        TypeName mapIntegerAndDeviceFactory = ParameterizedTypeName.get(ClassName.get(ConcurrentHashMap.class), ClassName.get(Integer.class), ClassName.get(DeviceFactory.class));
        TypeName mapOfDeviceOfAnyAndSensorFactory = ParameterizedTypeName.get(ClassName.get(ConcurrentHashMap.class), classOfAnyDevice, ClassName.get(Sensor.SensorFactory.class));
        StackTraceElement trace = Thread.currentThread().getStackTrace()[1];
        String autogenerated = "Autogenerated with [" + trace.getFileName().replace(".java", "") + ":" + trace.getMethodName() + "]";

        //INIT SENSOR
        MethodSpec method_initSensor = methodInitSensor(mapOfDeviceOfAnyAndSensorFactory, sensorList);
        MethodSpec method_initDevice = methodInitDevice(mapIntegerAndDeviceFactory, sensorList);

        //CREATE CLASS
        TypeSpec.Builder sensorRegistry = TypeSpec.classBuilder(SensorRegistry.class.getSimpleName())
                .addModifiers(PUBLIC)
                .addJavadoc("$N\nRegistration of {@link $T} and the Wrapper\n{@link $T}\n", autogenerated, Device.class, Sensor.class);

        //CALLBACK_PERIOD
        FieldSpec callbackPeriod = FieldSpec.builder(long.class, "CALLBACK_PERIOD", PUBLIC, STATIC, FINAL).initializer("$L", 64).build();

        //SENSOR MAP
        FieldSpec sensorMap = FieldSpec.builder(mapOfDeviceOfAnyAndSensorFactory, "sensorMap").addModifiers(PRIVATE, STATIC).initializer("$N()", method_initSensor.name).build();
        MethodSpec method_getAvailableDevices = methodGetAvailableDevices(sensorMap);
        MethodSpec getSensor = methodGetSensor(deviceLowName, classOfAnyDevice, sensorMap);

        //DEVICE MAP
        FieldSpec deviceMap = FieldSpec.builder(mapIntegerAndDeviceFactory, "deviceMap").addModifiers(PRIVATE, STATIC).initializer("$N()", method_initDevice.name).build();
        MethodSpec getDevice = method_getDevice(deviceMap);

        sensorRegistry.addFields(asList(callbackPeriod, sensorMap, deviceMap));
        sensorRegistry.addMethods(asList(getSensor, getDevice, method_getAvailableDevices, method_initSensor, method_initDevice));

        //Path sourceFile = getSourceFile(packageName, className);
        return JavaFile.builder(SensorRegistry.class.getPackage().getName(), sensorRegistry.build()).build();
    }

    private static MethodSpec methodGetSensor(String deviceLowName, TypeName classOfAnyDevice, FieldSpec sensorMap) {
        return MethodSpec.methodBuilder("getSensor").addModifiers(PUBLIC, STATIC).returns(Sensor.SensorFactory.class)
                    .addParameter(classOfAnyDevice, firstLetterLow(deviceLowName), FINAL)
                    .addStatement("return $N.get($N)", sensorMap.name, deviceLowName)
                    .build();
    }

    private static MethodSpec method_getDevice(FieldSpec deviceMap) {
        return MethodSpec.methodBuilder("getDevice").addModifiers(PUBLIC, STATIC).returns(DeviceFactory.class)
                    .addParameter(Integer.class, "deviceIdentifier")
                    .addStatement("return $N.get($N)", deviceMap.name, "deviceIdentifier")
                    .build();
    }

    private static MethodSpec methodGetAvailableDevices(FieldSpec sensorMap) {
        return MethodSpec.methodBuilder("getDeviceAvailableDevices")
                    .addModifiers(PUBLIC, STATIC).returns(ParameterizedTypeName.get(List.class, Class.class))
                    .addStatement("return new $T<>($N.keySet())", ArrayList.class, sensorMap.name)
                    .build();
    }

    private static MethodSpec methodInitSensor(final TypeName mapOfDeviceOfAnyAndSensorFactory, final  List<Class<? extends Sensor>> sensorList) {
        MethodSpec.Builder initSensor_spec = MethodSpec.methodBuilder("initSensor")
                .addModifiers(PRIVATE, STATIC)
                .returns(mapOfDeviceOfAnyAndSensorFactory)
                .addStatement("$T<Class<? extends $T>, $T> registry = new $T<>()", ConcurrentHashMap.class, Device.class, Sensor.SensorFactory.class, ConcurrentHashMap.class);
        for (Class<? extends Sensor> sensor : sensorList) {
            Type genericSuperClass = ((ParameterizedType) sensor.getGenericSuperclass()).getActualTypeArguments()[0];
            initSensor_spec.addStatement("registry.put($T.class, $T::new)", genericSuperClass, sensor);
        }
        return initSensor_spec.addStatement("return registry").build();
    }

    private static MethodSpec methodInitDevice(final TypeName mapOfDeviceOfAnyAndSensorFactory, final List<Class<? extends Sensor>> sensorList) {
        MethodSpec.Builder initSensor_spec = MethodSpec.methodBuilder("initDevice")
                .addModifiers(PRIVATE, STATIC)
                .returns(mapOfDeviceOfAnyAndSensorFactory)
                .addStatement("$T<$T, $T> registry = new $T<>()", ConcurrentHashMap.class, Integer.class, DeviceFactory.class, ConcurrentHashMap.class);
        for (Class<? extends Sensor> sensor : sensorList) {
            Type genericSuperClass = ((ParameterizedType) sensor.getGenericSuperclass()).getActualTypeArguments()[0];
            initSensor_spec.addStatement("registry.put($T.DEVICE_IDENTIFIER, $T::new)", genericSuperClass, genericSuperClass);
        }
        return initSensor_spec.addStatement("return registry").build();
    }

    private static String firstLetterLow(final String input) {
        return input.substring(0, 1).toLowerCase() + input.substring(1);
    }

}