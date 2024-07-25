package br.com.linkagrotech.userservice.dto;

import lombok.Builder;

@Builder
public record KeyloackCredenciais(String value, String temporary, String userLabel){
}
