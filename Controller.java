package com.example.lab4;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    private Object createObjectHandler() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        object = Class.forName(classPath.getText()).getConstructor().newInstance();
        Class<?> Class = object.getClass();
        Field[] declaredFields = Class.getDeclaredFields();
        Method[] declaredMethods = Class.getDeclaredMethods();
        addRow(object, declaredFields, declaredMethods);
        return object;
    }

    @FXML
    private void saveChangesHandler() throws InvocationTargetException, IllegalAccessException {
        if(object == null) return;
        Class<?> Class = object.getClass();
        Field[] declaredFields = Class.getDeclaredFields();
        Method[] declaredMethods = Class.getDeclaredMethods();
        setChanges(object, declaredFields, declaredMethods);
        console.clear();
        console.appendText(object.toString());
    }

    private void setChanges(Object object, Field[] fields, Method[] methods) throws InvocationTargetException, IllegalAccessException {
        for (Field field : fields) {
            for (Method method : methods) {
                if (isSetter(method) && method.getName().toLowerCase(Locale.ROOT).contains(field.getName().toLowerCase(Locale.ROOT))) {
                    for (Node n : mainContainer.getChildren()) {
                        HBox hbox = (HBox) n;
                        if (hbox.getId().contains(field.getName())) {
                            if(hbox.getChildren().get(0).getId().contains("text")){
                                TextArea ta = (TextArea) hbox.getChildren().get(0);
                                method.invoke(object, ta.getText());
                            } else {
                                TextField tf = (TextField) hbox.getChildren().get(0);
                                method.invoke(object, tf.getText());
                            }
                        }
                    }
                }
            }
        }
    }

    private void addRow(Object object, Field[] fields, Method[] methods) {
        for (Field field : fields) {
            for (Method method : methods) {
                if (isGetter(method) && method.getName().toLowerCase(Locale.ROOT).contains(field.getName().toLowerCase(Locale.ROOT))) {
                    try {
                        Object obj = method.invoke(object);
                        HBox hbox = new HBox();
                        if (!field.getName().contains("text")) {
                            TextField tf = new TextField((String) obj);
                            tf.setId(field.getName());
                            tf.setPrefColumnCount(20);
                            Label label = new Label("<- " + field.getName());
                            hbox.getChildren().addAll(tf, label);
                        } else {
                            TextArea ta = new TextArea((String) obj);
                            ta.setId(field.getName());
                            ta.setPrefColumnCount(20);
                            Label label = new Label("<- " + field.getName());
                            hbox.getChildren().addAll(ta, label);
                        }
                        hbox.setId(field.getName() + "Hbox");
                        mainContainer.getChildren().add(hbox);

                    } catch (InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean isGetter(Method method) {
        return method.getName().startsWith("get");
    }

    private boolean isSetter(Method method) {
        return method.getName().startsWith("set");
    }
}