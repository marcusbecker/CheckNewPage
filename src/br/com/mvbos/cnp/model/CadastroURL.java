/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.cnp.model;

import java.io.Serializable;

/**
 *
 * @author mbecker
 */
public class CadastroURL implements Serializable {

    private String url;
    private String status = "Sem mudança.";
    private int contadorAtual;
    private int contadorProximo;
    private boolean novo;

    public CadastroURL() {
    }

    public CadastroURL(String url, int contadorAtual) {
        this.url = url;
        this.contadorAtual = contadorAtual;
        this.contadorProximo = contadorAtual + 1;
    }

    public CadastroURL(String url, int contadorAtual, int contadorProximo, boolean novo) {
        this.url = url;
        this.contadorAtual = contadorAtual;
        this.contadorProximo = contadorProximo;
        this.novo = novo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getContadorAtual() {
        return contadorAtual;
    }

    public void setContadorAtual(int contadorAtual) {
        this.contadorAtual = contadorAtual;
    }

    public int getContadorProximo() {
        return contadorProximo;
    }

    public void setContadorProximo(int contadorProximo) {
        this.contadorProximo = contadorProximo;
    }

    public boolean isNovo() {
        return novo;
    }

    public void setNovo(boolean novo) {
        this.novo = novo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
