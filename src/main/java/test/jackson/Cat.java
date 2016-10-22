/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.jackson;

/**
 *
 * @author te0003
 */
public class Cat extends Animal {

    public String getFavoriteToy() {
        return favoriteToy;
    }

    public Cat() {}

    public Cat(String name, String favoriteToy) {
        setName(name);
        setFavoriteToy(favoriteToy);
    }

    public void setFavoriteToy(String favoriteToy) {
        this.favoriteToy = favoriteToy;
    }

    private String favoriteToy;

}
