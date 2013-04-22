package br.com.mvbos.cnp.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    /* Lista de CadastroURL que representam as linhas. */
    private List<CadastroURL> linhas;

    /* Array de Strings com o nome das colunas. */
    private String[] colunas = new String[]{
        "URL", "Atual", "Próximo", "Status"};

    /* Cria um ClienteTableModel vazio. */
    public TableModel() {
        linhas = new ArrayList<>(20);
    }

    /* Cria um ClienteTableModel carregado com
     * a lista de CadastroURL especificada. */
    public TableModel(List<CadastroURL> listaDeCliente) {
        linhas = new ArrayList<>(listaDeCliente);
    }


    /* Retorna a quantidade de colunas. */
    @Override
    public int getColumnCount() {
        // EstÃ¡ retornando o tamanho do array "colunas".
        return colunas.length;
    }

    /* Retorna a quantidade de linhas. */
    @Override
    public int getRowCount() {
        // Retorna o tamanho da lista de CadastroURL.
        return linhas.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        // Retorna o conteÃºdo do Array que possui o nome das colunas
        return colunas[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CadastroURL cliente = linhas.get(rowIndex);

        // Retorna o campo referente a coluna especificada.
        // Aqui é feito um switch para verificar qual é a coluna
        // e retornar o campo adequado. As colunas sãoas mesmas
        // que foram especificadas no array "colunas".
        switch (columnIndex) {

            // Seguindo o exemplo: "Tipo","Data de Cadastro", "Nome", "Idade"};
            case 0:
                return cliente.getUrl();
            case 1:
                return cliente.getContadorAtual();
            case 2:
                return cliente.getContadorProximo();
            case 3:
                return cliente.getStatus();
            default:
                throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }
    }

    @Override
    //modifica na linha e coluna especificada
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        CadastroURL cliente = linhas.get(rowIndex); // Carrega o item da linha que deve ser modificado

        switch (columnIndex) { // Seta o valor do campo respectivo
            case 0:
                cliente.setUrl(aValue.toString());
            case 1:
                cliente.setContadorAtual(Integer.valueOf(aValue.toString()));
            case 2:
                cliente.setContadorProximo(Integer.valueOf(aValue.toString()));

            default:
            // Isto não deveria acontecer...             
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    //modifica na linha especificada
    public void setValueAt(CadastroURL aValue, int rowIndex) {
        CadastroURL cliente = linhas.get(rowIndex); // Carrega o item da linha que deve ser modificado

        cliente.setUrl(aValue.getUrl());
        cliente.setContadorAtual(aValue.getContadorAtual());
        cliente.setContadorProximo(aValue.getContadorProximo());

        for (int i = 0; i < colunas.length; i++) {
            fireTableCellUpdated(rowIndex, i);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public CadastroURL getCliente(int indiceLinha) {
        return linhas.get(indiceLinha);
    }

    /* Adiciona um registro. */
    public void addCadastro(CadastroURL m) {
        // Adiciona o registro.
        linhas.add(m);
        int ultimoIndice = getRowCount() - 1;
        fireTableRowsInserted(ultimoIndice, ultimoIndice);
    }

    /* Remove a linha especificada. */
    public void removeCadastro(int indiceLinha) {
        linhas.remove(indiceLinha);

        fireTableRowsDeleted(indiceLinha, indiceLinha);
    }

    /* Adiciona uma lista de CadastroURL ao final dos registros. */
    public void addCadastro(List<CadastroURL> cliente) {
        // Pega o tamanho antigo da tabela.
        int tamanhoAntigo = getRowCount();

        // Adiciona os registros.
        linhas.addAll(cliente);

        fireTableRowsInserted(tamanhoAntigo, getRowCount() - 1);
    }

    /* Remove todos os registros. */
    public void limpar() {
        linhas.clear();


        fireTableDataChanged();
    }

    /* Verifica se este table model esta vazio. */
    public boolean isEmpty() {
        return linhas.isEmpty();
    }

    public List<CadastroURL> getValues() {
        return linhas;
    }

    @Override
    public void fireTableDataChanged() {
        super.fireTableDataChanged();
        /*for (int row = 0; row < linhas.size(); row++) {
            for (int col = 0; col < colunas.length; col++) {
                fireTableCellUpdated(row, col);
            }
        }*/
    }
}
