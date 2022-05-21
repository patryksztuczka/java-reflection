package com.example.lab4;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public class Controller {

    @FXML
    TextField classPath;

    @FXML
    TextArea console;

    @FXML
    Button createObjectButton;

    @FXML
    Button saveChangesButton;

    @FXML
    VBox mainContainer;

    Object object = null;

    @FXML
    private void createObjectHandler() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        object = Class.forName(classPath.getText()).getConstructor().newInstance();
        Class<?> Class = object.getClass();
        Field[] declaredFields = Class.getDeclaredFields();
        Method[] declaredMethods = Class.getDeclaredMethods();
        mainContainer.getChildren().clear();
        addRow(object, declaredFields, declaredMethods);
    }

    @FXML
    private void saveChangesHandler() throws IllegalAccessException {
        if (object == null) return;
        Class<?> Class = object.getClass();
        Field[] declaredFields = Class.getDeclaredFields();
        Method[] declaredMethods = Class.getDeclaredMethods();
        console.clear();
        setChanges(object, declaredFields, declaredMethods);
        console.appendText(object.toString());
    }

    private void addRow(Object object, Field[] fields, Method[] methods) {
        for (Field field : fields) {
            for (Method method : methods) {
                if (isGetter(method) && method.getName().toLowerCase(Locale.ROOT).contains(field.getName().toLowerCase(Locale.ROOT))) {
                    try {
                        Object obj = method.invoke(object);
                        HBox hbox = new HBox();
                        TextInputControl input;
                        if (!field.getName().contains("text")) {
                            input = new TextField(String.valueOf(obj));
                        } else {
                            input = new TextArea(String.valueOf(obj));
                        }
                        input.setId(field.getName());
                        Label label = new Label("<- " + field.getName());
                        hbox.setId(field.getName() + "Hbox");
                        hbox.getChildren().addAll(input, label);
                        mainContainer.getChildren().add(hbox);

                    } catch (InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void setChanges(Object object, Field[] fields, Method[] methods) throws IllegalAccessException {
        for (Field field : fields) {
            for (Method method : methods) {
                if (isSetter(method) && method.getName().toLowerCase(Locale.ROOT).contains(field.getName().toLowerCase(Locale.ROOT))) {
                    for (Node n : mainContainer.getChildren()) {
                        HBox hbox = (HBox) n;
                        if (hbox.getId().contains(field.getName())) {
                            try {
                                Class<?> clazz = toObject(field.getType().getName());
                                TextInputControl input;
                                if (hbox.getChildren().get(0).getId().contains("text")) {
                                    input = (TextArea) hbox.getChildren().get(0);
                                } else {
                                    input = (TextField) hbox.getChildren().get(0);
                                }
                                if (clazz.getName().equals("java.lang.String")) {
                                    method.invoke(object, input.getText());
                                } else if (clazz.getName().equals("java.lang.Character")) {
                                    Character valueOf = Character.valueOf(input.getText().charAt(0));
                                    method.invoke(object, valueOf);
                                } else {
                                    Object valueOf = clazz.getMethod("valueOf", String.class).invoke(null, input.getText());
                                    method.invoke(object, valueOf);
                                }
                            } catch (ClassNotFoundException | NoSuchMethodException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                console.appendText("Can not change " + field.getName() + " value.\n");
                            }
                        }
                    }
                }
            }
        }
    }

    private Class<?> toObject(String fieldName) throws ClassNotFoundException, NoSuchMethodException {
        if (fieldName.equals("boolean")) return Boolean.class;
        if (fieldName.equals("char")) return Character.class;
        if (fieldName.equals("byte")) return Byte.class;
        if (fieldName.equals("short")) return Short.class;
        if (fieldName.equals("int")) return Integer.class;
        if (fieldName.equals("long")) return Long.class;
        if (fieldName.equals("float")) return Float.class;
        if (fieldName.equals("double")) return Double.class;
        return Class.forName(fieldName);
    }

    private boolean isGetter(Method method) {
        return method.getName().startsWith("get");
    }

    private boolean isSetter(Method method) {
        return method.getName().startsWith("set");
    }
}