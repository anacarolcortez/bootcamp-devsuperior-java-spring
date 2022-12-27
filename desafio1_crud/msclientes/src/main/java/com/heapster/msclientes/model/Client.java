package com.heapster.msclientes.model;

import com.heapster.msclientes.dto.ClientDTO;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name="clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String cpf;
    private Double income;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant birthDate;
    private Integer children;

    public Client(){}

    public Client(Long id, String name, String cpf, Double income, Instant birthDate, Integer children) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.income = income;
        this.birthDate = birthDate;
        this.children = children;
    }

    public Client(ClientDTO clientDTO) {
        this.name = clientDTO.getName();
        this.cpf = clientDTO.getCpf();
        this.income = clientDTO.getIncome();
        this.birthDate = clientDTO.getBirthDate();
        this.children = clientDTO.getChildren();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getChildren() {
        return children;
    }

    public void setChildren(Integer children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id.equals(client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Client update(ClientDTO clientDTO) {
        if (clientDTO.getName() != null){
            this.name = clientDTO.getName();
        }

        if (clientDTO.getCpf() != null){
            this.cpf = clientDTO.getCpf();
        }

        if (clientDTO.getIncome() != null){
            this.income = clientDTO.getIncome();
        }

        if (clientDTO.getBirthDate() != null){
            this.birthDate = clientDTO.getBirthDate();
        }

        if (clientDTO.getChildren() != null){
            this.children = clientDTO.getChildren();
        }

        return this;
    }
}
