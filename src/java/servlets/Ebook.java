/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

/**
 *
 * @author Mihai
 */
public class Ebook {
    
            private String isbn;
            private String denumire;
            private int id_type;
            private int id_quality;
            private int pages;
            private int id_genre;
            private int pret;

    public Ebook(String isbn, String denumire, int id_type, int id_quality, int pages, int id_genre, int pret) {
        this.isbn = isbn;
        this.denumire = denumire;
        this.id_type = id_type;
        this.id_quality = id_quality;
        this.pages = pages;
        this.id_genre = id_genre;
        this.pret = pret;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getDenumire() {
        return denumire;
    }

    public int getId_type() {
        return id_type;
    }

    public int getId_quality() {
        return id_quality;
    }

    public int getPages() {
        return pages;
    }

    public int getId_genre() {
        return id_genre;
    }

    public int getPret() {
        return pret;
    }
    
    
            
            
    
}
