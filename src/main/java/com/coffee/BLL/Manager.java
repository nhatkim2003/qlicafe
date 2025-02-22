package com.coffee.BLL;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public abstract class Manager<T> {
    public Object[][] getData(List<T> objectList) {
        Object[][] data = new Object[objectList.size()][];
        for (int i = 0; i < data.length; ++i) {
            data[i] = objectList.get(i).toString().split(" \\| ");
//            data[i] = Arrays.copyOf(data[i], data[i].length + 1);
        }
        return data;
    }

    public int getAutoID(List<T> objectList) {
        if (objectList.isEmpty()) {
            return 1;
        }
        T lastObject = objectList.get(objectList.size() - 1);
        String lastID = lastObject.toString().split(" \\| ")[0];
        int id = Integer.parseInt(lastID);
        return id + 1;
    }

    public String formatNumberToString(int number, int digits) {
        String format = "%0" + digits + "d";
        return String.format(format, number);
    }

    public List<Object> getObjectsProperty(String key, List<T> objectList) {
        List<Object> listOfProperties = new ArrayList<>();
        for (T object : objectList) {
            listOfProperties.add(getValueByKey(object, key));
        }
        return listOfProperties;
    }

    public List<T> findObjectsBy(String key, Object value, List<T> objectList) {
        List<T> objects = new ArrayList<>();
        for (T object : objectList)
            if (getValueByKey(object, key).equals(value))
                objects.add(object);
        return objects;
    }

    public int getIndex(T object, String key, List<T> objectList) {
        return IntStream.range(0, objectList.size())
                .filter(i -> Objects.equals(getValueByKey(objectList.get(i), key), getValueByKey(object, key)))
                .findFirst()
                .orElse(-1);
    }

    public int getIndex(T object, List<String> keys, List<T> objectList) {
        return IntStream.range(0, objectList.size())
                .filter(i -> {
                    boolean have = true;
                    for (String key : keys) {
                        have = have && Objects.equals(getValueByKey(objectList.get(i), key), getValueByKey(object, key));
                    }
                    return have;
                })
                .findFirst()
                .orElse(-1);
    }

    public abstract Object getValueByKey(T object, String key);
}
