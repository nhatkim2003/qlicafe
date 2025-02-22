package com.coffee.DTO;

public class Decentralization {
    private int role_id;
    private int module_id;
    private int function_id;

    public Decentralization() {
    }

    public Decentralization(int role_id, int module_id, int function_id) {
        this.role_id = role_id;
        this.module_id = module_id;
        this.function_id = function_id;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }

    public int getFunction_id() {
        return function_id;
    }

    public void setFunction_id(int function_id) {
        this.function_id = function_id;
    }

    @Override
    public String toString() {
        return role_id + " | " +
                module_id + " | " +
                function_id;
    }
}
