package com.example.test.sortrecyclerview;

/**
 * Created by Noe on 18/2/2017.
 */
public class UserData {
    String id;
    int sort_id;
    String name;
    String quantity;
    String description;

    public UserData() {
        super();
    }

    public void setId(String id){
        this.id= id;
    }
    public String getId(){
        return id;
    }
    public void setSort(int sort){
        this.sort_id= sort;
    }
    public int getSort(){
        return sort_id;
    }

    public void setName(String name){
        this.name= name;
    }
    public String getName(){
        return name;
    }
    public void setDescription(String description){
        this.description= description;
    }
    public String getDescription(){
        return description;
    }
    public void setQuantity(String quantity){
        this.quantity= quantity;
    }
    public String getQuantity(){
        return quantity;
    }
}
